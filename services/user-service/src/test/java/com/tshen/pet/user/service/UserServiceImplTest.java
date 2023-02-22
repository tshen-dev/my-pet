package com.tshen.pet.user.service;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import com.tshen.pet.user.mapper.UserMapperImpl;
import com.tshen.pet.user.repo.UserRepo;
import com.tshen.pet.user.webclient.NotificationClient;
import org.junit.jupiter.api.Test;

class UserServiceImplTest {

  private final UserRepo repo = mock(UserRepo.class, RETURNS_DEEP_STUBS);
  private final KeycloakClientService keycloakClientService = mock(KeycloakClientService.class, RETURNS_DEEP_STUBS);
  private final NotificationClient notificationClient = mock(NotificationClient.class, RETURNS_DEEP_STUBS);

  private final UserServiceImpl instance = new UserServiceImpl(new UserMapperImpl(), repo, keycloakClientService, notificationClient);

  @Test
  void find() {
//    instance.findById(1);
  }
}