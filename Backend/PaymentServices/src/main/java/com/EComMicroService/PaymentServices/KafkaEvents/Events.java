package com.EComMicroService.PaymentServices.KafkaEvents;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import com.EComMicroService.PaymentServices.Enums.EventType;
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
    private Map<String,Integer> items;
    private long discountAmount;
    private long total;
    private Address shippingAddress;
}


