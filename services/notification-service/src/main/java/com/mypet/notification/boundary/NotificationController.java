package com.mypet.notification.boundary;

import com.mypet.notification.client.request.NotificationRequest;
import com.mypet.notification.service.NotificationService;
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
  public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
    notificationService.sendNotification(request);
    return ResponseEntity.noContent().build();
  }
}
