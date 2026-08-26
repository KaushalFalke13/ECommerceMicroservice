package com.ecommicroservice.orderservices.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String Id;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "number is required")
    @Min(value = 1, message = "number must be >= 1")
    private Long number;

    @NotBlank(message = "street is required")
    private String street;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    @NotNull(message = "pincode is required")
    @Min(value = 100000, message = "pincode must be at least 100000")
    private Long pincode;

    private boolean isDefault;

    private String type;

    private String userId;

}
