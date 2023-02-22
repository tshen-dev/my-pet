package com.tshen.pet.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.sender.MailSender;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

  private final MailSender mailSender = mock(MailSender.class);
  private final NotificationService instance = new NotificationService(mailSender);

  @Test
  void whenSendNotification_givenNotificationRequest_ThenCallMailSenderSend() {
    instance.sendNotification(new NotificationRequest());

    verify(mailSender).send(any());
  }
}
