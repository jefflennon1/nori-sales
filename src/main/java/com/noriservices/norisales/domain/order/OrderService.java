package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OderStatusDTO;
import com.noriservices.norisales.domain.order.DTO.OrderItemRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;
import com.noriservices.norisales.domain.product.ProductModel;
import com.noriservices.norisales.domain.product.ProductService;
import com.noriservices.norisales.domain.user.UserModel;
import com.noriservices.norisales.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    public Page<OrderResponseDTO> findByUser(Pageable pageable){
        UserModel user = userService.extractLoggedUser();
        return repository.findByUserId(user.getId(), pageable)
                .map(orderMapper::toResponse);
    }
    public Page<OrderResponseDTO> finAllPageable(Pageable pageable) {
        return repository.findAll(pageable).map(orderMapper::toResponse);
    }

    public OrderResponseDTO create(OrderRequestDTO order) {
        UserModel user = userService.extractLoggedUser();
        List<OrderItemModel> orderItems = new ArrayList<>();
        for(OrderItemRequestDTO item: order.items()){
            ProductModel product = productService.findEntityById(item.productId());
            if(!product.isActive()) throw new RuntimeException(" Product is inactive: "+ item.productId());
            if(product.getAvailableQuantity() < item.quantity() ) throw new RuntimeException("The available quantity  of the product is less than the requested amount: "+ item.productId());

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.quantity()));
            OrderItemModel orderItemModel = new OrderItemModel();
            orderItemModel.setQuantity(item.quantity());
            orderItemModel.setUnitPrice(unitPrice);
            orderItemModel.setSubtotal(subtotal);
            orderItemModel.setProduct(product);

            orderItems.add(orderItemModel);

        }

       BigDecimal totalPrice = orderItems.stream()
               .map(OrderItemModel::getSubtotal)
               .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderModel orderModel = new OrderModel();
        orderModel.setItems(orderItems);
        orderModel.setTotalPrice(totalPrice);
        orderModel.setUser(user);

        orderItems.forEach(item -> item.setOrder(orderModel));

        return orderMapper.toResponse(repository.save(orderModel));
    }

    public OrderModel findById(UUID orderId) {
       return repository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not Found: "+ orderId));
    }
    public OrderResponseDTO findDTOById(UUID orderId) {
        return orderMapper.toResponse(findById(orderId));
    }
    @Transactional
    public void confirmPayment(UUID orderId) {
        OrderModel order = findById(orderId);
        order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
        repository.save(order);
    }
}
