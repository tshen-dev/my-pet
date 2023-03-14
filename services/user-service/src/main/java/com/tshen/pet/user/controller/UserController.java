package com.tshen.pet.user.controller;

import static com.tshen.pet.utils.client.ApiResponse.success;

import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.service.UserService;
import com.tshen.pet.utils.client.ApiResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(success(this.userService.createUser(userDto)));
  }

  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
    return ResponseEntity.ok(success(this.userService.findById(id)));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Page<UserDto>>> getAll(Pageable pageable) {
    return ResponseEntity.ok(success(this.userService.findAll(pageable)));
  }

  @GetMapping("info")
  public ResponseEntity<ApiResponse<UserDto>> getInfo() {
    var userDto = Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .map(KeycloakPrincipal.class::cast)
        .map(KeycloakPrincipal::getName)
        .map(userService::findByKeycloakId)
        .orElse(null);

    return ResponseEntity.ok(success(userDto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponse<UserDto>> deactivateUser(@PathVariable Integer id) {
    return ResponseEntity.ok(success(this.userService.deactivateUser(id)));
  }

  @PostMapping("/email-verify/{id}")
  public ResponseEntity<Object> verify(@PathVariable Integer id) {
    this.userService.sendVerifyUser(id);
    return ResponseEntity.noContent().build();
  }
}
