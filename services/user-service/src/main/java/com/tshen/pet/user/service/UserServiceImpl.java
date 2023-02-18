package com.tshen.pet.user.service;

import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.mapper.UserMapper;
import com.tshen.pet.user.repo.UserRepo;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

  private final UserMapper mapper;
  private final UserRepo repo;
  private final KeycloakClientService keycloakClientService;

  public Optional<UserDto> findById(Integer id) {
    return repo.findById(id).map(mapper::userToUserDto);
  }

  @Transactional
  public Optional<UserDto> createUser(UserDto userDto) {
    String userName = userDto.getUserName();
    var userFindByUserNameOrEmail = repo.findAllByUserNameOrEmail(userName, userDto.getEmail());

    if (userFindByUserNameOrEmail.isEmpty()) {
      try {
        String keycloakId = keycloakClientService.createUser(userDto);
        var user = mapper.userDtoToUser(userDto);
        user.setKeycloakId(keycloakId);
        user = repo.save(user);
        keycloakClientService.updateAttribute(keycloakId, "user-id", String.valueOf(user.getId()));

        log.info("User created {}", userName);

        return Optional.of(user).map(mapper::userToUserDto);
      } catch (Exception ex) {
        log.info("Create user failed, rollback user {}", userName);
        keycloakClientService.deleteByUserNameQuietly(userName);
        repo.deleteByUserName(userName);

        throw ex;
      }
    }
    log.info("Found duplication user userName {} email {}", userName, userDto.getEmail());
    return Optional.empty();
  }

  public List<UserDto> findAll(Pageable pageable) {
    return repo.findAll(pageable).stream().map(mapper::userToUserDto).toList();
  }
}
