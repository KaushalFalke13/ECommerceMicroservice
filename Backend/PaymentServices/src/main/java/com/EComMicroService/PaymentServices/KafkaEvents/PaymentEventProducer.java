package com.ecommicroservice.paymentservices.kafkaevents;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final String topic = "order-events";

    public void sendPaymentEvent(Events event) {
        try {
            String msg = mapper.writeValueAsString(event);
            log.info("Sending payment event to topic {}: {}", topic, msg);
            kafkaTemplate.send(topic, event.getOrderId(), msg);
        } catch (Exception e) {
            log.error("Failed to serialize payment event", e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
