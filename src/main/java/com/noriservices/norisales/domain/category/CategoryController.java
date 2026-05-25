package com.noriservices.norisales.domain.category;

import com.noriservices.norisales.domain.category.DTO.CategoryRequestDTO;
import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/all")
    public ResponseEntity getAll(){
       List<CategoryResponseDTO> list =  service.getAll();
       return ResponseEntity.ok().body(list);
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id){
       CategoryResponseDTO response = service.findById(id);
       if(response == null) return ResponseEntity.notFound().build();

       return ResponseEntity.ok().body(response);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody @Valid CategoryRequestDTO dto){
        CategoryResponseDTO categoryResponseDTO = service.findByName(dto.name());
        if(categoryResponseDTO != null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        CategoryResponseDTO response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete={id}")
    public ResponseEntity delete(@PathVariable UUID id){
       CategoryResponseDTO dto = service.findById(id);
       if(dto != null) return ResponseEntity.notFound().build();
       service.delete(id);
       return ResponseEntity.ok().build();
    }
}
