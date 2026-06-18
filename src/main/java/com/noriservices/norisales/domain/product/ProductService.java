package com.noriservices.norisales.domain.product;

import com.noriservices.norisales.domain.category.CategoryModel;
import com.noriservices.norisales.domain.category.CategoryService;
import com.noriservices.norisales.domain.product.DTO.ProductRequestDTO;
import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;
import jakarta.validation.Valid;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private CategoryService categoryService;

    public ProductResponseDTO findById(UUID id){
        Optional<ProductModel> entity = repository.findById(id);
        return entity.map(productModel -> mapper.toResponse(productModel)).orElse(null);
    }
    public ProductModel findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public boolean isProductExists(UUID id , String name, BigDecimal price) {
       if(id != null){
        return  repository.findById(id).isPresent();
       }
       return repository.findByNameAndPrice(name, price).isPresent();
    }

    public ProductResponseDTO save(@Valid ProductRequestDTO newProductDTO) {
        ProductModel entity = mapper.toEntity(newProductDTO);
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setCategory(categoryService.findEntityByName(newProductDTO.category().name()));
        ProductModel created = repository.save(entity);

        return mapper.toResponse(created);
    }

    public Page<ProductResponseDTO> findAllPageable(Pageable pageable) {
      Page<ProductModel> products = repository.findAll(pageable);
      return products.map(item-> mapper.toResponse(item));
    }

    public ProductResponseDTO update(UUID id, @Valid ProductRequestDTO dto) {
        ProductModel entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setActive(dto.active());
        entity.setPrice(dto.price());
        entity.setAvailableQuantity(dto.availableQuantity());

        if(dto.category() != null){
            CategoryModel category = categoryService.findEntityById(dto.category().id());
            entity.setCategory(category);
        }
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
      Optional<ProductModel> entity =  repository.findById(id);
        repository.delete(entity.orElseThrow());
    }
}
