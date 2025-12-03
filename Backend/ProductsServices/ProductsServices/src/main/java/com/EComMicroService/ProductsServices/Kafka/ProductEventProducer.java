package com.EComMicroService.ProductsServices.Kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductEventProducer {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String topic = "product-events";

    public ProductEventProducer(KafkaTemplate<String, String> kafka) {
        this.kafka = kafka;
    }

    public void sendProductEvents(Events event) {
        try {
            String msg = mapper.writeValueAsString(event);
            kafka.send(topic, event.getOrderId(), msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

}
