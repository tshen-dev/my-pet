package com.tshen.pet.user.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.tshen.pet.utils.MethodUtils.processQuietly;
import static com.tshen.pet.utils.MethodUtils.processValidate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.tshen.pet.user.dto.UserCreationProcessDto;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.mapper.UserMapper;
import com.tshen.pet.user.model.User;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.utils.RepoUtils;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserMapper mapper;
  private final UserRepo repo;
  private final KeycloakClientService keycloakClientService;
  private final NotificationService notificationService;

  public UserDto findById(Integer id) {
    return mapper.toUserDto(findByIdThrowIfMissing(id));
  }

  public UserDto findByKeycloakId(String keycloakId) {
    return repo.findByKeycloakId(keycloakId)
        .map(mapper::toUserDto)
        .orElseThrow(() ->
            new MyPetRuntimeException(HttpStatus.NOT_FOUND, "Could not found user by [keycloakId={}]", keycloakId));
  }

  @Transactional
  public UserDto createUser(UserDto userDto) {
    validateUserCreationDto(userDto);
    String userName = userDto.getUserName();
    var userFindByUserNameOrEmail = repo.findAllByUserNameOrEmail(userName, userDto.getEmail());

    if (userFindByUserNameOrEmail.isEmpty()) {
      var userCreationProcessDto = new UserCreationProcessDto();
      try {
        String keycloakId = keycloakClientService.createUser(userDto);
        userCreationProcessDto.setCreatedInKeycloak(true);

        var user = mapper.toUser(userDto);
        user.setKeycloakId(keycloakId);
        user = repo.save(user);
        userCreationProcessDto.setCreatedInSystem(true);

        keycloakClientService.updateAttribute(keycloakId, "user-id", String.valueOf(user.getId()));

        var userCreatedDto = mapper.toUserDto(user);
        notificationService.sendWelcomeMail(userCreatedDto);

        log.info("User created [userName={}]", userName);
        return userCreatedDto;
      } catch (Exception ex) {
        log.info("Create user failed, rollback user [userName={}]", userName);
        rollbackUserCreation(userName, userCreationProcessDto);
        throw ex;
      }
    }
    log.info("Found duplication user [userName={}] [email={}]", userName, userDto.getEmail());
    throw new MyPetRuntimeException(HttpStatus.CONFLICT, "Username/email already existed!");
  }

  private static void validateUserCreationDto(UserDto userDto) {
    processValidate(() -> {
      checkArgument(isNotBlank(userDto.getFirstName()), "FirstName should not be empty");
      checkArgument(isNotBlank(userDto.getLastName()), "LastName should not be empty");
      checkArgument(isNotBlank(userDto.getEmail()), "Email should not be empty");
      checkArgument(isNotBlank(userDto.getUserName()), "UserName should not be empty");
      checkArgument(isNotBlank(userDto.getPassword()), "Password should not be empty");
    });
  }

  private void rollbackUserCreation(String userName, UserCreationProcessDto userCreationProcessDto) {
    if (userCreationProcessDto.isCreatedInSystem()) {
      repo.deleteByUserName(userName);
    }
    if (userCreationProcessDto.isCreatedInKeycloak()) {
      processQuietly(() -> keycloakClientService.deleteByUserName(userName));
    }
  }

  public Page<UserDto> findAll(Pageable pageable) {
    return RepoUtils.findAll(repo, mapper::toUserDto, pageable);
  }

  public UserDto deactivateUser(Integer id) {
    var user = findByIdThrowIfMissing(id);
    this.keycloakClientService.deActiveUser(user.getUserName());
    log.info("De-active user [userName={}]", user.getUserName());
    return mapper.toUserDto(user);
  }

  private User findByIdThrowIfMissing(Integer id) {
    return repo.findById(id)
        .orElseThrow(() ->
            new MyPetRuntimeException(HttpStatus.NOT_FOUND, "Could not found user by [userId={}]", id));
  }

  public void sendVerifyUser(Integer id) {
    notificationService.sendVerifyMail(findById(id));
    log.info("Sent verify mail [userId={}]", id);
  }
}
