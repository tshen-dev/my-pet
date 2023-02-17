package com.mypet.notification.service;

import com.mypet.notification.request.NotificationRequest;
import com.mypet.notification.sender.MailSender;
import com.mypet.notification.sender.input.MailInput;
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
