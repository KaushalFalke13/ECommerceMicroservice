package com.ecommicroservice.paymentservices.kafkaevents;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommicroservice.paymentservices.enums.EventType;
import com.ecommicroservice.paymentservices.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ObjectMapper mapper;
    private final PaymentService paymentService;
    private final PaymentEventProducer producer;

    @KafkaListener(topics = "payment-events", groupId = "PaymentServiceGroup")
    public void listen(String message) {
        try {
            log.info("Received payment event: {}", message);
            Events event = mapper.readValue(message, Events.class);

            switch (event.getEventType()) {
                case STOCK_RESERVED:
                    log.info("Processing STOCK_RESERVED for orderId: {}", event.getOrderId());
                    boolean paymentSuccessful = paymentService.startPayent(
                            event.getOrderId(),
                            event.getTotal(),
                            event.getPaymentMode() != null ? event.getPaymentMode() : "CREDIT_CARD");

                    if (paymentSuccessful) {
                        event.setEventType(EventType.PAYMENT_SUCCESS);
                        log.info("Payment successful for orderId: {}", event.getOrderId());
                    } else {
                        event.setEventType(EventType.PAYMENT_FAILED);
                        log.warn("Payment failed for orderId: {}", event.getOrderId());
                    }
                    producer.sendPaymentEvent(event);
                    break;

                case ORDER_CANCELLED:
                    log.info("Processing ORDER_CANCELLED for orderId: {}", event.getOrderId());
                    // Handle order cancellation - release any pending payments
                    // In real implementation, you might want to refund the payment
                    break;

                default:
                    log.warn("Unknown eventType: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Failed to process payment event: {}", message, e);
        }
    }
}
