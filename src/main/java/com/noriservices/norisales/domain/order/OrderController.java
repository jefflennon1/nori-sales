package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    private ResponseEntity<Page<OrderResponseDTO>> findAll(@PageableDefault(size = 10, sort = "id")Pageable pageable){
       return  ResponseEntity.ok().body(service.finAllPageable(pageable));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderResponseDTO>> getByUser(@PageableDefault(size = 10, sort = "id")Pageable pageable){
       return ResponseEntity.ok().body(service.findByUser(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getByUser(@PathVariable UUID id){
        return ResponseEntity.ok().body(service.findDTOById(id));
    }

    @PostMapping
    private ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO order){
       OrderResponseDTO response = service.create(order);
        return ResponseEntity.ok().body(response);
    }

}
