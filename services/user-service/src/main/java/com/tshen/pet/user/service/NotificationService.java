package com.tshen.pet.user.service;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.user.webclient.NotificationClient;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationClient notificationClient;

  public void sendNotification(NotificationRequest request) {
    try {
      notificationClient.sendNotification(request);
    } catch (FeignClientException feignClientException) {
      //TODO: handle exception
      throw new MyPetRuntimeException("Could not send notification");
    }
  }
}
