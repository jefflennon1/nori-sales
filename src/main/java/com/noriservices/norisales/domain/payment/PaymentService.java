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
import com.noriservices.norisales.domain.user.UserModel;
import com.noriservices.norisales.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
