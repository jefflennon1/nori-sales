package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/all")
    private ResponseEntity<List<OrderResponseDTO>> findAll(){
       return  ResponseEntity.ok().body(service.finAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> findByByUser(@PathVariable("userId") UUID id){
       return ResponseEntity.ok().body(service.findByUserId(id));
    }


}
