package com.noriservices.norisales.infra.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SaleEventProducerService {

    private static final String TOPIC = "order-confirmed";

    @Autowired
    private KafkaTemplate<String, OrderConfirmedEventDTO> kafkaTemplate;

    public void publishOrderConfirmed(OrderConfirmedEventDTO event) {
        kafkaTemplate.send(TOPIC, event.orderId().toString(), event);

    }
}