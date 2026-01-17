package com.EComMicroService.OrdersServices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Address {

    @Id
    private Long id;
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
