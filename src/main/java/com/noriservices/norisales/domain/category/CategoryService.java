package com.noriservices.norisales.domain.category;

import com.noriservices.norisales.domain.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return entity.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + name));
    }
    public CategoryModel findEntityById(UUID id){
        Optional<CategoryModel> entity = repository.findById(id);
        return entity.orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    public CategoryResponseDTO parseToResponse(CategoryRequestDTO dto){
        return mapper.toResponseDTO(dto);
    }

    public Page<CategoryResponseDTO> getAllPageable(Pageable pageable) {
        return repository.findAll(pageable)
               .map(mapper::toResponse);
    }

    public CategoryResponseDTO findById(UUID id) {
       Optional<CategoryModel> entity = repository.findById(id);
        return entity.map(categoryModel -> mapper.toResponse(categoryModel)).orElse(null);
    }

    public CategoryResponseDTO save(@Valid CategoryRequestDTO dto) {
      CategoryModel entity = repository.save(mapper.toEntity(dto));
      return mapper.toResponse(entity);
    }
    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {
        CategoryModel entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setActive(dto.active());

        return mapper.toResponse(repository.save(entity));
    }


    public void delete(UUID id) {
       Optional<CategoryModel> entity = repository.findById(id);
       repository.delete(entity.orElseThrow());
    }
}
