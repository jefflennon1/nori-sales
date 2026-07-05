package com.noriservices.norisales.payment;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.noriservices.norisales.order.Order;
import com.noriservices.norisales.order.OrderService;
import com.noriservices.norisales.order.OrderStatus;
import com.noriservices.norisales.payment.dto.PaymentResponseDTO;
import com.noriservices.norisales.payment.dto.WebhookDTO;
import com.noriservices.norisales.shared.exception.ForbiddenOperationException;
import com.noriservices.norisales.shared.exception.PaymentProviderException;
import com.noriservices.norisales.shared.exception.PendingPaymentException;
import com.noriservices.norisales.shared.exception.ResourceNotFoundException;
import com.noriservices.norisales.user.User;
import com.noriservices.norisales.user.UserService;
import com.noriservices.norisales.order.event.DTO.OrderConfirmedEventDTO;
import com.noriservices.norisales.order.event.DTO.OrderItemEventDTO;
import com.noriservices.norisales.order.event.OrderEventProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderEventProducer saleEventProducer;

    public PaymentResponseDTO generatePaymentByPix(UUID orderId){
        try {
              Order order = orderService.findById(orderId);
              User user = userService.extractLoggedUser();
              if(!order.getUser().getId().equals(user.getId())) throw new ForbiddenOperationException("Order does not belong to logged-in user!");
              if(!order.getStatus().equals(OrderStatus.PENDING_PAYMENT)) throw new PendingPaymentException("Payment has not been generate, please check your status order!");
              Optional<Payment> existingPayment = repository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING);

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
            com.mercadopago.resources.payment.Payment mpPayment = client.create(paymentRequest);
                Payment payment = new Payment();
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
            throw new PaymentProviderException("Payment cannot be generated for the current order status. ", e);
        } catch (MPException e) {
            throw new PaymentProviderException("Payment cannot be generated for the current order status. " , e);
        }
    }

    @Transactional
    public void processPaymentWebhook(WebhookDTO webhookDTO) {
      try {
          PaymentClient client = new PaymentClient();
          com.mercadopago.resources.payment.Payment mpPayment = client.get(Long.parseLong(webhookDTO.data().id()));
          if(mpPayment.getStatus().equals("approved")){
              Payment payment = repository.findByExternalId(webhookDTO.data().id()).orElseThrow(() -> new ResourceNotFoundException("Payment not found: "+ webhookDTO.data().id()));
              payment.setStatus(PaymentStatus.APPROVED);
              payment.setPaidAt(LocalDateTime.now());
              repository.save(payment);
              orderService.confirmPayment(payment.getOrder().getId());

              Order order = orderService.findById(payment.getOrder().getId());
              OrderConfirmedEventDTO event = new OrderConfirmedEventDTO(
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
      } catch (MPException | MPApiException ex){
          throw new PaymentProviderException("Failed to process payment webhook. ", ex);
      }
    }
}
