package com.noriservices.norisales.domain.payment;

import com.noriservices.norisales.domain.order.OrderModel;
import com.noriservices.norisales.domain.order.OrderService;
import com.noriservices.norisales.domain.order.OrderStatus;
import com.noriservices.norisales.domain.payment.DTO.PaymentResponseDTO;
import com.noriservices.norisales.domain.user.UserModel;
import com.noriservices.norisales.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    public PaymentResponseDTO generatePaymentByPix(UUID orderId){
      OrderModel order = orderService.findById(orderId);
      UserModel user = userService.extractLoggedUser();
      if(!order.getUser().getId().equals(user.getId())) throw new RuntimeException("Order not belongs to user logged!");
      if(!order.getStatus().equals(OrderStatus.PENDING_PAYMENT)) throw new RuntimeException("Payment not generate, verify status order!");


      return null;
    }
}
