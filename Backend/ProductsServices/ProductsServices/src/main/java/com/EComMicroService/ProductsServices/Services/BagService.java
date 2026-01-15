package com.EComMicroService.ProductsServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BagService {

    int addItem(String userId, String productId);
    void removeItem(String userId, String productId);
    List<String> getItems(String userId);
    
}
