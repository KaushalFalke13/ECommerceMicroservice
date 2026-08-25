package com.EComMicroService.OrdersServices.DTO;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrdersDTO {

    private String orderId;

    @NotBlank(message = "userId is required")
    private String userId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "userEmail is required")
    private String userEmail;

    private String orderNumber;

    @NotNull(message = "items cannot be null")
    private Map<String, Integer> items;

    private double totalPrice;

    @Min(value = 0, message = "totalAmount must be >= 0")
    private float totalAmount;

    @Min(value = 0, message = "discountAmount must be >= 0")
    private float discountAmount;

    @NotBlank(message = "addressId is required")
    private String addressId;

}
