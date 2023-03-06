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
  private @Value("${keycloak.realm}") String keycloakRealm;
  private @Value("${keycloak.admin-user}") String keycloakAdminUser;
  private @Value("${keycloak.admin-password}") String keycloakAdminPassword;

  @Bean
  public RealmResource realmsResource() {
    return KeycloakBuilder.builder()
        .serverUrl(keycloakServerUrl)
        .realm("master")
        .grantType(OAuth2Constants.PASSWORD)
        .username(keycloakAdminUser)
        .password(keycloakAdminPassword)
        .clientId("admin-cli")
        .build()
        .realm(keycloakRealm);
  }
}
