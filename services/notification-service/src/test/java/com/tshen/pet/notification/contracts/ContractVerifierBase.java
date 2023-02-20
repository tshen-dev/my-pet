package com.tshen.pet.notification.contracts;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

import com.tshen.pet.notification.NotificationServiceApplication;
import com.tshen.pet.notification.client.request.NotificationRequest;
import com.tshen.pet.notification.controller.NotificationController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK,
  classes = NotificationServiceApplication.class,
  properties = {
    "spring.cloud.config.enabled=false"
  }
)
@DirtiesContext
@AutoConfigureMessageVerifier
public abstract class ContractVerifierBase {

  @BeforeAll
  public static void setup() {
    standaloneSetup(MockMvcBuilders.standaloneSetup(new NotificationControllerStub()));
  }

  static class NotificationControllerStub extends NotificationController {

    public NotificationControllerStub() {
      super(null);
    }

    @Override
    public ResponseEntity<String> sendNotification(NotificationRequest request) {
      return ResponseEntity.noContent().build();
    }
  }
}
