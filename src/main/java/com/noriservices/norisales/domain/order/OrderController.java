package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/all")
    private ResponseEntity<List<OrderResponseDTO>> findAll(){
       return  ResponseEntity.ok().body(service.finAll());
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getByUser(){
       return ResponseEntity.ok().body(service.findByUser());
    }

    @PostMapping("/create")
    private ResponseEntity<?> create(OrderRequestDTO order){
        service.create(order);
        return ResponseEntity.ok().build();
    }
}
