package com.ecommicroservice.orderservices.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ecommicroservice.orderservices.dto.ApiResponse;
import com.ecommicroservice.orderservices.dto.BagItemDTO;

@FeignClient(name = "PRODUCTS-SERVICES", url = "${products.service.url:http://localhost:8082}", fallback = ProductsServiceClientFallback.class)
public interface ProductsServiceClient {

    @GetMapping("/bag/items")
    ApiResponse<List<BagItemDTO>> getBagItems(@RequestHeader("Authorization") String authHeader);

    @DeleteMapping("/bag/clear")
    ApiResponse<Void> clearBag(@RequestHeader("Authorization") String authHeader);
}