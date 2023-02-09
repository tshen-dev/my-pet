package com.mypet.notification.boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/pingg")
  public ResponseEntity<String> getEnv() {
    return ResponseEntity.ok("pongg");
  }
}
