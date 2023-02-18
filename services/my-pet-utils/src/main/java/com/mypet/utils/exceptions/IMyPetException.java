package com.mypet.utils.exceptions;

import org.springframework.http.HttpStatus;

public interface IMyPetException {

  HttpStatus getHttpStatus();

  String getMessage();
}
