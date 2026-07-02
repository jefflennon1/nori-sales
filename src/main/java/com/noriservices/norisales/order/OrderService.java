package com.noriservices.norisales.order;

import com.noriservices.norisales.order.DTO.OrderItemRequestDTO;
import com.noriservices.norisales.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.order.DTO.OrderResponseDTO;
import com.noriservices.norisales.product.Product;
import com.noriservices.norisales.product.ProductService;
import com.noriservices.norisales.user.User;
import com.noriservices.norisales.user.UserService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        User user = userService.extractLoggedUser();
        return repository.findByUserId(user.getId(), pageable)
                .map(orderMapper::toResponse);
    }
    public Page<OrderResponseDTO> finAllPageable(Pageable pageable) {
        return repository.findAll(pageable).map(orderMapper::toResponse);
    }

    public OrderResponseDTO create(OrderRequestDTO order) {
        User user = userService.extractLoggedUser();
        List<OrderItemModel> orderItems = new ArrayList<>();
        for(OrderItemRequestDTO item: order.items()){
            Product product = productService.findEntityById(item.productId());
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

        Order Order = new Order();
        Order.setItems(orderItems);
        Order.setTotalPrice(totalPrice);
        Order.setUser(user);

        orderItems.forEach(item -> item.setOrder(Order));

        return orderMapper.toResponse(repository.save(Order));
    }

    public Order findById(UUID orderId) {
       return repository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not Found: "+ orderId));
    }
    public OrderResponseDTO findDTOById(UUID orderId) {
        return orderMapper.toResponse(findById(orderId));
    }
    @Transactional
    public void confirmPayment(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
        repository.save(order);
    }
}
