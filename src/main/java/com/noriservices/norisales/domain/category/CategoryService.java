package com.noriservices.norisales.domain.category;

import com.noriservices.norisales.domain.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        return entity.orElse(null);
    }

    public CategoryResponseDTO parseToResponse(CategoryRequestDTO dto){
        return mapper.toResponseDTO(dto);
    }

    public List<CategoryResponseDTO> getAll() {
        List<CategoryModel>  list = repository.findAll();
        List<CategoryResponseDTO> response = new ArrayList<>();

        for(CategoryModel model : list){
            CategoryResponseDTO dto =  mapper.toResponse(model);
            response.add(dto);
        }

        return response;
    }

    public CategoryResponseDTO findById(UUID id) {
       Optional<CategoryModel> entity = repository.findById(id);
        return entity.map(categoryModel -> mapper.toResponse(categoryModel)).orElse(null);
    }

    public CategoryResponseDTO save(@Valid CategoryRequestDTO dto) {
      CategoryModel entity = repository.save(mapper.toEntity(dto));
      return mapper.toResponse(entity);
    }

    public void delete(UUID id) {
       Optional<CategoryModel> entity = repository.findById(id);
       repository.delete(entity.orElseThrow());
    }
}
