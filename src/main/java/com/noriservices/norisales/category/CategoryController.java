package com.noriservices.norisales.category;

import com.noriservices.norisales.category.dto.CategoryRequestDTO;
import com.noriservices.norisales.category.dto.CategoryResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> getAll(@PageableDefault(size = 10, sort = "name")Pageable pageable){
       return ResponseEntity.ok().body(service.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id){
       CategoryResponseDTO response = service.findById(id);
       if(response == null) return ResponseEntity.notFound().build();

       return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> save(@RequestBody @Valid CategoryRequestDTO dto){
        CategoryResponseDTO categoryResponseDTO = service.findByName(dto.name());
        if(categoryResponseDTO != null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        CategoryResponseDTO response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<CategoryResponseDTO> update(@RequestBody @Valid CategoryRequestDTO dto){
        CategoryResponseDTO categoryResponseDTO = service.findById(dto.id());
        if(categoryResponseDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        CategoryResponseDTO response = service.update(dto.id(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete={id}")
    public ResponseEntity<CategoryResponseDTO> delete(@PathVariable UUID id){
       CategoryResponseDTO dto = service.findById(id);
       if(dto != null) return ResponseEntity.notFound().build();
       service.delete(id);
       return ResponseEntity.ok().build();
    }
}
