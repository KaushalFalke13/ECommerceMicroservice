package com.EComMicroService.OrdersServices.Client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.EComMicroService.OrdersServices.DTO.ApiResponse;
import com.EComMicroService.OrdersServices.DTO.BagItemDTO;

@FeignClient(name = "PRODUCTS-SERVICES", url = "${products.service.url:http://localhost:8082}", fallback = ProductsServiceClientFallback.class)
public interface ProductsServiceClient {

    @GetMapping("/bag/items")
    ApiResponse<List<BagItemDTO>> getBagItems(@RequestHeader("Authorization") String authHeader);

    @DeleteMapping("/bag/clear")
    ApiResponse<Void> clearBag(@RequestHeader("Authorization") String authHeader);
}