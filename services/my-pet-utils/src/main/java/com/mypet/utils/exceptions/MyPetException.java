package com.mypet.utils.exceptions;

import org.springframework.http.HttpStatus;

public class MyPetException extends Exception implements IMyPetException {

  private final HttpStatus httpStatus;

  public MyPetException() {
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
