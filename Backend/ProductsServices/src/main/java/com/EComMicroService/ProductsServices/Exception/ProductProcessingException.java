package com.EComMicroService.ProductsServices.Exception;

public class ProductProcessingException extends RuntimeException {
    public ProductProcessingException(String message) {
        super(message);
    }

    public ProductProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
