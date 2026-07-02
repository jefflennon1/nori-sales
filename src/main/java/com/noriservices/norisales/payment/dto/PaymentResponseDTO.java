package com.noriservices.norisales.payment.dto;


import com.noriservices.norisales.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponseDTO(UUID paymentId,
                                 BigDecimal amount,
                                 String pixQrCode,
                                 String pixQrCodeB64,
                                 PaymentStatus status) {
}
