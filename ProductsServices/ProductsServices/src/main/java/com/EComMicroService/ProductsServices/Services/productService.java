package com.EComMicroService.ProductsServices.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.products;

@Service
public interface productService {

    products saveProducts(productDTO product);
    products updateProducts(productDTO product,String id);
    productDTO getProductById(String id);
    List<productDTO> getAllProduct(int pageNumber, int pageSize);
    void deleteProducts(String id);

}
