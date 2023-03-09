package com.tshen.pet.product.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Product {

  @Id
  private ObjectId id;
  private String code;
  private ProductCategory productCategory;
}
