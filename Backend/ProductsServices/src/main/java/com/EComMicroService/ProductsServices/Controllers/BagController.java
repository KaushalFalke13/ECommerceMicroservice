package com.EComMicroService.ProductsServices.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Services.BagService;

@RestController
@RequestMapping("/products/bag")
public class BagController {
    
    private final BagService bagService;

    public static class AddToBagRequest {
        private String productId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }

    public BagController(BagService bagService) {
        this.bagService = bagService;
    }

    @PostMapping("/add")
    public List<productDTO> addToBag(@RequestBody AddToBagRequest request, @RequestHeader("Authorization") String authHeader) {
        return bagService.addItem(authHeader, request.getProductId());
    }

    @PostMapping("/remove")
    public List<productDTO> removeFromBag(@RequestBody AddToBagRequest request,@RequestHeader("Authorization") String authHeader) {
        return bagService.removeItem(authHeader, request.getProductId());
    }

    @GetMapping("/bagItems")
    public List<productDTO> getBagItems(@RequestHeader("Authorization") String authHeader) {
        return bagService.getItems(authHeader);
    }
}