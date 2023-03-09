package com.tshen.pet.product.service;

import com.tshen.pet.product.dto.ProductDto;
import com.tshen.pet.product.repo.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repo;

  public List<ProductDto> findAll(Pageable pageable) {
    return null;
  }
}
