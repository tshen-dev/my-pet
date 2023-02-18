package com.mypet.user.mapper;

import com.mypet.user.dto.UserDto;
import com.mypet.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

  @Mapping(target = "keycloakId", ignore = true)
  User userDtoToUser(UserDto userDto);

  @Mapping(target = "password", ignore = true)
  UserDto userToUserDto(User user);
}
