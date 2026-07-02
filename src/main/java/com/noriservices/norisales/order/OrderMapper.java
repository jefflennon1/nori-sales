package com.noriservices.norisales.order;

import com.noriservices.norisales.order.dto.OrderRequestDTO;
import com.noriservices.norisales.order.dto.OrderResponseDTO;
import com.noriservices.norisales.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(source = "user",  target = "user")
    OrderResponseDTO toResponse(Order entity);

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "createdAt",  ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "items",      ignore = true)
    @Mapping(target = "status",     ignore = true)
    @Mapping(target = "user",       ignore = true)
    Order toEntity(OrderRequestDTO request);
}
