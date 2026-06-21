package com.noriservices.norisales.infra.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockListenerService {

    @KafkaListener(topics = "inventory-updated", groupId = "nori-stock")
    public void updateProductAvailableQuantity(String message){

    }

}
