package com.EComMicroService.ProductsServices.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.products;
import com.EComMicroService.ProductsServices.Repositorys.productRepository;
import com.EComMicroService.ProductsServices.Services.helperServices;
import com.EComMicroService.ProductsServices.Services.productServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceImplTest {

    @Mock
    private productRepository productRepository;

    @Spy
    private helperServices helperServices = new helperServices();

    @InjectMocks
    private productServiceImpl productService;

    private productDTO testProductDTO;
    private products testProduct;
    private String testProductId;

    @BeforeEach
    void setUp() {
        testProductId = UUID.randomUUID().toString();

        testProductDTO = new productDTO();
        testProductDTO.setTitle("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(99.99f);
        testProductDTO.setStock(10);
        testProductDTO.setMRP(199);
        testProductDTO.setTitle("Test Product");

        testProduct = products.builder()
                .productId(testProductId)
                .title("Test Product")
                .description("Test Description")
                .price(99.99f)
                .stock(10)
                .reservedStock(0)
                .discount(10)
                .build();
    }

    @Test
    @DisplayName("Should save product successfully")
    void saveProducts_ShouldReturnSavedProduct_WhenValidDTO() {
        // Arrange
        when(productRepository.save(any(products.class))).thenReturn(testProduct);

        // Act
        products result = productService.saveProducts(testProductDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getProductId(), result.getProductId());
        assertEquals(testProduct.getTitle(), result.getTitle());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getPrice(), result.getPrice());
        assertEquals(testProduct.getStock(), result.getStock());
        verify(productRepository, times(1)).save(any(products.class));
    }

    @Test
    @DisplayName("Should map DTO to entity correctly when saving")
    void saveProducts_ShouldMapDTOToEntityCorrectly() {
        // Arrange
        when(productRepository.save(any(products.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        products result = productService.saveProducts(testProductDTO);

        // Assert
        assertEquals(testProductDTO.getTitle(), result.getTitle());
        assertEquals(testProductDTO.getDescription(), result.getDescription());
        assertEquals(testProductDTO.getPrice(), result.getPrice());
        assertEquals(testProductDTO.getStock(), result.getStock());
    }

    @Test
    @DisplayName("Should update product successfully")
    void updateProducts_ShouldUpdateAndReturnProduct_WhenExists() {
        // Arrange
        productDTO updateDTO = new productDTO();
        updateDTO.setTitle("Updated Product");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPrice(149.99f);
        updateDTO.setStock(20);
        updateDTO.setMRP(299);

        when(productRepository.findById(testProductId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(products.class))).thenReturn(testProduct);

        // Act
        products result = productService.updateProducts(updateDTO, testProductId);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(149.99f, result.getPrice());
        assertEquals(20, result.getStock());
        verify(productRepository, times(1)).findById(testProductId);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Should return null when updating non-existent product")
    void updateProducts_ShouldReturnNull_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(testProductId)).thenReturn(Optional.empty());

        // Act
        products result = productService.updateProducts(testProductDTO, testProductId);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(testProductId);
        verify(productRepository, never()).save(any(products.class));
    }

    @Test
    @DisplayName("Should get product by ID when exists")
    void getProductById_ShouldReturnProductDTO_WhenExists() {
        // Arrange
        when(productRepository.findById(testProductId)).thenReturn(Optional.of(testProduct));

        // Act
        productDTO result = productService.getProductById(testProductId);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getProductId(), result.getId());
        assertEquals(testProduct.getTitle(), result.getTitle());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getPrice(), result.getPrice());
        verify(productRepository, times(1)).findById(testProductId);
    }

    @Test
    @DisplayName("Should return null when getting non-existent product by ID")
    void getProductById_ShouldReturnNull_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(testProductId)).thenReturn(Optional.empty());

        // Act
        productDTO result = productService.getProductById(testProductId);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(testProductId);
    }

    @Test
    @DisplayName("Should get all products with pagination")
    void getAllProduct_WithPagination_ShouldReturnPageOfProducts() {
        // Arrange
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<products> productList = Arrays.asList(testProduct);
        Page<products> productPage = new PageImpl<>(productList, pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act
        List<productDTO> result = productService.getAllProduct(pageNumber, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getTitle(), result.get(0).getTitle());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should get all products without pagination")
    void getAllProduct_WithoutPagination_ShouldReturnAllProducts() {
        // Arrange
        List<products> productList = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<productDTO> result = productService.getAllProduct();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getTitle(), result.get(0).getTitle());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete product successfully")
    void deleteProducts_ShouldDeleteProduct_WhenExists() {
        // Arrange
        doNothing().when(productRepository).deleteById(testProductId);

        // Act
        productService.deleteProducts(testProductId);

        // Assert
        verify(productRepository, times(1)).deleteById(testProductId);
    }

    @Test
    @DisplayName("Should reserve stock when quantity is available")
    void reserveStock_ShouldReturnTrue_WhenSufficientStockAvailable() {
        // Arrange
        int reserveQty = 3;
        when(productRepository.reserveStockIfAvailable(testProductId, reserveQty)).thenReturn(1);

        // Act
        boolean result = productService.reserveStock(testProductId, reserveQty);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).reserveStockIfAvailable(testProductId, reserveQty);
    }

    @Test
    @DisplayName("Should return false when reserving stock with insufficient quantity")
    void reserveStock_ShouldReturnFalse_WhenInsufficientStock() {
        // Arrange
        int reserveQty = 15;
        when(productRepository.reserveStockIfAvailable(testProductId, reserveQty)).thenReturn(0);

        // Act
        boolean result = productService.reserveStock(testProductId, reserveQty);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).reserveStockIfAvailable(testProductId, reserveQty);
    }

    @Test
    @DisplayName("Should return false when reserving stock for non-existent product")
    void reserveStock_ShouldReturnFalse_WhenProductNotFound() {
        // Arrange
        int reserveQty = 5;
        when(productRepository.reserveStockIfAvailable(testProductId, reserveQty)).thenReturn(0);

        // Act
        boolean result = productService.reserveStock(testProductId, reserveQty);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).reserveStockIfAvailable(testProductId, reserveQty);
    }

    @Test
    @DisplayName("Should release stock successfully")
    void releaseStock_ShouldReleaseStock_WhenProductExists() {
        // Arrange
        int releaseQty = 3;
        when(productRepository.releaseReservedStock(testProductId, releaseQty)).thenReturn(1);

        // Act
        boolean result = productService.releaseStock(testProductId, releaseQty);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).releaseReservedStock(testProductId, releaseQty);
    }

    @Test
    @DisplayName("Should return false when releasing stock for non-existent product")
    void releaseStock_ShouldReturnFalse_WhenProductNotFound() {
        // Arrange
        int releaseQty = 5;
        when(productRepository.releaseReservedStock(testProductId, releaseQty)).thenReturn(0);

        // Act
        boolean result = productService.releaseStock(testProductId, releaseQty);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).releaseReservedStock(testProductId, releaseQty);
    }

    @Test
    @DisplayName("Should confirm stock successfully")
    void confirmStock_ShouldConfirmStock_WhenProductExists() {
        // Arrange
        int confirmQty = 3;
        when(productRepository.confirmReservedStock(testProductId, confirmQty)).thenReturn(1);

        // Act
        boolean result = productService.confirmStock(testProductId, confirmQty);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).confirmReservedStock(testProductId, confirmQty);
    }

    @Test
    @DisplayName("Should return false when confirming stock for non-existent product")
    void confirmStock_ShouldReturnFalse_WhenProductNotFound() {
        // Arrange
        int confirmQty = 5;
        when(productRepository.confirmReservedStock(testProductId, confirmQty)).thenReturn(0);

        // Act
        boolean result = productService.confirmStock(testProductId, confirmQty);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).confirmReservedStock(testProductId, confirmQty);
    }
}
