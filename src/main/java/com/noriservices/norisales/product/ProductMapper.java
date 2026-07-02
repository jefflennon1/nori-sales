package com.noriservices.norisales.product;

import com.noriservices.norisales.category.CategoryMapper;
import com.noriservices.norisales.product.DTO.ProductRequestDTO;
import com.noriservices.norisales.product.DTO.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    ProductResponseDTO toResponse(Product product);

    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "active",            ignore = true)
    @Mapping(target = "createdAt",         ignore = true)
    @Mapping(target = "updatedAt",         ignore = true)
    @Mapping(target = "category",          ignore = true)
    Product toEntity(ProductRequestDTO request);
}
