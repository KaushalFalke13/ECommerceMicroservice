package com.ecommicroservice.orderservices.enums;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    PAYMENT_FAILED,
    OUT_OF_STOCK,
    RETURNED,
    REFUNDED
}
