package com.tshen.pet.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodUtils {

  public static void processQuietly(Runnable runnable) {
    try {
      runnable.run();
    } catch (Exception ex) {
      log.warn("Error when process quietly", ex);
    }
  }
}
