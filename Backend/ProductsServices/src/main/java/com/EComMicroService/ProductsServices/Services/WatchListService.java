package com.EComMicroService.ProductsServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.ProductsServices.DTO.productDTO;

@Service
public interface WatchListService {

    List<productDTO> addItem(String userId, String productId);
    List<productDTO> removeItem(String userId, String productId);
    List<productDTO> getItems(String userId);
}
