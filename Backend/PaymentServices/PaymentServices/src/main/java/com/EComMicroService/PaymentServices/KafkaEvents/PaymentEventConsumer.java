package com.EComMicroService.PaymentServices.KafkaEvents;

import org.springframework.kafka.annotation.KafkaListener;
import com.EComMicroService.PaymentServices.Enums.EventType;
import com.EComMicroService.PaymentServices.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentEventConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final PaymentService paymentService;
    private final PaymentEventProducer producer;

    public PaymentEventConsumer(PaymentService paymentService, PaymentEventProducer producer) {
        this.paymentService = paymentService;
        this.producer = producer;
    }

    @KafkaListener(topics = "product-events", groupId = "PaymentServiceGroup")
    public void listen(String message) {
        try {
            Events Event = mapper.readValue(message, Events.class);

            switch (Event.getEventType().toString()) {
                case "STOCK_RESERVED":
                    boolean payment = paymentService.startPayent(Event.getOrderId(), Event.getTotal(), "jldf");
                    if (payment) {
                        Event.setEventType(EventType.PAYMENT_SUCCESS);
                        producer.sendPaymentEvent(Event);
                    } else {
                        Event.setEventType(EventType.PAYMENT_FAILED);
                        producer.sendPaymentEvent(Event);
                    }

                    break;
                default:
                    System.out.println("Unknown eventType: " + Event.getEventType());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
