package com.ecommicroservice.orderservices.kafkaevents;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ecommicroservice.orderservices.entity.OrdersEventsLog;
import com.ecommicroservice.orderservices.enums.EventStatus;
import com.ecommicroservice.orderservices.services.OrderEventService;

@Component
public class KafkaHelper {

    private final OrderEventService orderEventService;
    private final OrderProducer producer;

    public KafkaHelper(OrderEventService orderEventService, OrderProducer producer) {
        this.orderEventService = orderEventService;
        this.producer = producer;
    }

    @Scheduled(fixedRate = 100000)
    public void sendUnPublishedEvents() {
        try {
            List<OrdersEventsLog> unpublishedEvents = orderEventService.getUnpublishedEvents();
            unpublishedEvents.forEach(event -> sendEvent(event));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void markAsPublished(OrdersEventsLog event) {
        event.setPublished(EventStatus.SENT);
        orderEventService.UpdateOrderEventsLog(event);
    }

    private void sendEvent(OrdersEventsLog event) {
        try {
            producer.sendOrderEvent(event.getEvent());
            System.out.println("Order event Send for order id:" + event.getOrderId());
            markAsPublished(event);
        } catch (Exception e) {

        }
    }

}
