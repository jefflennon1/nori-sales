package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import com.noriservices.norisales.domain.user.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderResponseDTO> findByUser(){
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return repository.findByUserId(user.getId())
               .stream()
               .map(orderMapper::toResponse)
               .toList();
    }

    public List<OrderResponseDTO> finAll() {
        return repository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
