package com.noriservices.norisales.order;

import com.noriservices.norisales.order.DTO.OrderItemDTO;
import com.noriservices.norisales.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderItemMapper {

    @Mapping(source = "product", target = "product")
    OrderItemDTO toDTO(OrderItemModel entity);
}
