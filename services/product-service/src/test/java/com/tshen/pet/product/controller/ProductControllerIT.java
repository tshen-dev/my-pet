package com.tshen.pet.product.controller;

import com.tshen.pet.product.repo.ProductCategoryRepository;
import com.tshen.pet.product.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
class ProductControllerIT {

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProductCategoryRepository productCategoryRepository;

  @Autowired
  MongoTemplate mongoTemplate;

}
