package com.tshen.pet.user.controller.stubs;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.tshen.pet.user.controller.UserController;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.utils.client.ApiResponse;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public class UserControllerStub extends UserController {


  public UserControllerStub() {
    super(null);
  }

  @Override
  public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto) {
    if (isBlank(userDto.getFirstName())) {
      throw new MyPetRuntimeException(HttpStatus.BAD_REQUEST, "FirstName should not be empty");
    }
    userDto.setId(RandomUtils.nextInt(1, Integer.MAX_VALUE));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(userDto));
  }

  @Override
  public ResponseEntity<ApiResponse<Page<UserDto>>> getAll(Pageable pageable) {
    return ResponseEntity.ok(ApiResponse.success(new PageImpl<>(List.of(
        UserDto.builder().firstName("Hen").lastName("Tran Sam")
            .userName("tshen").email("tshen.petproject@tshen.com").build(),
        UserDto.builder().firstName("Teo").lastName("Tony")
            .userName("ttony").email("ttony.petproject@tshen.com").build()
    ), pageable, 2L)));
  }

  @Override
  public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
    return ResponseEntity.ok(ApiResponse.success(
        UserDto.builder().id(id).firstName("Hen").lastName("Tran Sam")
            .userName("tshen").email("tshen.petproject@tshen.com").build()
    ));
  }

  @Override
  public ResponseEntity<ApiResponse<UserDto>> deactivateUser(Integer id) {
    return ResponseEntity.ok(ApiResponse.success());
  }
}
