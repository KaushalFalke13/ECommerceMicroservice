package com.EComMicroService.OrdersServices.Services;

import java.util.List;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {

    String createOrder(OrdersDTO order) throws JsonProcessingException;

    List<Orders> getOrdersByUserId(String userId);

    Object listOrdersForUser(String userId);

    Object getOrderDetails(String orderId);

    Boolean cancelOrder(String orderId);

}
