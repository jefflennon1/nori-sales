package com.noriservices.norisales.domain.category;

import com.noriservices.norisales.domain.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryMapper mapper;

    public CategoryResponseDTO findByName(String name){
      Optional<CategoryModel> entity = repository.findByName(name);
        return entity.map(categoryModel -> mapper.toResponse(categoryModel)).orElse(null);
    }

    public CategoryModel findEntityByName(String name){
        Optional<CategoryModel> entity = repository.findByName(name);
        return entity.orElse(null);
    }

    public CategoryResponseDTO parseToResponse(CategoryRequestDTO dto){
        return mapper.toResponseDTO(dto);
    }
}
