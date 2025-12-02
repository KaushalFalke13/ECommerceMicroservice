package com.EComMicroService.ProductsServices.Kafka;


import com.EComMicroService.ProductsServices.Enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Events {
    private String orderId;
    private EventType eventType;
    private String payload;

}

