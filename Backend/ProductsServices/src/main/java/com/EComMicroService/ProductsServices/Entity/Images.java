package com.EComMicroService.ProductsServices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or AUTO
    private Long image_id;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

}
