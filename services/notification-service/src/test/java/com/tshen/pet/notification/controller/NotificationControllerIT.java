package com.tshen.pet.notification.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.service.INotificationService;
import com.tshen.pet.utils.exceptions.MyPetRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
class NotificationControllerIT {

  @Autowired
  private MockMvc mvc;
  @SpyBean
  private INotificationService notificationService;

  @Test
  void whenSendNotification_givenNotificationRequest_thenReturnStatus200() throws Exception {
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
  }

  @Test
  void whenSendNotification_givenTransportSendFail_thenReturnStatus500() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    var notificationRequest = new NotificationRequest();
    notificationRequest.setTo("tshen.petproject@gmail.com");
    doThrow(new MyPetRuntimeException()).when(notificationService).sendNotification(notificationRequest);

    MockHttpServletRequestBuilder requestBuilder = post("/notifications")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(notificationRequest));
    mvc.perform(requestBuilder)
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }
}