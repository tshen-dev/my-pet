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
import com.tshen.pet.user.service.NotificationService;
import com.tshen.pet.utils.client.ApiResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(stubs = "classpath:/META-INF/com.tshen.pet/notification-service/**/*.json")
class UserControllerIT {

  @Autowired MockMvc mvc;
  @MockBean KeycloakClientService keycloakClientService;
  @Autowired UserRepo repo;
  @Autowired ObjectMapper objectMapper;
  @Autowired
  NotificationService notificationService;

  @Test
  void whenPOST_users_givenUserDtoMissingField_shouldReturn400WithMessage() throws Exception {
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
    var userDb = whenPOST_users_givenUserDto_thenCreateKeycloakUser_saveUserToDB_return201AndUserInfo();
    // Create with user duplicate mail
    whenPOST_users_givenUserDtoDuplicateEmail_thenReturn409WithMessage();
    // Find user by id
    whenGET_users_withId_givenUserId_thenReturn200AndUserInfo(userDb);
    // Find all user
    whenGET_users_thenReturn200AndListUserInfo(userDb);
    // De-active user
    whenDELETE_users_withId_givenUserId_thenReturn200AndUserInfo(userDb);
  }

  private void whenDELETE_users_withId_givenUserId_thenReturn200AndUserInfo(User userDb) throws Exception {
    mvc.perform(delete("/users/{id}", String.valueOf(userDb.getId())))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.firstName", is(userDb.getFirstName())),
            jsonPath("$.data.lastName", is(userDb.getLastName())),
            jsonPath("$.data.userName", is(userDb.getUserName())),
            jsonPath("$.data.email", is(userDb.getEmail())));
    verify(keycloakClientService).deActiveUser(userDb.getUserName());
  }

  private void whenGET_users_thenReturn200AndListUserInfo(User userDb) throws Exception {
    mvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.content.[0].firstName", is(userDb.getFirstName())),
            jsonPath("$.data.content.[0].lastName", is(userDb.getLastName())),
            jsonPath("$.data.content.[0].userName", is(userDb.getUserName())),
            jsonPath("$.data.content.[0].email", is(userDb.getEmail())));
  }

  private void whenGET_users_withId_givenUserId_thenReturn200AndUserInfo(User userDb) throws Exception {
    mvc.perform(get("/users/{id}", String.valueOf(userDb.getId())))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.data.firstName", is(userDb.getFirstName())),
            jsonPath("$.data.lastName", is(userDb.getLastName())),
            jsonPath("$.data.userName", is(userDb.getUserName())),
            jsonPath("$.data.email", is(userDb.getEmail())));
  }

  private User whenPOST_users_givenUserDto_thenCreateKeycloakUser_saveUserToDB_return201AndUserInfo() throws Exception {
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

  private void whenPOST_users_givenUserDtoDuplicateEmail_thenReturn409WithMessage() throws Exception {
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
