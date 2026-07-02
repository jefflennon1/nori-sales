package com.noriservices.norisales.product.event;

import com.noriservices.norisales.order.event.DTO.OrderItemEventDTO;
import com.noriservices.norisales.product.Product;
import com.noriservices.norisales.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockUpdatedConsumer {

    @Autowired
    private ProductService productService;

    @Transactional
    @KafkaListener(topics = "inventory-updated")
    public void updateProductAvailableQuantity(OrderItemEventDTO event){
       Product entity = productService.findEntityById(event.productId());
       entity.setAvailableQuantity(event.quantity());
        productService.saveEntity(entity);
    }

}
