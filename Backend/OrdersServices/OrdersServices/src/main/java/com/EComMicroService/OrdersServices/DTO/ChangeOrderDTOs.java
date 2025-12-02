package com.EComMicroService.OrdersServices.DTO;

import java.util.UUID;

import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;

public class ChangeOrderDTOs {

    public static Orders changeDTOtoOrders(OrdersDTO dto) {
        return Orders.builder()
                .OrderId(UUID.randomUUID().toString())
                .userId(dto.getUserId())
                .orderNumber(UUID.randomUUID().toString())
                .totalAmount(dto.getTotalAmount())
                .discountAmount(dto.getDiscountAmount())
                .finalAmount(dto.getTotalAmount())
                .orderStatus(OrderStatus.CREATED)
                .build();
    }

    public static OrdersDTO changeOrdersToDto(Orders orders) {
        OrdersDTO ordersDTO = OrdersDTO.builder()
                // .orderId(orders.getOrderId())
                .build();
        return ordersDTO;
    }

    public static OrdersDTO changeEvtToDto(String orderId, String orders) {
        OrdersDTO ordersDTO = OrdersDTO.builder()
                .build();
        return ordersDTO;
    }
}
