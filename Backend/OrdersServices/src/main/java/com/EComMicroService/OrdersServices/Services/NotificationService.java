package com.EComMicroService.OrdersServices.Services;

import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.Entity.Orders;

@Service
public interface NotificationService {
    void sendOrderConfirmation(Orders order, String userEmail);

    void sendOrderStatusUpdate(Orders order, String userEmail);
}
