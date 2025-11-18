package com.EComMicroService.OrdersServices.Controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EComMicroService.OrdersServices.OrderService;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/status")
    public String getOrderStatus() {    
        
        return "Order service is up and running!";
    }

    @PatchMapping("/updateAddress")
    public String updateAddress(@RequestParam String orderId) {
        return "Order with ID " + orderId + " has been updated.";
    }

    @PostMapping("/placeOrder")
    public String createOrder() {
        orderService.createOrder(null);
        return "Order created successfully!";
    }

    @GetMapping("/listOrders")
    public String listOrders(@RequestParam String userId) {
        // Logic to list orders would go here
        return "Listing all orders.";   
    }

    @GetMapping("/orderDetails")
    public String getOrderDetails(@RequestParam String orderId) {
        // Logic to get order details would go here
        return "Details for order ID: " + orderId;
    }

    @DeleteMapping("/cancelOrder")
    public String cancelOrder(@RequestParam String orderId) {
        // Logic to cancel an order would go here
        return "Order with ID " + orderId + " has been canceled.";
    }

}