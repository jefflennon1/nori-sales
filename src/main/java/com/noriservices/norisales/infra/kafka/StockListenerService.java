package com.noriservices.norisales.infra.kafka;

import com.noriservices.norisales.domain.product.ProductModel;
import com.noriservices.norisales.domain.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockListenerService {

    @Autowired
    private ProductService productService;

    @Transactional
    @KafkaListener(topics = "inventory-updated")
    public void updateProductAvailableQuantity(OrderItemEventDTO event){
       ProductModel entity = productService.findEntityById(event.productId());
       entity.setAvailableQuantity(event.quantity());
        productService.saveEntity(entity);
    }

}
