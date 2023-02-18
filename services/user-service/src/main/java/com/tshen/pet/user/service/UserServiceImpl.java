package com.tshen.pet.user.service;

import com.tshen.pet.user.dto.UserCreationProcessDto;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.mapper.UserMapper;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

  private final UserMapper mapper;
  private final UserRepo repo;
  private final KeycloakClientService keycloakClientService;

  public UserDto findById(Integer id) {
    return repo.findById(id)
        .map(mapper::userToUserDto)
        .orElseThrow(() -> new MyPetRuntimeException(HttpStatus.NOT_FOUND, "Could not found user by [userId={}]", id));
  }

  @Transactional
  public UserDto createUser(UserDto userDto) {
    String userName = userDto.getUserName();
    var userFindByUserNameOrEmail = repo.findAllByUserNameOrEmail(userName, userDto.getEmail());

    if (userFindByUserNameOrEmail.isEmpty()) {
      var userCreationProcessDto = new UserCreationProcessDto();
      try {
        String keycloakId = keycloakClientService.createUser(userDto);
        userCreationProcessDto.setCreatedInKeycloak(true);

        var user = mapper.userDtoToUser(userDto);
        user.setKeycloakId(keycloakId);
        user = repo.save(user);
        userCreationProcessDto.setCreatedInSystem(true);

        keycloakClientService.updateAttribute(keycloakId, "user-id", String.valueOf(user.getId()));

        log.info("User created [userName={}]", userName);

        return mapper.userToUserDto(user);
      } catch (Exception ex) {
        rollbackUserCreation(userName, userCreationProcessDto);
        throw ex;
      }
    }
    log.info("Found duplication user [userName={}] [email={}]", userName, userDto.getEmail());
    throw new MyPetRuntimeException(HttpStatus.CONFLICT, "Username/email already existed!");
  }

  private void rollbackUserCreation(String userName, UserCreationProcessDto userCreationProcessDto) {
    log.info("Create user failed, rollback user [userName={}]", userName);
    if (userCreationProcessDto.isCreatedInSystem()) {
      repo.deleteByUserName(userName);
    }
    if (userCreationProcessDto.isCreatedInKeycloak()) {
      keycloakClientService.deleteByUserNameQuietly(userName);
    }
  }

  public List<UserDto> findAll(Pageable pageable) {
    return repo.findAll(pageable).stream().map(mapper::userToUserDto).toList();
  }

  public UserDto deactivateUser(Integer id) {
    return repo.findById(id).map(user -> {
      this.keycloakClientService.deActiveUser(user.getUserName());
      return mapper.userToUserDto(user);
    }).orElseThrow(() ->
        new MyPetRuntimeException(HttpStatus.NOT_FOUND, "Could not found user by [userId={}]", id)
    );
  }
}
