package com.EComMicroService.OrdersServices.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrdersDTO {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    

}
