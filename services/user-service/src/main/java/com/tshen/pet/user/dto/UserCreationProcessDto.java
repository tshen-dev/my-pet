package com.tshen.pet.user.dto;

import lombok.Data;

@Data
public class UserCreationProcessDto {

  private boolean createdInSystem;
  private boolean createdInKeycloak;
}
