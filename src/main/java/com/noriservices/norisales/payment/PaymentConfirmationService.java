package com.noriservices.norisales.payment;

import com.noriservices.norisales.order.Order;
import com.noriservices.norisales.order.OrderStatus;
import com.noriservices.norisales.order.event.DTO.OrderConfirmedEventDTO;
import com.noriservices.norisales.order.event.DTO.OrderItemEventDTO;
import com.noriservices.norisales.order.event.OrderEventProducer;
import com.noriservices.norisales.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService {

    private final PaymentRepository paymentRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public void confirmApprovedPayment(String externalPaymentId) {
        Payment payment = paymentRepository
                .findByExternalIdForUpdate(externalPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found: " + externalPaymentId
                ));

        if (payment.getStatus() == PaymentStatus.APPROVED) {
            return;
        }

        LocalDateTime paidAt = LocalDateTime.now();

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setPaidAt(paidAt);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAYMENT_CONFIRMED);

        OrderConfirmedEventDTO event = new OrderConfirmedEventDTO(
                order.getId(),
                order.getUser().getId(),
                order.getItems()
                        .stream()
                        .map(item -> new OrderItemEventDTO(
                                item.getProduct().getId(),
                                item.getQuantity()
                        ))
                        .toList(),
                paidAt
        );

        orderEventProducer.publishOrderConfirmed(event);
    }
}
