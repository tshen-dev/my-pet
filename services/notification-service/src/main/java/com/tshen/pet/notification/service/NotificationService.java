package com.tshen.pet.notification.service;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.sender.MailSender;
import com.tshen.pet.notification.sender.input.MailInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "notification-service.is-simulator", havingValue = "false")
public class NotificationService implements INotificationService {

  private final MailSender mailSender;

  @Override
  public void sendNotification(NotificationRequest request) {
    log.info("Sending notification [from={}] [to={}]", request.getFrom(), request.getTo());
    mailSender.send(new MailInput(request));
  }
}
