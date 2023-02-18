package com.mypet.user.boundary;

import com.mypet.user.dto.UserDto;
import com.mypet.user.service.UserServiceImpl;
import com.mypet.utils.client.ApiResponse;
import com.mypet.utils.exceptions.MyPetRuntimeException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  private final UserServiceImpl userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto) {
    return this.userService.createUser(userDto)
        .map(i -> ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(i)))
        .orElseThrow(() -> new MyPetRuntimeException("Exception throws"));
  }

  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
    return this.userService.findById(id)
        .map(ApiResponse::success)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserDto>>> getAll(Pageable pageable) {
    return ResponseEntity.ok(ApiResponse.success(this.userService.findAll(pageable)));
  }
}
