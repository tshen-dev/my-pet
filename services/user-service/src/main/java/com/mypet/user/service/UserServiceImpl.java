package com.mypet.user.service;

import com.mypet.user.dto.UserDto;
import com.mypet.user.mapper.UserMapper;
import com.mypet.user.repo.UserRepo;
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
    var userFindByUserNameOrEmail = repo.findAllByUserNameOrEmail(userDto.getUserName(), userDto.getEmail());
    if (userFindByUserNameOrEmail.isEmpty()) {
      try {
        String keycloakId = keycloakClientService.createUser(userDto);
        var user = mapper.userDtoToUser(userDto);
        user.setKeycloakId(keycloakId);
        user = repo.save(user);
        keycloakClientService.updateAttribute(keycloakId, "user-id", String.valueOf(user.getId()));

        log.info("User created {}", userDto.getUserName());

        return Optional.of(user).map(mapper::userToUserDto);
      } catch (Exception ex) {
        keycloakClientService.deleteByUserNameQuietly(userDto.getUserName());
        repo.deleteByUserName(userDto.getUserName());

        throw ex;
      }
    }
    log.info("Found duplication user userName {} email {}", userDto.getUserName(), userDto.getEmail());
    return Optional.empty();
  }

  public List<UserDto> findAll(Pageable pageable) {
    return repo.findAll(pageable).stream().map(mapper::userToUserDto).toList();
  }
}
