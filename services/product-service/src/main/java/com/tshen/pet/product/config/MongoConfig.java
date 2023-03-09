package com.tshen.pet.product.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.lang.NonNull;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Value("${mongo.url}")
  private String mongoDbUrl;

  @Override
  @NonNull
  protected String getDatabaseName() {
    return "product-service";
  }

  @Override
  @NonNull
  public MongoClient mongoClient() {
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(mongoDbUrl))
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  @Override
  @NonNull
  public Collection<String> getMappingBasePackages() {
    return Collections.singleton("com.tshen.pet.product.model");
  }
}
