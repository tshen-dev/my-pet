package com.tshen.pet.product.controller;

import com.tshen.pet.product.dto.ProductCategoryDto;
import com.tshen.pet.utils.client.ApiResponse;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-category")
@RequiredArgsConstructor
public class ProductCategoryController {

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductCategoryDto>>> findAll(Pageable pageable) {
    return ResponseEntity.ok().build();
  }

  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<ProductCategoryDto>> findById(@PathVariable String id) {
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductCategoryDto>> findById(@RequestBody ProductCategoryDto productDto) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Objects> deleteById(@PathVariable String id) {
    return ResponseEntity.noContent().build();
  }
}
