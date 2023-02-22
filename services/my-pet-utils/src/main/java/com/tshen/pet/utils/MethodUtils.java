package com.tshen.pet.utils;

import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import com.tshen.pet.utils.functional.QuietlyProcessor;
import com.tshen.pet.utils.functional.ValidateProcessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodUtils {

  public static void processQuietly(QuietlyProcessor quietlyProcessor) {
    try {
      quietlyProcessor.run();
    } catch (Exception ex) {
      log.warn("Error when process quietly", ex);
    }
  }

  public static void processValidate(ValidateProcessor validateProcessor) {
    try {
      validateProcessor.processValidate();
    } catch (IllegalArgumentException ex) {
      throw new MyPetRuntimeException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }
}
