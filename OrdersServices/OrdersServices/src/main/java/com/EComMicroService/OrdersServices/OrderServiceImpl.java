package com.EComMicroService.OrdersServices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.DTO.orderDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Repositorys.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Orders createOrder(orderDTO order) {
        return orderRepository.save(new Orders());
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

}
