package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderResponseDTO> findByUserId(UUID id){
       return repository.findByUserId(id)
               .stream()
               .map(orderMapper::toResponse)
               .toList();
    }
}
