package com.EComMicroService.OrdersServices.KafkaEvents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    public OrderConsumer() {
    }

    @KafkaListener(topics = "order-events", groupId = "demo-group")
    public void listen(String message) {
        try {
            Events orderEvent = mapper.readValue(message, Events.class);
            System.out.println(
                    "Received OrderEvent: " + orderEvent.getEventType() + " for Order ID: " + orderEvent.getOrderId());
        } catch (Exception e) {
            System.err.println("Failed to parse OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
