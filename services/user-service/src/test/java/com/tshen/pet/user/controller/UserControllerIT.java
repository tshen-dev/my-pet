package com.tshen.pet.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.user.service.KeycloakClientService;
import com.tshen.pet.utils.client.ApiResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "com.tshen.pet:notification-service:+:stubs:8082")
class UserControllerIT {


  @Autowired MockMvc mvc;
  @MockBean(answer = Answers.RETURNS_DEEP_STUBS) KeycloakClientService keycloakClientService;
  @Autowired UserRepo repo;
  @Autowired ObjectMapper objectMapper;

  @Test
  void POST_users_givenUserDtoMissingField_shouldReturn400WithMessage() throws Exception {
    UserDto userDtoInput = UserDto.builder().build();

    MockHttpServletRequestBuilder requestBuilder = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDtoInput));
    mvc.perform(requestBuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("FirstName should not be empty")));
  }

  @Test
  void POST_users_givenUserDto_shouldReturn201WithUserDataAndUserWasSavedInDB() throws Exception {
    var keycloakId = UUID.randomUUID().toString();
    when(keycloakClientService.createUser(any())).thenReturn(keycloakId);

    UserDto userDtoInput = UserDto.builder()
        .email("tshen.petproject@gmail.com")
        .firstName("Hen")
        .lastName("Tran Sam")
        .userName("tshen")
        .password("tshen")
        .build();

    MockHttpServletRequestBuilder requestBuilder = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDtoInput));
    var responseAsString = mvc.perform(requestBuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated())
        .andExpectAll(
          jsonPath("$.data.firstName", is(userDtoInput.getFirstName())),
          jsonPath("$.data.lastName", is(userDtoInput.getLastName())),
          jsonPath("$.data.userName", is(userDtoInput.getUserName())),
          jsonPath("$.data.email", is(userDtoInput.getEmail())))
        .andReturn().getResponse().getContentAsString();

    var userCreatedId = objectMapper.readValue(responseAsString, new TypeReference<ApiResponse<UserDto>>() {})
        .getData().getId();
    var userDb = repo.findById(userCreatedId)
        .orElseThrow(NullPointerException::new);
    assertEquals(keycloakId, userDb.getKeycloakId());
    assertEquals(userDtoInput.getFirstName(), userDb.getFirstName());
    assertEquals(userDtoInput.getLastName(), userDb.getLastName());
    assertEquals(userDtoInput.getUserName(), userDb.getUserName());
    assertEquals(userDtoInput.getEmail(), userDb.getEmail());
  }
}
