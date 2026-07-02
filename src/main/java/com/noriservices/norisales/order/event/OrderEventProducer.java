package com.noriservices.norisales.order.event;

import com.noriservices.norisales.order.event.DTO.OrderConfirmedEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final String TOPIC = "order-confirmed";

    @Autowired
    private KafkaTemplate<String, OrderConfirmedEventDTO> kafkaTemplate;

    public void publishOrderConfirmed(OrderConfirmedEventDTO event) {
        kafkaTemplate.send(TOPIC, event.orderId().toString(), event);
    }
}