package com.EComMicroService.PaymentServices.KafkaEvents;

import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentEventProducer {
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String topic = "order-events";

    public PaymentEventProducer(KafkaTemplate<String, String> kafka) {
        this.kafka = kafka;
    }

    public void sendPaymentEvent(Events event) {
        try {
            String msg = mapper.writeValueAsString(event);
            kafka.send(topic, event.getOrderId(), msg);

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
