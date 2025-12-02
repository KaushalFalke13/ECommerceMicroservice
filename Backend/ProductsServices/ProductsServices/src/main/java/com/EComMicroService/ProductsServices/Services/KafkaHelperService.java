package com.EComMicroService.ProductsServices.Services;

import org.springframework.stereotype.Service;
import com.EComMicroService.ProductsServices.Kafka.Events;

@Service
public class KafkaHelperService {

    // private final productService productService;

    // public KafkaHelperService(productService productService) {
    //     this.productService = productService;
    // }

    public void reserveProducts(Events orderEvent) {

        orderEvent.getPayload().charAt(0);
    }

}
