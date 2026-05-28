package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderRequestDTO;
import com.noriservices.norisales.domain.order.DTO.OrderResponseDTO;
import com.noriservices.norisales.domain.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(source = "user",  target = "user")
    OrderResponseDTO toResponse(OrderModel entity);

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "createdAt",  ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "items",      ignore = true)
    @Mapping(target = "status",     ignore = true)
    @Mapping(target = "user",       ignore = true)
    OrderModel toEntity(OrderRequestDTO request);
}
