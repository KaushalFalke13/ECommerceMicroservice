package com.EComMicroService.OrdersServices.DTO;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrdersDTO {

    private String orderId;
    private String userId;
    private String orderNumber;
    private Map<String, Integer> items;
    private double totalPrice;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    

}
