package com.mypet.notification.sender;

public interface NotificationSender<I> {

  void send(I input);
}
