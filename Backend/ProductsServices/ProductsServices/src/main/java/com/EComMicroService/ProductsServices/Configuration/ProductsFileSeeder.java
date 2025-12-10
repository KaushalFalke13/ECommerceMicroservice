package com.EComMicroService.ProductsServices.Configuration;

import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Services.productServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

// @Configuration
public class ProductsFileSeeder {

    private static final String SEED_PATH = "templates/products.jsfs";
    
    // @Bean
    CommandLineRunner seedProducts(productServiceImpl productService) {
        return args -> {
            ObjectMapper om = new ObjectMapper();
            // om.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            om.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ClassPathResource resource = new ClassPathResource(SEED_PATH);

            if (!resource.exists()) {
                System.out.println("Product seed file not found at classpath:" + SEED_PATH);
                return;
            }

            try (InputStream is = resource.getInputStream()) {
                List<productDTO> products = om.readValue(is, new TypeReference<List<productDTO>>() {});
                System.out.println("Loaded " + products.size() + " products from seed file.");

                int count = 0;
                for (productDTO dto : products) {
                    try {
                        // call your existing single-save method to persist one-by-one
                        productService.saveProducts(dto);
                        count++;
                    } catch (Exception e) {
                        // log and continue so a single bad product doesn't stop the rest
                        System.err.println("Failed to save product '" + dto.getTitle() + "': " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                System.out.println("Inserted " + count + " products (attempted " + products.size() + ").");
            } catch (Exception e) {
                System.err.println("Error reading/parsing seed file: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
