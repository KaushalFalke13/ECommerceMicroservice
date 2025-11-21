package com.EComMicroService.OrdersServices.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersDTO {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalPrice;

}
