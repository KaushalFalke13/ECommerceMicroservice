package com.EComMicroService.productsservices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.productsservices.DTO.productDTO;

@Service
public interface BagService {

    List<productDTO> addItem(String userId, String productId);

    List<productDTO> removeItem(String userId, String productId);

    List<productDTO> getItems(String userId);

}
