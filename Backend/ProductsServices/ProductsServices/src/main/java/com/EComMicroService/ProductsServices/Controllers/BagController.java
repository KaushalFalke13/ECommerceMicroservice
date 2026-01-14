package com.EComMicroService.ProductsServices.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.EComMicroService.ProductsServices.Services.BagService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/bag")
public class BagController {

    public static class AddToBagRequest {
        private String productId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }
    
    private final BagService bagService;

    @GetMapping("/view")
    public void viewBag() {
        System.out.println("Viewing bag contents");
    }

    @PostMapping("/add")
    public void addToBag(@RequestBody AddToBagRequest request) {
    System.out.println("Adding product to bag: " + request.getProductId());
    // String userId = jwt.getClaim("userId");
    // bagService.addItem(userId, request.getProductId());
    }
}
