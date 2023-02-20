package com.tshen.pet.notification.contracts.controller.stubs;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.controller.NotificationController;
import com.tshen.pet.utils.client.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public class NotificationControllerStub extends NotificationController {

  public NotificationControllerStub() {
    super(null);
  }

  @Override
  public ResponseEntity<ApiResponse<String>> sendNotification(@RequestBody NotificationRequest request) {
    if (StringUtils.contains(request.getTo(), "500")) {
      return ResponseEntity.internalServerError().body(ApiResponse.error());
    }
    return ResponseEntity.ok(ApiResponse.success());
  }
}
