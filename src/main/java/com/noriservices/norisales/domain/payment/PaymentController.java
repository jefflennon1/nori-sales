package com.noriservices.norisales.domain.payment;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.noriservices.norisales.domain.payment.DTO.PaymentResponseDTO;
import com.noriservices.norisales.domain.payment.DTO.WebhookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}/pix")
    public ResponseEntity<PaymentResponseDTO> generatePix(@PathVariable UUID orderId) throws MPException, MPApiException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.generatePaymentByPix(orderId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> paymentNotification(@RequestBody  WebhookDTO webhookDTO) throws MPException, MPApiException {
        paymentService.processPaymentWebhook(webhookDTO);

        return ResponseEntity.ok().build();
    }
}
