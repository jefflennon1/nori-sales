package com.noriservices.norisales.payment;

import com.noriservices.norisales.order.OrderMapper;
import com.noriservices.norisales.payment.dto.PaymentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    PaymentResponseDTO toResponse(Payment payment);
}
