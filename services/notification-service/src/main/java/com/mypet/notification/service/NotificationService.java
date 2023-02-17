package com.mypet.notification.service;

import com.mypet.notification.request.NotificationRequest;

public interface NotificationService {

  void sendNotification(NotificationRequest request);
}
