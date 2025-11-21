package com.EComMicroService.OrdersServices.Services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Repositorys.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String createOrder(OrdersDTO order) {
        return orderRepository.save(new Orders()).getId();
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Object listOrdersForUser(String userId) {
        throw new UnsupportedOperationException("Unimplemented method 'listOrdersForUser'");
    }

    @Override
    public Object getOrderDetails(String orderId) {
        throw new UnsupportedOperationException("Unimplemented method 'getOrderDetails'");
    }

    @Override
    public Boolean cancelOrder(String orderId) {
        throw new UnsupportedOperationException("Unimplemented method 'cancelOrder'");
    }

}
