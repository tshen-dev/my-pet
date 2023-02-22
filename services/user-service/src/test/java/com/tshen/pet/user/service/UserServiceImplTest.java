package com.tshen.pet.user.service;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.mapper.UserMapper;
import com.tshen.pet.user.mapper.UserMapperImpl;
import com.tshen.pet.user.model.User;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class UserServiceImplTest {

  private final UserRepo repo = mock(UserRepo.class);
  private final KeycloakClientService keycloakClientService = mock(KeycloakClientService.class);
  private final UserMapper mapper = new UserMapperImpl();
  private final UserServiceImpl instance = new UserServiceImpl(mapper, repo, keycloakClientService);

  @Test
  void whenFindById_givenPresentId_thenReturnUserDto() {
    var user = User.builder()
        .id(1)
        .firstName("Hen")
        .lastName("Tran Sam")
        .email("tshen.petproject@gmail.com")
        .userName("tshen")
        .keycloakId(UUID.randomUUID().toString())
        .build();
    when(repo.findById(1)).thenReturn(of(user));

    var userDto = instance.findById(1);

    assertEquals(user.getFirstName(), userDto.getFirstName());
    assertEquals(user.getLastName(), userDto.getLastName());
    assertEquals(user.getEmail(), userDto.getEmail());
    assertEquals(user.getUserName(), userDto.getUserName());
  }

  @Test
  void whenFindById_givenNotPresentId_thenThrowMyPetException() {
    assertThrows(MyPetRuntimeException.class, () -> instance.findById(1));
  }

  @Test
  void whenCreateUser_givenNotValidUserDto_thenThrowMyPetException() {
    var userDto = new UserDto();

    assertThrows(MyPetRuntimeException.class, () -> instance.createUser(userDto));
  }

  @Test
  void whenCreateUser_givenExistUserDtoInfo_thenThrowMyPetException() {
    var userDto = new UserDto();
    when(repo.findAllByUserNameOrEmail(any(), any())).thenReturn(List.of(new User()));

    assertThrows(MyPetRuntimeException.class, () -> instance.createUser(userDto));
  }

  @Test
  void whenCreateUser_givenValidUserDto_thenCreateUserKeycloak_saveUserToDB() {
    var userDto = UserDto.builder()
        .firstName("Hen")
        .lastName("Tran Sam")
        .email("tshen.petproject@gmail.com")
        .userName("tshen")
        .password("tshen")
        .build();
    var user = mapper.userDtoToUser(userDto);
    when(repo.findAllByUserNameOrEmail(any(), any())).thenReturn(List.of());
    when(repo.save(any())).thenReturn(user);

    var userSaved = instance.createUser(userDto);

    assertEquals(user.getFirstName(), userSaved.getFirstName());
    assertEquals(user.getLastName(), userSaved.getLastName());
    assertEquals(user.getEmail(), userSaved.getEmail());
    assertEquals(user.getUserName(), userSaved.getUserName());
    verify(repo).save(any());
    verify(keycloakClientService).createUser(any());
    verify(keycloakClientService).updateAttribute(any(),any() ,any());
  }

  @Test
  void whenCreateUser_givenValidUserDto_butRepoSaveFail_thenRollBackByDeleteKeycloakUser() {
    var userDto = UserDto.builder()
        .firstName("Hen")
        .lastName("Tran Sam")
        .email("tshen.petproject@gmail.com")
        .userName("tshen")
        .password("tshen")
        .build();
    when(repo.findAllByUserNameOrEmail(any(), any())).thenReturn(List.of());
    when(repo.save(any())).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> instance.createUser(userDto));

    verify(repo).save(any());
    verify(keycloakClientService).createUser(any());
    verify(keycloakClientService).deleteByUserName(any());
  }

  @Test
  void whenCreateUser_givenValidUserDto_butUpdateKeycloakAttributeFail_thenRollBackByDeleteKeycloakUserAndUserInDB() {
    var userDto = UserDto.builder()
        .firstName("Hen")
        .lastName("Tran Sam")
        .email("tshen.petproject@gmail.com")
        .userName("tshen")
        .password("tshen")
        .build();
    var user = mapper.userDtoToUser(userDto);
    when(repo.findAllByUserNameOrEmail(any(), any())).thenReturn(List.of());
    when(repo.save(any())).thenReturn(user);
    doThrow(new RuntimeException()).when(keycloakClientService).updateAttribute(any(),any() ,any());

    assertThrows(RuntimeException.class, () -> instance.createUser(userDto));

    verify(keycloakClientService).createUser(any());
    verify(repo).save(any());
    verify(repo).deleteByUserName(any());
    verify(keycloakClientService).deleteByUserName(any());
  }

  @Test
  void whenFindAll_thenCallRepoFindAll() {
    var pageRequest = PageRequest.of(0, 10);
    when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of()));

    instance.findAll(pageRequest);

    verify(repo).findAll(pageRequest);
  }

  @Test
  void whenDeactivateUser_givenUserId_thenCallKeycloakDeActiveUser() {
    var user = User.builder()
        .id(1)
        .firstName("Hen")
        .lastName("Tran Sam")
        .email("tshen.petproject@gmail.com")
        .userName("tshen")
        .build();
    when(repo.findById(any())).thenReturn(of(user));

    var userDto = instance.deactivateUser(user.getId());

    assertNotNull(userDto);
    verify(keycloakClientService).deActiveUser(userDto.getUserName());
  }
}
