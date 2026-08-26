package com.EComMicroService.productsservices.Services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.EComMicroService.productsservices.Enums.EventType;
import com.EComMicroService.productsservices.Kafka.Events;

import jakarta.transaction.Transactional;

@Service
public class KafkaHelperService {

    private final productService productService;

    public KafkaHelperService(productService productService) {
        this.productService = productService;
    }

    public boolean sendProductEvents(Events event) {
        event.setEventType(EventType.STOCK_RESERVED);
        return true;
    }

    @Transactional
    public boolean reserveProducts(Events orderEvent) {

        if (orderEvent == null || orderEvent.getItems() == null) {
            return false;
        }

        for (Map.Entry<String, Integer> entry : orderEvent.getItems().entrySet()) {
            String productId = entry.getKey();
            Integer qty = entry.getValue();
            boolean ok = productService.reserveStock(productId, qty);
            if (!ok) {
                // If reservation fails for any product, return exception
                return false;
            }
        }

        return true;
    }

    @Transactional
    public boolean releaseProducts(Events orderEvent) {

        if (orderEvent == null || orderEvent.getItems() == null) {
            return false;
        }

        for (Map.Entry<String, Integer> entry : orderEvent.getItems().entrySet()) {
            String productId = entry.getKey();
            Integer qty = entry.getValue();
            boolean ok = productService.releaseStock(productId, qty);
            if (!ok) {
                // If reservation fails for any product, return exception
                return false;
            }
        }

        return true;
    }

}
