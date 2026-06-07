package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderItemRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;
import com.noriservices.norisales.domain.product.ProductModel;
import com.noriservices.norisales.domain.product.ProductService;
import com.noriservices.norisales.domain.user.UserModel;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    public List<OrderResponseDTO> findByUser(){
        UserModel user = extractLoggedUser();

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

    public void create(OrderRequestDTO order) {
        UserModel user = extractLoggedUser();
        List<ProductModel> products = new ArrayList<>();
        for(OrderItemRequestDTO item: order.items()){
            ProductModel product = productService.findEntityById(item.productId());
            if(!product.isActive()) throw new RuntimeException(" Product is inactive: "+ item.productId());
            if(product.getAvailableQuantity() < item.quantity() ) throw new RuntimeException("The available quantity  of the product is less than the requested amount: "+ item.productId());
            products.add(product);
        }
    }

    private static @Nullable UserModel extractLoggedUser() {
        return (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
