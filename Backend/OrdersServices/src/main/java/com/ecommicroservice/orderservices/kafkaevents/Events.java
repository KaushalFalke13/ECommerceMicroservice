package com.ecommicroservice.orderservices.kafkaevents;

import com.ecommicroservice.orderservices.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Events {
    private String orderId;
    private String userId;
    private String orderNumber;
    private EventType eventType;
    private Map<String, Integer> items;
    private float discountAmount;
    private float total;
    private Address shippingAddress;
}
