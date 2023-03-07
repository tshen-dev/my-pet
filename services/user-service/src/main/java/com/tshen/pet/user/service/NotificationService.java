package com.tshen.pet.user.service;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.webclient.NotificationClient;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationClient notificationClient;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  private void sendKafkaMessage(NotificationRequest notificationRequest) {
    kafkaTemplate.send("notifications", notificationRequest);
  }

  public void sendNotification(NotificationRequest request) {
    try {
      notificationClient.sendNotification(request);
    } catch (FeignClientException feignClientException) {
      //TODO: handle exception
      throw new MyPetRuntimeException("Could not send notification");
    }
  }

  public void sendVerifyMail(UserDto user) {
    var notificationRequest = new NotificationRequest();
    notificationRequest.setTitle("Verify mail");
    notificationRequest.setTo(user.getEmail());
    notificationRequest.setContent("Are you " + user.getUserName() + "?");
    sendKafkaMessage(notificationRequest);
  }

  public void sendWelcomeMail(UserDto user) {
    var notificationRequest = new NotificationRequest();
    notificationRequest.setTitle("Sign up mail");
    notificationRequest.setTo(user.getEmail());
    notificationRequest.setContent("Welcome " + user.getUserName() + "!");
    sendKafkaMessage(notificationRequest);
  }
}
