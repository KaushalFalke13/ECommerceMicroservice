package com.EComMicroService.OrdersServices.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.EComMicroService.OrdersServices.DTO.ChangeOrderDTOs;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;
import com.EComMicroService.OrdersServices.Repositorys.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderRepository orderRepository;
    private final OrderEventService orderEventService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderEventService = orderEventService;
    }

    @Override
    @Transactional
    public String createOrder(OrdersDTO order) throws JsonProcessingException {
        Orders newOrder = ChangeOrderDTOs.changeDTOtoOrders(order);
        String payload = mapper.writeValueAsString(order);
        String orderId = orderRepository.save(newOrder).getOrderId();
        orderEventService.saveOrderEvent(orderId, payload);
        return orderId;
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Object listOrdersForUser(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Object getOrderDetails(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Boolean cancelOrder(String orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        if (!order.getOrderStatus().equals(OrderStatus.DELEVERED)) {
            order.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

}
