package com.EComMicroService.OrdersServices.KafkaEvents;

import com.EComMicroService.OrdersServices.Enums.OrderStatus;
import com.EComMicroService.OrdersServices.Services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "product-events", groupId = "OrderServiceGroup")
    public void ProductListener(String message) {
        try {
            Events Event = mapper.readValue(message, Events.class);

            switch (Event.getEventType().toString()) {
                case "STOCK_RESERVATION_FAILED":
                    orderService.deleteOrder(Event.getOrderId());
                    break;
                default:
                    // System.out.println("Unknown eventType: " + Event.getEventType());
            }
        } catch (Exception e) {
            // System.err.println("Failed to parse OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

   @KafkaListener(topics = "payment-events", groupId = "OrderServiceGroup")
    public void PaymentListener(String message) {
        try {
            Events Event = mapper.readValue(message, Events.class);

            switch (Event.getEventType().toString()) {
                case "PAYMENT_SUCCESS":
                        orderService.updateOrderStatus(Event.getOrderId(), OrderStatus.PAIDED);
                    break;
                default:
                    // System.out.println("Unknown eventType: " + Event.getEventType());
            }
        } catch (Exception e) {
            // System.err.println("Failed to parse OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
