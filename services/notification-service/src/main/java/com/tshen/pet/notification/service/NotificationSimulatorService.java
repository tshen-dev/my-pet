package com.tshen.pet.notification.service;

import com.tshen.pet.notification.client.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "notification-service.is-simulator", havingValue = "true", matchIfMissing = true)
public class NotificationSimulatorService implements INotificationService {

  @Override
  public void sendNotification(NotificationRequest request) {
    log.info("Simulate sending notification [from={}] [to={}] [title={}]: {}",
        request.getFrom(), request.getTo(), request.getTitle(), request.getContent());
  }
}
