package com.tshen.pet.user.webclient;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.utils.client.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "notifications", url = "${feign-client.notification-service.url}")
public interface NotificationClient {

  @PostMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<ApiResponse<String>> sendNotification(@RequestBody NotificationRequest request);
}
