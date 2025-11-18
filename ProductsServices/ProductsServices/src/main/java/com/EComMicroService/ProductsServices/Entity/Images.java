package com.EComMicroService.ProductsServices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Images {

    @Id
    private String id; 
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
 
}
