package com.tshen.pet.notification.kafka.consumer;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

  private final INotificationService notificationService;

  @KafkaListener(topics = "notifications", groupId = "notification-service")
  public void notificationConsumer(@Payload NotificationRequest notificationRequest,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
    log.info("Process Kafka notification message [partition={}]", partition);
    notificationService.sendNotification(notificationRequest);
  }
}
