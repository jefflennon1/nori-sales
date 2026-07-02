package com.noriservices.norisales.category;


import com.noriservices.norisales.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.category.DTO.CategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toResponse(Category category);

    CategoryResponseDTO toResponseDTO(CategoryRequestDTO category);

    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "active",    ignore = true)
    Category toEntity(CategoryRequestDTO request);

}
