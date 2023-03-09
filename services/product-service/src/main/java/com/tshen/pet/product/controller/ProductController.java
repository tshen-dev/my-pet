package com.tshen.pet.product.controller;

import static com.tshen.pet.utils.client.ApiResponse.success;

import com.tshen.pet.product.dto.ProductDto;
import com.tshen.pet.product.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductDto>>> findAll(Pageable pageable) {
    return ResponseEntity.ok(success(productService.findAll(pageable)));
  }

  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<ProductDto>> findById(@PathVariable String id) {
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductDto>> findById(@RequestBody ProductDto productDto) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Objects> deleteById(@PathVariable String id) {
    return ResponseEntity.noContent().build();
  }
}
