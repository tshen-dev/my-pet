package com.tshen.pet.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tshen.pet.user.dto.UserDto;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

class KeycloakClientServiceTest {

  private final RealmResource realmResource = mock(RealmResource.class, RETURNS_DEEP_STUBS);
  private final KeycloakClientService instance = new KeycloakClientService(realmResource);

  @Test
  void createUser_returnUserIdFromKeycloak() {
    var response = Response.created(UriBuilder.fromUri("http://keycloak/.../1234-1234").build()).build();
    when(realmResource.users().create(any())).thenReturn(response);

    var result = instance.createUser(new UserDto());

    verify(realmResource.users()).create(any());
    assertEquals("1234-1234", result);
  }

  @Test
  void updateAttribute_callUpdateToKeycloak() {
    var userMock = mock(UserResource.class);
    when(realmResource.users().get("user-id")).thenReturn(userMock);

    instance.updateAttribute("user-id", "key", "value");

    verify(userMock).update(any());
  }

  @Test
  void deleteByUserNameQuietly_callRemoveToKeycloak() {
    var userMock = mock(UserResource.class);
    when(realmResource.users().search(anyString())).thenReturn(List.of(new UserRepresentation()));
    when(realmResource.users().get(any())).thenReturn(userMock);

    instance.deleteByUserNameQuietly("user-name");

    verify(userMock).remove();
  }

  @Test
  void deActiveUser_callSetEnabledToKeycloak() {
    var userRepresentationMock = mock(UserRepresentation.class);
    when(realmResource.users().search(anyString())).thenReturn(List.of(userRepresentationMock));

    instance.deActiveUser("user-name");

    verify(userRepresentationMock).setEnabled(false);
  }
}
