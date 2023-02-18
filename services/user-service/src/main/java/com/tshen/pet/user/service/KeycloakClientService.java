package com.tshen.pet.user.service;

import static com.tshen.pet.utils.MethodUtils.processQuietly;

import com.tshen.pet.user.dto.UserDto;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakClientService {

  private final RealmResource realmResource;

  public String createUser(UserDto userDto) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(userDto.getUserName());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());
    user.setCredentials(Collections.singletonList(createPasswordCredentials(userDto.getPassword())));
    user.setEnabled(true);

    try (var response = realmResource.users().create(user)) {
      log.info("Creating user {} in Keycloak", userDto.getUserName());
      var uri = response.getLocation();
      var path = uri.getPath();
      return path.substring(path.lastIndexOf('/') + 1);
    }
  }

  public void updateAttribute(String id, String key, String value) {
    UserRepresentation user = new UserRepresentation();
    user.singleAttribute(key, value);
    realmResource.users().get(id).update(user);
    log.info("Updated user attribute {}: {} for {} in Keycloak", key, value, id);
  }

  public void deleteByUserName(String userName) {
    realmResource.users().search(userName)
        .forEach(i -> {
          realmResource.users().get(i.getId()).remove();
          log.info("Removed user {}: {} in Keycloak", i.getId(), i.getUsername());
        });
  }

  public void deleteByUserNameQuietly(String userName) {
    processQuietly(() -> deleteByUserName(userName));
  }

  private static CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }
}
