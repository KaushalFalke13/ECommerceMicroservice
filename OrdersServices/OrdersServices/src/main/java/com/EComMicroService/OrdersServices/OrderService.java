package com.EComMicroService.OrdersServices;

import java.util.List;

import com.EComMicroService.OrdersServices.DTO.orderDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;

public interface OrderService {

    Orders createOrder(orderDTO order);

    List<Orders> getOrdersByUserId(String userId);



}
