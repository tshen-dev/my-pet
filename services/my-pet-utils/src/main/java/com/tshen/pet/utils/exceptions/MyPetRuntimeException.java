package com.tshen.pet.utils.exceptions;

import org.springframework.http.HttpStatus;

public class MyPetRuntimeException extends RuntimeException implements IMyPetException {

  private final HttpStatus httpStatus;

  public MyPetRuntimeException() {
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public MyPetRuntimeException(String message) {
    super(message);
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
