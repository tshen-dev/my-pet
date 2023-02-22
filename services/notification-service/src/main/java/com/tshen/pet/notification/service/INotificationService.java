package com.tshen.pet.notification.service;

import com.tshen.pet.notification.client.request.NotificationRequest;

public interface INotificationService {

  void sendNotification(NotificationRequest request);
}
