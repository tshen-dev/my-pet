package com.tshen.pet.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.model.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

  @Test
  void testUserToUserDto() {
    var user = User.builder()
        .id(1)
        .firstName("Hen")
        .lastName("Tran Sam")
        .userName("tshen")
        .email("tshen.petproject@gmail.com")
        .keycloakId(UUID.randomUUID().toString())
        .build();

    UserMapper mapper = Mappers.getMapper(UserMapper.class);
    var userDto = mapper.userToUserDto(user);

    assertEquals(user.getId(), userDto.getId());
    assertEquals(user.getFirstName(), userDto.getFirstName());
    assertEquals(user.getLastName(), userDto.getLastName());
    assertEquals(user.getEmail(), userDto.getEmail());
    assertEquals(user.getUserName(), userDto.getUserName());
    assertNull(mapper.userToUserDto(null));
  }

  @Test
  void testUserDtoToUser() {
    var userDto = UserDto.builder()
        .id(1)
        .firstName("Hen")
        .lastName("Tran Sam")
        .userName("tshen")
        .email("tshen.petproject@gmail.com")
        .password(UUID.randomUUID().toString())
        .build();

    UserMapper mapper = Mappers.getMapper(UserMapper.class);
    var user = mapper.userDtoToUser(userDto);

    assertEquals(userDto.getId(), user.getId());
    assertEquals(userDto.getFirstName(), user.getFirstName());
    assertEquals(userDto.getLastName(), user.getLastName());
    assertEquals(userDto.getEmail(), user.getEmail());
    assertEquals(userDto.getUserName(), user.getUserName());
    assertNull(mapper.userDtoToUser(null));
  }
}