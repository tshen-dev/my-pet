package com.tshen.pet.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.sender.MailSender;
import org.junit.jupiter.api.Test;

class NotificationServiceImplTest {

  private final MailSender mailSender = mock(MailSender.class);
  private final NotificationServiceImpl instance = new NotificationServiceImpl(mailSender);

  @Test
  void sendNotification_callMailSenderSend() {
    instance.sendNotification(new NotificationRequest());

    verify(mailSender).send(any());
  }
}
