package com.noriservices.norisales.domain.payment;

import com.noriservices.norisales.domain.order.OrderMapper;
import com.noriservices.norisales.domain.payment.DTO.PaymentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    PaymentResponseDTO toResponse(PaymentModel payment);
}
