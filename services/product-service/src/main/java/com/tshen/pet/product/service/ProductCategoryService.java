package com.tshen.pet.product.service;

import com.tshen.pet.product.repo.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryService {

  private final ProductCategoryRepository repo;

}
