package com.tshen.pet.user.contracts;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
public abstract class ContractVerifierBase {

  @BeforeEach
  public void setup() {
//    standaloneSetup(MockMvcBuilders.standaloneSetup(new MyPetAdviceController(),
//        new UserController(new UserServiceImpl())));
  }
}
