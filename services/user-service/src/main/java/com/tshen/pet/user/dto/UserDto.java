package com.tshen.pet.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private Integer id;
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private String email;
}
