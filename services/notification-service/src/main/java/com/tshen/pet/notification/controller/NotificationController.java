package com.tshen.pet.notification.controller;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.service.NotificationService;
import com.tshen.pet.utils.client.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping
  public ResponseEntity<ApiResponse<String>> sendNotification(@RequestBody NotificationRequest request) {
    notificationService.sendNotification(request);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
