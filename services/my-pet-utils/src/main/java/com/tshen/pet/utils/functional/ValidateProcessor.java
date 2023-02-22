package com.tshen.pet.utils.functional;

@FunctionalInterface
public interface ValidateProcessor {

  void processValidate() throws IllegalArgumentException;
}
