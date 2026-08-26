package com.ecommicroservice.paymentservices.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotBlank(message = "Order ID is required")
    @Size(min = 1, max = 50, message = "Order ID must be between 1 and 50 characters")
    private String orderId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private Long amount;

    @NotBlank(message = "Payment mode is required")
    @Size(min = 3, max = 50, message = "Payment mode must be between 3 and 50 characters")
    private String paymentMode;
}
