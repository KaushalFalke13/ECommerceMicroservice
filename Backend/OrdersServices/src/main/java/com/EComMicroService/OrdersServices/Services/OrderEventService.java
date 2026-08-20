package com.EComMicroService.OrdersServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;

@Service
public interface OrderEventService {

    void saveOrderEvent(OrdersDTO ordersDTO);

    List<OrdersEventsLog> getUnpublishedEvents();

    boolean UpdateOrderEventsLog(OrdersEventsLog eventsLog);
}
