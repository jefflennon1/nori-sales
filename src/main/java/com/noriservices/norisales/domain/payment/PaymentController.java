package com.noriservices.norisales.domain.payment;

import com.noriservices.norisales.domain.payment.DTO.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<?> sendPayment(@PathVariable UUID orderId){
       PaymentResponseDTO response = paymentService.generatePaymentByPix(orderId);
        return ResponseEntity.ok().body(response);
    }
}
