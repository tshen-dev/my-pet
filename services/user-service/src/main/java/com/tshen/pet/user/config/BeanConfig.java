package com.tshen.pet.user.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  private @Value("${keycloak.auth-server-url}") String keycloakServerUrl;

  @Bean
  public RealmResource realmsResource() {
    KeycloakBuilder keycloakBuilder = KeycloakBuilder.builder()
        .serverUrl(keycloakServerUrl)
        .realm("master")
        .grantType(OAuth2Constants.PASSWORD)
        .username("admin")
        .password("admin")
        .clientId("admin-cli");

    try (var keycloak = keycloakBuilder.build()) {
      return keycloak.realm("MyPetDev");
    }
  }
}
