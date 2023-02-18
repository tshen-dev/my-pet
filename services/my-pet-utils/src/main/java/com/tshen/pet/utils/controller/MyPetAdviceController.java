package com.tshen.pet.utils.controller;

import com.tshen.pet.utils.client.ApiResponse;
import com.tshen.pet.utils.exceptions.IMyPetException;
import com.tshen.pet.utils.exceptions.MyPetException;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class MyPetAdviceController {

  @ExceptionHandler(value = {MyPetException.class, MyPetRuntimeException.class})
  protected ResponseEntity<Object> handleMyPetException(Exception ex, WebRequest request) {
    if (ex instanceof IMyPetException iMyPetException) {
      return ResponseEntity.status(iMyPetException.getHttpStatus()).body(ApiResponse.message(ex.getMessage()));
    }
    return ResponseEntity.internalServerError().body(ApiResponse.message(ex.getMessage()));
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.internalServerError().body(ApiResponse.message("Unknown Error, please contact supported!"));
  }
}
