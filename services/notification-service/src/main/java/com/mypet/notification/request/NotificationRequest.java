package com.mypet.notification.request;

import lombok.Data;

@Data
public class NotificationRequest {

  private String from;
  private String to;
  private String title;
  private String content;
}
