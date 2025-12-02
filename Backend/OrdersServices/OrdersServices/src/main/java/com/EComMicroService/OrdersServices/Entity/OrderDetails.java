package com.EComMicroService.OrdersServices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetails {

    @Id
    private String orderDetailsId;
    private String orderId;
    private String productId;
    private String orderDate;
    private int quantity;
    private double price;
}
