package com.EComMicroService.OrdersServices.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDTO {

    private String name;
    private Long number;
    private String street;
    private String city;
    private String state;
    private Long pincode;
    private boolean isDefault;
    private String type;
    private String userId;

}
