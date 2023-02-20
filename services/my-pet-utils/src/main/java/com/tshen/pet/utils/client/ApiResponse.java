package com.tshen.pet.utils.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

  private String message;
  private T data;

  public static <U> ApiResponse<U> of(String message, U data) {
    return new ApiResponse<>(message, data);
  }

  public static <U> ApiResponse<U> success(U data) {
    return of("SUCCESS", data);
  }

  public static <U> ApiResponse<U> success() {
    return success(null);
  }

  public static <U> ApiResponse<U> error(U data) {
    return of("ERROR", data);
  }

  public static <U> ApiResponse<U> error() {
    return error(null);
  }

  public static <U> ApiResponse<U> message(String message) {
    return of(message, null);
  }
}
