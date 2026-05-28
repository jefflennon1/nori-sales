package com.noriservices.norisales.domain.order;

import com.noriservices.norisales.domain.order.DTO.OrderItemDTO;
import com.noriservices.norisales.domain.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderItemMapper {

    @Mapping(source = "product", target = "product")
    OrderItemDTO toDTO(OrderItemModel entity);
}
