package com.EComMicroService.ProductsServices.Kafka;

import org.springframework.kafka.annotation.KafkaListener;

import com.EComMicroService.ProductsServices.Services.KafkaHelperService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductEventConsumer {

    private final KafkaHelperService kafkaHelperService;

    private ProductEventConsumer(KafkaHelperService kafkaHelperService) {
        this.kafkaHelperService = kafkaHelperService;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "order-events", groupId = "ProductServiceGroup")
    public void listen(String message) {
        try {
            Events orderEvent = mapper.readValue(message, Events.class);

            switch (orderEvent.getEventType().toString()) {
                case "ORDER_CREATED":
                    kafkaHelperService.reserveProducts(orderEvent);
                    break;
                case "ORDER_CANCELLED":
                    System.out.println("cancelled");
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
