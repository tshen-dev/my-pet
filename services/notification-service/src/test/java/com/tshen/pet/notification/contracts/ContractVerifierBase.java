package com.tshen.pet.notification.contracts;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

import com.tshen.pet.notification.contracts.controller.stubs.NotificationControllerStub;
import com.tshen.pet.utils.controller.MyPetAdviceController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
public abstract class ContractVerifierBase {

  @BeforeEach
  public void setup() {
    standaloneSetup(MockMvcBuilders.standaloneSetup(new MyPetAdviceController(),
        new NotificationControllerStub()));
  }
}
