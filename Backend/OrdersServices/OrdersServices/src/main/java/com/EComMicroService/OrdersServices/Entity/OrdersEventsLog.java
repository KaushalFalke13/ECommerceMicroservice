package com.EComMicroService.OrdersServices.Entity;

import com.EComMicroService.OrdersServices.Enums.EventStatus;
import com.EComMicroService.OrdersServices.KafkaEvents.Events;
import com.EComMicroService.OrdersServices.KafkaEvents.OrderEventConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersEventsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderEventId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "event", columnDefinition = "LONGTEXT")
    @Convert(converter = OrderEventConverter.class)
    private Events event;

    @Enumerated(EnumType.STRING)
    private EventStatus published;

}
