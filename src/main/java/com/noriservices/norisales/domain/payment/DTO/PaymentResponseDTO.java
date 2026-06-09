package com.noriservices.norisales.domain.payment.DTO;


import com.noriservices.norisales.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponseDTO(UUID paymentId,
                                 BigDecimal amount,
                                 String pixQrCode,
                                 String pixQrCodeB64,
                                 PaymentStatus status) {
}
