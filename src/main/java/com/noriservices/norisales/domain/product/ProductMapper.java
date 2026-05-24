package com.noriservices.norisales.domain.product;

import com.noriservices.norisales.domain.category.CategoryMapper;
import com.noriservices.norisales.domain.product.DTO.ProductRequestDTO;
import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    ProductResponseDTO toResponse(ProductModel product);

    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "active",            ignore = true)
    @Mapping(target = "createdAt",         ignore = true)
    @Mapping(target = "updatedAt",         ignore = true)
    @Mapping(target = "category",          ignore = true)
    ProductModel toEntity(ProductRequestDTO request);
}
