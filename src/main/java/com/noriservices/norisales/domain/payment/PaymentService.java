package com.noriservices.norisales.domain.payment;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.noriservices.norisales.domain.order.OrderModel;
import com.noriservices.norisales.domain.order.OrderService;
import com.noriservices.norisales.domain.order.OrderStatus;
import com.noriservices.norisales.domain.payment.DTO.PaymentResponseDTO;
import com.noriservices.norisales.domain.payment.DTO.WebhookDTO;
import com.noriservices.norisales.domain.user.UserModel;
import com.noriservices.norisales.domain.user.UserService;
import com.noriservices.norisales.infra.kafka.OrderConfirmedEvent;
import com.noriservices.norisales.infra.kafka.OrderItemEventDTO;
import com.noriservices.norisales.infra.kafka.SaleEventProducerService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private PaymentMapper mapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private SaleEventProducerService saleEventProducer;

    public PaymentResponseDTO generatePaymentByPix(UUID orderId) throws MPException, MPApiException {
        try {
              OrderModel order = orderService.findById(orderId);
              UserModel user = userService.extractLoggedUser();
              if(!order.getUser().getId().equals(user.getId())) throw new RuntimeException("Order does not belong to logged-in user!");
              if(!order.getStatus().equals(OrderStatus.PENDING_PAYMENT)) throw new RuntimeException("Payment has not been generate, please check your status order!");
              Optional<PaymentModel> existingPayment = repository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING);

                if (existingPayment.isPresent()) {
                    return mapper.toResponse(existingPayment.get()); // returns the existing payment (if exists)
                }
                PaymentClient client = new PaymentClient();
                PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                        .transactionAmount(order.getTotalPrice())
                        .description("Nori Sales - Order " + order.getId())
                        .paymentMethodId("pix")
                        .payer(
                                PaymentPayerRequest.builder()
                                        .email(user.getEmail())
                                        .build()
                        )
                        .build();
                Payment mpPayment = client.create(paymentRequest);
                PaymentModel payment = new PaymentModel();
                payment.setOrder(order);
                payment.setAmount(order.getTotalPrice());
                payment.setExternalId(String.valueOf(mpPayment.getId()));
                payment.setPixQrCode(
                        mpPayment.getPointOfInteraction()
                                .getTransactionData()
                                .getQrCode()
                );
                payment.setPixQrCodeB64(
                        mpPayment.getPointOfInteraction()
                                .getTransactionData()
                                .getQrCodeBase64()
                );


                return mapper.toResponse(repository.save(payment));
        } catch (MPApiException e) {
            throw new RuntimeException("Mercado Pago API error: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException("Mercado Pago error: " + e.getMessage());
        }
    }

    @Transactional
    public void processPaymentWebhook(WebhookDTO webhookDTO) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment mpPayment = client.get(Long.parseLong(webhookDTO.data().id()));
        if(mpPayment.getStatus().equals("approved")){
            PaymentModel payment = repository.findByExternalId(webhookDTO.data().id()).orElseThrow(() -> new ResourceNotFoundException("Payment not found: "+ webhookDTO.data().id()));
            payment.setStatus(PaymentStatus.APPROVED);
            payment.setPaidAt(LocalDateTime.now());
            repository.save(payment);
            orderService.confirmPayment(payment.getOrder().getId());

            OrderModel order = payment.getOrder();
            OrderConfirmedEvent event = new OrderConfirmedEvent(
                    order.getId(),
                    order.getUser().getId(),
                    order.getItems().stream()
                            .map(item -> new OrderItemEventDTO(
                                    item.getProduct().getId(),
                                    item.getQuantity()
                            ))
                            .toList(),
                    LocalDateTime.now()
            );

            saleEventProducer.publishOrderConfirmed(event);
        }
    }

}
