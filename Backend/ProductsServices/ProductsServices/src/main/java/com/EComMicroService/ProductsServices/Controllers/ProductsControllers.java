package com.EComMicroService.ProductsServices.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Services.productService;

@Controller
@RequestMapping("/productsApi")
public class ProductsControllers {

    private final productService productService;

    public ProductsControllers(@Autowired productService productService) {
        this.productService = productService;
    }
    // search products

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@ModelAttribute productDTO productDTO){
        productService.saveProducts(productDTO);
        return ResponseEntity.ok("Product Created Successfully");
    }

    @PatchMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@ModelAttribute productDTO productDTO, @RequestParam String id){
        productService.updateProducts(productDTO, id);
        return ResponseEntity.ok("Product Updated Successfully");
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam String id){
        productService.deleteProducts(id);
        return ResponseEntity.ok("Product Deleted Successfully");
    }

    @GetMapping("/products")    
    public ResponseEntity<?> getProducts(@RequestParam int pageNumber, @RequestParam int pageSize){
        List<productDTO> productDTOs =  productService.getAllProduct(pageNumber, pageSize);
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/productsById")
    public ResponseEntity<?> getProductsById(@RequestParam String id){
        System.out.println("id received: " + id);
       productDTO productDTO =  productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }


}
