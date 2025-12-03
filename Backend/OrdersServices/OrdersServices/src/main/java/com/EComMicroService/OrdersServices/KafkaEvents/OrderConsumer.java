package com.EComMicroService.OrdersServices.KafkaEvents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    public OrderConsumer() {
    }

    @KafkaListener(topics = "product-events", groupId = "OrderServiceGroup")
    public void listen(String message) {
        try {
            Events orderEvent = mapper.readValue(message, Events.class);

            switch (orderEvent.getEventType().toString()) {
                case "STOCK_RESERVED":

                    break;
                case "STOCK_RESERVATION_FAILED":
                    if (true) {
                        // create success event

                    }
                    break;
                default:
                    System.out.println("Unknown eventType: " + orderEvent.getEventType());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
