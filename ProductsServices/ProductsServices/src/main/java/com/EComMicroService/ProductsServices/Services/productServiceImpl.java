package com.EComMicroService.ProductsServices.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.products;
import com.EComMicroService.ProductsServices.Repositorys.productRepository;

@Service
public class productServiceImpl implements productService {

    @Autowired
    private productRepository productRepository;

    @Autowired
    private helperServices helperServices;

    @Override
    public products saveProducts(productDTO productDTO) {
        products product = helperServices.changeDtoToProducts(productDTO);
        return productRepository.save(product);
    }

    @Override
    public productDTO getProductById(String id) {
        products products = productRepository.findById(id).orElse(null);
        return helperServices.changeProductToDto(products);
    }

    @Override
    public List<productDTO> getAllProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<products> products = productRepository.findAll(pageable).getContent();
        return products.stream()
                .map(prod -> helperServices.changeProductToDto(prod))
                .toList();
    }

    @Override
    public products updateProducts(productDTO product, String id) {
        products existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setTitle(product.getTitle());
            existingProduct.setMRP(product.getMRP());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setDiscount(product.getDiscount());
            existingProduct.getImages().setImage1(product.getImages1());
            existingProduct.getImages().setImage2(product.getImages2());
            existingProduct.getImages().setImage3(product.getImages3());
            existingProduct.getImages().setImage4(product.getImages4());
            existingProduct.getImages().setImage5(product.getImages5());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProducts(String id) {
        productRepository.deleteById(id);
    }

}
