package com.noriservices.norisales.domain.product;

import com.noriservices.norisales.domain.product.DTO.ProductRequestDTO;
import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable @Valid UUID id){
        ProductResponseDTO product = service.findById(id);
        if(product == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO newProductDTO){
       boolean existsProduct = service.isProductExists(newProductDTO.id(), newProductDTO.name(), newProductDTO.price());
       if(existsProduct) return ResponseEntity.status(HttpStatus.CONFLICT).build();
       ProductResponseDTO created =  service.save(newProductDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductResponseDTO> update(@RequestBody @Valid ProductRequestDTO newProductDTO){
        boolean existsProduct = service.isProductExists(newProductDTO.id(), newProductDTO.name(), newProductDTO.price());
        if(!existsProduct) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ProductResponseDTO updated =  service.update(newProductDTO.id(), newProductDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/delete={id}")
    public ResponseEntity<ProductResponseDTO> exclude(@PathVariable UUID id){
        ProductResponseDTO dto = service.findById(id);
        if(dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
