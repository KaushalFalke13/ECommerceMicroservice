package com.EComMicroService.OrdersServices.Client;

import java.util.List;
import org.springframework.stereotype.Component;
import com.EComMicroService.OrdersServices.DTO.ApiResponse;
import com.EComMicroService.OrdersServices.DTO.BagItemDTO;

@Component
public class ProductsServiceClientFallback implements ProductsServiceClient {

    @Override
    public ApiResponse<List<BagItemDTO>> getBagItems(String authHeader) {
        return new ApiResponse<>(503, "Products service is currently unavailable. Please try again later.", List.of());
    }

    @Override
    public ApiResponse<Void> clearBag(String authHeader) {
        return new ApiResponse<>(503, "Products service is currently unavailable. Please try again later.");
    }
}
