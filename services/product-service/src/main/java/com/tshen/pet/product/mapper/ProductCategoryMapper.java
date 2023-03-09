package com.tshen.pet.product.mapper;


import com.tshen.pet.product.dto.ProductCategoryDto;
import com.tshen.pet.product.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProductCategoryMapper {

  ProductCategory toProductCategory(ProductCategoryDto productCategoryDto);

  ProductCategoryDto toProductCategoryDto(ProductCategory productCategory);
}
