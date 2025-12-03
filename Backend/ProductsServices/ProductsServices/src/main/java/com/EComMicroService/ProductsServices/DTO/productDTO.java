package com.EComMicroService.ProductsServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class productDTO {
    
    private String description;
    private String title;
    private Integer MRP;
    private float price;
    private Integer stock;
    private Integer reservedStock;
    private Integer discount;
    private String images1;
    private String images2;
    private String images3;
    private String images4;
    private String images5; 
}
