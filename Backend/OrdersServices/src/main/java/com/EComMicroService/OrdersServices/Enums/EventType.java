package com.EComMicroService.OrdersServices.Enums;

public enum EventType {
    ORDER_PENDING,
    ORDER_CREATED,
    ORDER_PAID,
    ORDER_CANCELLED,
    ORDER_FAILED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    STOCK_RESERVED,
    STOCK_FAILED,
    ORDER_COMPLETED
}
