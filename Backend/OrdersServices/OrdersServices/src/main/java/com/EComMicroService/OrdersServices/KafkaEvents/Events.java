package com.EComMicroService.OrdersServices.KafkaEvents;

import com.EComMicroService.OrdersServices.Enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Events {
    private String orderId;
    private EventType eventType;  // <== ENUM here
    private String payload;

    public Events() {
    }

    public Events(String orderId, EventType eventType, String payload) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
    }
}
