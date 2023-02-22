package com.tshen.pet.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tshen.pet.user.dto.UserDto;
import com.tshen.pet.user.model.User;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.user.service.KeycloakClientService;
import com.tshen.pet.utils.client.ApiResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "com.tshen.pet:notification-service:+:stubs:8082")
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerIT {

  @Autowired MockMvc mvc;
  @MockBean KeycloakClientService keycloakClientService;
  @Autowired UserRepo repo;
  @Autowired ObjectMapper objectMapper;

  @Test
  void POST_users_givenUserDtoMissingField_shouldReturn400WithMessage() throws Exception {
    UserDto userDtoInput = UserDto.builder().build();

    MockHttpServletRequestBuilder requestBuilder = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDtoInput));
    mvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("FirstName should not be empty")));
  }

  @Test
  void testCreatedUser_findById_findAll_deActiveUser() throws Exception {
    // Create user
    var userDb = giveUserDto_whenPOST_users_shouldCreateKeycloakUser_saveUserToDB_return201AndUserInfo();
    // Create with user duplicate mail
    giveUserDtoDuplicateEmail_whenPOST_users_shouldReturn409WithMessage();
    // Find user by id
    giveUserId_whenGET_users_withId_shouldReturn200AndUserInfo(userDb);
    // Find all user
    whenGET_users_shouldReturn200AndListUserInfo(userDb);
    // De-active user
    giveUserId_whenDELETE_users_withId_shouldReturn200AndUserInfo(userDb);
  }

  private void giveUserId_whenDELETE_users_withId_shouldReturn200AndUserInfo(User userDb) throws Exception {
    mvc.perform(delete("/users/{id}", String.valueOf(userDb.getId())))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.firstName", is(userDb.getFirstName())),
            jsonPath("$.data.lastName", is(userDb.getLastName())),
            jsonPath("$.data.userName", is(userDb.getUserName())),
            jsonPath("$.data.email", is(userDb.getEmail())));
    verify(keycloakClientService).deActiveUser(userDb.getUserName());
  }

  private void whenGET_users_shouldReturn200AndListUserInfo(User userDb) throws Exception {
    mvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.[0].firstName", is(userDb.getFirstName())),
            jsonPath("$.data.[0].lastName", is(userDb.getLastName())),
            jsonPath("$.data.[0].userName", is(userDb.getUserName())),
            jsonPath("$.data.[0].email", is(userDb.getEmail())));
  }

  private void giveUserId_whenGET_users_withId_shouldReturn200AndUserInfo(User userDb) throws Exception {
    mvc.perform(get("/users/{id}", String.valueOf(userDb.getId())))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.firstName", is(userDb.getFirstName())),
            jsonPath("$.data.lastName", is(userDb.getLastName())),
            jsonPath("$.data.userName", is(userDb.getUserName())),
            jsonPath("$.data.email", is(userDb.getEmail())));
  }

  private User giveUserDto_whenPOST_users_shouldCreateKeycloakUser_saveUserToDB_return201AndUserInfo() throws Exception {
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
    verify(keycloakClientService).createUser(any());
    verify(keycloakClientService).updateAttribute(keycloakId, "user-id", String.valueOf(userDb.getId()));
    return userDb;
  }

  private void giveUserDtoDuplicateEmail_whenPOST_users_shouldReturn409WithMessage() throws Exception {
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
    mvc.perform(requestBuilder)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message", is("Username/email already existed!")));
  }
}
