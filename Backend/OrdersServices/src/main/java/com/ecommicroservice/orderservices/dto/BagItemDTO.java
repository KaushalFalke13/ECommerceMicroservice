package com.ecommicroservice.orderservices.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BagItemDTO {

    @NotBlank(message = "productId is required")
    private String productId;

    private String productTitle;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be >= 1")
    private Integer quantity;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be >= 0")
    private Float price;

    @Min(value = 0, message = "totalPrice must be >= 0")
    private Float totalPrice;
}
