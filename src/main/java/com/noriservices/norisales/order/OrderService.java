package com.noriservices.norisales.order;

import com.noriservices.norisales.order.dto.OrderItemRequestDTO;
import com.noriservices.norisales.order.dto.OrderRequestDTO;
import com.noriservices.norisales.order.dto.OrderResponseDTO;
import com.noriservices.norisales.product.Product;
import com.noriservices.norisales.product.ProductService;
import com.noriservices.norisales.shared.exception.ForbiddenOperationException;
import com.noriservices.norisales.shared.exception.ResourceNotFoundException;
import com.noriservices.norisales.user.User;
import com.noriservices.norisales.user.UserRole;
import com.noriservices.norisales.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserService userService;

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
        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemRequestDTO item: order.items()){
            Product product = productService.findEntityById(item.productId());
            if(!product.isActive()) throw new RuntimeException(" Product is inactive: "+ item.productId());
            if(product.getAvailableQuantity() < item.quantity() ) throw new RuntimeException("The available quantity  of the product is less than the requested amount: "+ item.productId());

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.quantity()));
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(item.quantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubtotal(subtotal);
            orderItem.setProduct(product);

            orderItems.add(orderItem);

        }

       BigDecimal totalPrice = orderItems.stream()
               .map(OrderItem::getSubtotal)
               .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order newOrder = new Order();
        newOrder.setItems(orderItems);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setUser(user);

        orderItems.forEach(item -> item.setOrder(newOrder));

        return orderMapper.toResponse(repository.save(newOrder));
    }

    public Order findById(UUID orderId) {
       return repository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not Found: "+ orderId));
    }
    public OrderResponseDTO findDTOById(UUID orderId) {
        User user = userService.extractLoggedUser();
        Order entity = findById(orderId);
        assert user != null;
        if(!user.getRole().equals(UserRole.ADMIN) && !entity.getUser().getId().equals(user.getId())) throw new ForbiddenOperationException("Action not permitted for the logged-in user.");

        return orderMapper.toResponse(entity);
    }
    @Transactional
    public void confirmPayment(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
        repository.save(order);
    }
}
