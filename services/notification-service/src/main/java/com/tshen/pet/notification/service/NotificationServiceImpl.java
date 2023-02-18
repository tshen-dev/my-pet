package com.tshen.pet.notification.service;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.sender.MailSender;
import com.tshen.pet.notification.sender.input.MailInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final MailSender mailSender;

  @Override
  public void sendNotification(NotificationRequest request) {
    mailSender.send(new MailInput(request));
  }
}
