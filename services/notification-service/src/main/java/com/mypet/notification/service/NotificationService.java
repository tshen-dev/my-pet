package com.mypet.notification.service;

import com.mypet.notification.client.request.NotificationRequest;

public interface NotificationService {

  void sendNotification(NotificationRequest request);
}
