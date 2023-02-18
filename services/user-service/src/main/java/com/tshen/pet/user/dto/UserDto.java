package com.tshen.pet.user.dto;

import lombok.Data;

@Data
public class UserDto {

  private Integer id;
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private String email;
}
