package com.tshen.pet.product.repo;

import com.tshen.pet.product.model.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

  ProductCategory findByCode(String code);
}
