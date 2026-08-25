package com.EComMicroService.OrdersServices.Services;

import java.util.List;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {

    String createOrder(OrdersDTO order, String authHeader) throws JsonProcessingException;

    List<Orders> getOrdersByUserId(String userId);

    Orders updateOrderStatus(String orderId, OrderStatus status);

    Object listOrdersForUser(String userId);

    Object getOrderDetails(String orderId);

    Boolean cancelOrder(String orderId) throws JsonProcessingException;

    Boolean deleteOrder(String orderId);

    Boolean updateOrderAddress(String orderId, String addressId);

}
