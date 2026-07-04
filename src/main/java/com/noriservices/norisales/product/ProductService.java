package com.noriservices.norisales.product;

import com.noriservices.norisales.category.Category;
import com.noriservices.norisales.category.CategoryService;
import com.noriservices.norisales.product.dto.ProductRequestDTO;
import com.noriservices.norisales.product.dto.ProductResponseDTO;
import com.noriservices.norisales.shared.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryService categoryService;

    public ProductResponseDTO findById(UUID id){
        Optional<Product> entity = repository.findById(id);
        return entity.map(Product -> mapper.toResponse(Product)).orElse(null);
    }
    public Product findEntityById(UUID id) {
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
        Product entity = mapper.toEntity(newProductDTO);
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setCategory(categoryService.findEntityByName(newProductDTO.category().name()));
        Product created = repository.save(entity);

        return mapper.toResponse(created);
    }

    public void saveEntity(Product entity){
        repository.save(entity);
    }


    public Page<ProductResponseDTO> findAllPageable(Pageable pageable) {
      Page<Product> products = repository.findAll(pageable);
      return products.map(item-> mapper.toResponse(item));
    }

    public ProductResponseDTO update(UUID id, @Valid ProductRequestDTO dto) {
        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setActive(dto.active());
        entity.setPrice(dto.price());
        entity.setAvailableQuantity(dto.availableQuantity());

        if(dto.category() != null){
            Category category = categoryService.findEntityById(dto.category().id());
            entity.setCategory(category);
        }
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
      Optional<Product> entity =  repository.findById(id);
        repository.delete(entity.orElseThrow());
    }
}
