package com.EComMicroService.ProductsServices.Services;

import org.springframework.stereotype.Service;

import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.Images;
import com.EComMicroService.ProductsServices.Entity.products;

@Service
public class helperServices {

    products changeDtoToProducts(productDTO productDTO){
        return products.builder()
                            .MRP(productDTO.getMRP())
                            .title(productDTO.getTitle())
                            .price(productDTO.getPrice())
                            .stock(productDTO.getStock())
                            .discount(productDTO.getDiscount())
                            .description(productDTO.getDescription())
                            .images(Images.builder()
                                .image1(productDTO.getImages1())
                                .image2(productDTO.getImages2())
                                .image3(productDTO.getImages3())
                                .image4(productDTO.getImages4())
                                .image5(productDTO.getImages5())            
                                .build())
                            .build();
    }

    productDTO changeProductToDto(products products){
        return productDTO.builder()
                                .title(products.getTitle())
                                .MRP(products.getMRP())
                                .description(products.getDescription())
                                .price(products.getPrice())
                                .stock(products.getStock())
                                .discount(products.getDiscount())
                                .images1(products.getImages().getImage1())
                                .images2(products.getImages().getImage2())
                                .images3(products.getImages().getImage3())
                                .images4(products.getImages().getImage4())
                                .images5(products.getImages().getImage5())
                                .build();
    }

}
