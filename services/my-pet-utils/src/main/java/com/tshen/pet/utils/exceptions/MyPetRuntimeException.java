package com.tshen.pet.utils.exceptions;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

public class MyPetRuntimeException extends RuntimeException implements IMyPetException {

  private final HttpStatus httpStatus;

  public MyPetRuntimeException() {
    this("Unknown Error, please contact supported!");
  }

  public MyPetRuntimeException(HttpStatus httpStatus, String message, Object... args) {
    super(MessageFormatter.arrayFormat(message, args).getMessage());
    this.httpStatus = httpStatus;
  }

  public MyPetRuntimeException(String message, Object... args) {
    super(MessageFormatter.arrayFormat(message, args).getMessage());
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
