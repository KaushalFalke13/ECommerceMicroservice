package com.ecommicroservice.orderservices.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommicroservice.orderservices.dto.OrdersDTO;
import com.ecommicroservice.orderservices.entity.OrdersEventsLog;

@Service
public interface OrderEventService {

    void saveOrderEvent(OrdersDTO ordersDTO);

    List<OrdersEventsLog> getUnpublishedEvents();

    boolean UpdateOrderEventsLog(OrdersEventsLog eventsLog);
}
