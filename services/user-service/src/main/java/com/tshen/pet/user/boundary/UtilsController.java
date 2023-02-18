package com.tshen.pet.user.boundary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utils")
public class UtilsController {

  @Value("${environment}")
  private String env;

  @GetMapping("/environment")
  public ResponseEntity<String> getEnv() {
    return ResponseEntity.ok(env);
  }
}
