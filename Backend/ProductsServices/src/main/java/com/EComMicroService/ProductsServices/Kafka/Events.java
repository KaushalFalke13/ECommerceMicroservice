package com.EComMicroService.ProductsServices.Kafka;


import java.util.Map;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
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
    private String userId;
    private String orderNumber;
    private EventType eventType;
    private Map<String,Integer> items;
    private long discountAmount;
    private long total;
    private Address shippingAddress;
}

