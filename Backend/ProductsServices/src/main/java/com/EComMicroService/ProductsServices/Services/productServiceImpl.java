package com.EComMicroService.productsservices.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.EComMicroService.productsservices.DTO.productDTO;
import com.EComMicroService.productsservices.Entity.products;
import com.EComMicroService.productsservices.Repositorys.productRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class productServiceImpl implements productService {

    private static final String PRODUCT_SERVICE = "productService";

    @Autowired
    private productRepository productRepository;

    @Autowired
    private helperServices helperServices;

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "saveProductFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public products saveProducts(productDTO productDTO) {
        products product = helperServices.changeDtoToProducts(productDTO);
        return productRepository.save(product);
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "getProductByIdFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public productDTO getProductById(String id) {
        products products = productRepository.findById(id).orElse(null);
        if (products == null) {
            return null;
        }
        return helperServices.changeProductToDto(products);
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "getAllProductFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public List<productDTO> getAllProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<products> products = productRepository.findAll(pageable).getContent();
        return products.stream()
                .map(prod -> helperServices.changeProductToDto(prod))
                .toList();
    }

    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "getAllProductFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public List<productDTO> getAllProduct() {
        List<products> products = productRepository.findAll();
        return products.stream()
                .map(prod -> helperServices.changeProductToDto(prod))
                .toList();
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "updateProductFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public products updateProducts(productDTO product, String id) {
        products existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setTitle(product.getTitle());
            existingProduct.setMRP(product.getMRP());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setDiscount(product.getDiscount());
            if (existingProduct.getImages() != null) {
                existingProduct.getImages().setImage1(product.getImages1());
                existingProduct.getImages().setImage2(product.getImages2());
                existingProduct.getImages().setImage3(product.getImages3());
                existingProduct.getImages().setImage4(product.getImages4());
                existingProduct.getImages().setImage5(product.getImages5());
            }
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "deleteProductFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public void deleteProducts(String id) {
        productRepository.deleteById(id);
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "reserveStockFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public boolean reserveStock(String productId, int qty) {
        int updated = productRepository.reserveStockIfAvailable(productId, qty);
        return updated == 1;
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "releaseStockFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public boolean releaseStock(String productId, int qty) {
        int updated = productRepository.releaseReservedStock(productId, qty);
        return updated == 1;
    }

    @Override
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "confirmStockFallback")
    @Retry(name = PRODUCT_SERVICE)
    @Bulkhead(name = PRODUCT_SERVICE)
    @RateLimiter(name = PRODUCT_SERVICE)
    public boolean confirmStock(String productId, int qty) {
        int updated = productRepository.confirmReservedStock(productId, qty);
        return updated == 1;
    }

    // ==================== FALLBACK METHODS ====================

    public products saveProductFallback(productDTO productDTO, Throwable t) {
        log.error("Save product fallback triggered: {}", t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public productDTO getProductByIdFallback(String id, Throwable t) {
        log.error("Get product by id fallback triggered for id {}: {}", id, t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public List<productDTO> getAllProductFallback(int pageNumber, int pageSize, Throwable t) {
        log.error("Get all products fallback triggered: {}", t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public List<productDTO> getAllProductFallback(Throwable t) {
        log.error("Get all products fallback triggered: {}", t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public products updateProductFallback(productDTO product, String id, Throwable t) {
        log.error("Update product fallback triggered for id {}: {}", id, t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public void deleteProductFallback(String id, Throwable t) {
        log.error("Delete product fallback triggered for id {}: {}", id, t.getMessage());
        throw new RuntimeException("Product service is temporarily unavailable. Please try again later.", t);
    }

    public boolean reserveStockFallback(String productId, int qty, Throwable t) {
        log.error("Reserve stock fallback triggered for product {}: {}", productId, t.getMessage());
        return false;
    }

    public boolean releaseStockFallback(String productId, int qty, Throwable t) {
        log.error("Release stock fallback triggered for product {}: {}", productId, t.getMessage());
        return false;
    }

    public boolean confirmStockFallback(String productId, int qty, Throwable t) {
        log.error("Confirm stock fallback triggered for product {}: {}", productId, t.getMessage());
        return false;
    }
}
