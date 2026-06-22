package com.noriservices.norisales.domain.category;


import com.noriservices.norisales.domain.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toResponse(CategoryModel category);

    CategoryResponseDTO toResponseDTO(CategoryRequestDTO category);

    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "active",    ignore = true)
    CategoryModel toEntity(CategoryRequestDTO request);

}
