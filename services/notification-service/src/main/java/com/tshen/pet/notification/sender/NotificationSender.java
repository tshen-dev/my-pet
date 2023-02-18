package com.tshen.pet.notification.sender;

public interface NotificationSender<I> {

  void send(I input);
}
