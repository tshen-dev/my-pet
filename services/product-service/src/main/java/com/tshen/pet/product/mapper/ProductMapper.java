package com.tshen.pet.product.mapper;

import com.tshen.pet.product.dto.ProductDto;
import com.tshen.pet.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProductMapper {

  Product toProduct(ProductDto productDto);

  ProductDto toProductDto(Product product);
}
