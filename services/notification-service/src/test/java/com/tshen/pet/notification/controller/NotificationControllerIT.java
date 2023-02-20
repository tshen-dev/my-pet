package com.tshen.pet.notification.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tshen.pet.notification.client.request.NotificationRequest;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIT {

  @Autowired
  private MockMvc mvc;

  @Test
  void givenNotificationRequest_whenSendNotification_thenCallTransportSend_andReturnStatus200() throws Exception {
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      ObjectMapper mapper = new ObjectMapper();
      var notificationRequest = new NotificationRequest();
      notificationRequest.setTo("tshen.petproject@gmail.com");

      MockHttpServletRequestBuilder requestBuilder = post("/notifications")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(notificationRequest));
      mvc.perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("SUCCESS")));

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }

  @Test
  void givenTransportSendFail_whenSendNotification_andReturnStatus500() throws Exception {
    try (MockedStatic<Transport> transportMockedStatic = Mockito.mockStatic(Transport.class)) {
      ObjectMapper mapper = new ObjectMapper();
      var notificationRequest = new NotificationRequest();
      notificationRequest.setTo("tshen.petproject@gmail.com");
      transportMockedStatic.when(() -> Transport.send(any())).thenThrow(new RuntimeException());

      MockHttpServletRequestBuilder requestBuilder = post("/notifications")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(notificationRequest));
      mvc.perform(requestBuilder)
          .andExpect(status().isInternalServerError())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

      transportMockedStatic.verify(() -> Transport.send(any()));
    }
  }
}