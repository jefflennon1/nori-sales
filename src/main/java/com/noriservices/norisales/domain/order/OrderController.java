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

    @GetMapping("/user-list={uuid}")
    public ResponseEntity findByByUser(@PathVariable("uuid") UUID id){
       List<OrderResponseDTO> ListDto =  service.findByUserId(id);
       if(ListDto == null) return ResponseEntity.notFound().build();
       return ResponseEntity.ok().body(ListDto);
    }
}
