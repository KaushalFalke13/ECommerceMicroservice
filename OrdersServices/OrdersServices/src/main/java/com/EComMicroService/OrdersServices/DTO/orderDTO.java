package com.EComMicroService.OrdersServices.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class orderDTO {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalPrice;

}
