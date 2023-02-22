package com.tshen.pet.user.contracts;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

import com.tshen.pet.user.controller.stubs.UserControllerStub;
import com.tshen.pet.utils.controller.MyPetAdviceController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@ActiveProfiles(profiles = "test")
abstract class ContractVerifierBase {

  @BeforeEach
  public void setup() {
    standaloneSetup(MockMvcBuilders.standaloneSetup(
          new MyPetAdviceController(),
          new UserControllerStub())
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()));
  }
}
