package com.EComMicroService.ProductsServices.Kafka;

import org.springframework.kafka.annotation.KafkaListener;

import com.EComMicroService.ProductsServices.Enums.EventType;
import com.EComMicroService.ProductsServices.Services.KafkaHelperService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductEventConsumer {

    private final KafkaHelperService kafkaHelperService;
    private final ProductEventProducer producer;

    private ProductEventConsumer(KafkaHelperService kafkaHelperService,ProductEventProducer producer) {
        this.kafkaHelperService = kafkaHelperService;
        this.producer = producer;

    }

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "order-events", groupId = "ProductServiceGroup")
    public void listen(String message) {
        try {
            Events orderEvent = mapper.readValue(message, Events.class);

            switch (orderEvent.getEventType().toString()) {
                case "ORDER_CREATED":
                    if (kafkaHelperService.reserveProducts(orderEvent)) {
                        orderEvent.setEventType(EventType.STOCK_RESERVED);
                        producer.sendProductEvents(orderEvent);
                    } else {
                        orderEvent.setEventType(EventType.STOCK_RESERVATION_FAILED);
                        producer.sendProductEvents(orderEvent);
                    }
                    break;
                case "ORDER_CANCELLED":
                    if (kafkaHelperService.releaseProducts(orderEvent)) {
                        // create success event

                    } else {
                        // create failure event
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
