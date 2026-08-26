package com.ecommicroservice.paymentservices.kafkaevents;

import java.util.Map;

import com.ecommicroservice.paymentservices.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private long discountAmount;
    private long total;
    private String paymentMode;
    private String shippingAddress;
    private String userEmail;
}
