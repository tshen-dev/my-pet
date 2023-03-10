package com.tshen.pet.notification.sender.input;

import com.tshen.pet.notification.client.request.NotificationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
