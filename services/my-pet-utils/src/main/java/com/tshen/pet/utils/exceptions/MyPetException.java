package com.tshen.pet.utils.exceptions;

import org.springframework.http.HttpStatus;

public class MyPetException extends Exception implements IMyPetException {

  private final HttpStatus httpStatus;

  public MyPetException() {
    super("Unknown Error, please contact supported!");
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public MyPetException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
