package com.ecommicroservice.orderservices.services;

import org.springframework.stereotype.Service;

import com.ecommicroservice.orderservices.entity.Orders;

@Service
public interface NotificationService {
    void sendOrderConfirmation(Orders order, String userEmail);

    void sendOrderStatusUpdate(Orders order, String userEmail);
}
