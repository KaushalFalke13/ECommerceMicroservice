package com.EComMicroService.ProductsServices.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.EComMicroService.ProductsServices.Configuration.jwtUtil;
import com.EComMicroService.ProductsServices.Services.BagService;

@RestController
@RequestMapping("/products/bag")
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

    public BagController(BagService bagService, jwtUtil jwtUtil) {
        this.bagService = bagService;
        this.jwtUtil = jwtUtil;
    }
    private final jwtUtil jwtUtil;
    private final BagService bagService;

    @GetMapping("/view")
    public String viewBag() {
        return "Viewing bag contents";
    }

    @PostMapping("/add")
    public Integer addToBag(@RequestBody AddToBagRequest request,  @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid");
        }

        String userId = jwtUtil.getUserIdFromToken(token);
        System.err.println("userId in bag controller: " + userId);
        return(Integer) bagService.addItem(userId, request.getProductId());
    } 
}
