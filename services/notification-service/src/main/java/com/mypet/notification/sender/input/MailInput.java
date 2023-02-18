package com.mypet.notification.sender.input;

import com.mypet.notification.client.request.NotificationRequest;
import lombok.Data;

@Data
public class MailInput {

  private String from;
  private String to;
  private String title;
  private String content;

  public MailInput(NotificationRequest notificationRequest) {
    this.from = notificationRequest.getFrom();
    this.to = notificationRequest.getTo();
    this.title = notificationRequest.getTitle();
    this.content = notificationRequest.getContent();
  }
}
