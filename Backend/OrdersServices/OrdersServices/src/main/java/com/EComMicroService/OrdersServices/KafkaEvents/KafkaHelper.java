package com.EComMicroService.OrdersServices.KafkaEvents;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;
import com.EComMicroService.OrdersServices.Enums.EventStatus;
import com.EComMicroService.OrdersServices.Services.OrderEventService;

public class KafkaHelper {

    private final OrderEventService orderEventService;
    private final OrderProducer producer;

    public KafkaHelper(OrderEventService orderEventService, OrderProducer producer) {
        this.orderEventService = orderEventService;
        this.producer = producer;
    }

    @Scheduled(fixedDelay = 1000)
    private void sendUnPublishedEvents() {
        List<OrdersEventsLog> unpublishedEvents = orderEventService.getUnpublishedEvents();
        unpublishedEvents.forEach(event -> sendEvent(event));
    }

    private void markAsPublished(OrdersEventsLog event) {
        event.setPublished(EventStatus.SENT);
        orderEventService.UpdateOrderEventsLog(event);
    }

    private void sendEvent(OrdersEventsLog event) {
        try {
            producer.sendOrderEvent(event.getEvent());
            markAsPublished(event);
        } catch (Exception e) {
            
        }
    }

}
