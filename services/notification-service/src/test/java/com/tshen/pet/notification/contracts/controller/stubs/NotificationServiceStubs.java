package com.tshen.pet.notification.contracts.controller.stubs;

import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.service.NotificationService;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import org.apache.commons.lang3.StringUtils;

public class NotificationServiceStubs implements NotificationService {

  @Override
  public void sendNotification(NotificationRequest request) {
    if (StringUtils.contains(request.getTo(), "500")) {
      throw new MyPetRuntimeException();
    }
  }
}
