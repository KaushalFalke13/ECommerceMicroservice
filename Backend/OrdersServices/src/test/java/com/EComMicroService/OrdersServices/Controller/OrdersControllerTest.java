package com.EComMicroService.OrdersServices.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.EComMicroService.OrdersServices.Controllers.OrdersController;
import com.EComMicroService.OrdersServices.DTO.AddressDTO;
import com.EComMicroService.OrdersServices.DTO.ApiResponse;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Address;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Services.AddressService;
import com.EComMicroService.OrdersServices.Services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private OrdersController ordersController;

    private final String VALID_TOKEN = "Bearer valid.jwt.token";
    private final String USER_ID = "user-123";
    private final String ORDER_ID = "ord-456";

    private OrdersDTO ordersDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        ordersDTO = OrdersDTO.builder()
                .orderId(ORDER_ID)
                .userId(USER_ID)
                .orderNumber("ORD-001")
                .totalAmount(150.00f)
                .build();

        addressDTO = AddressDTO.builder()
                .Id("1")
                .name("John Doe")
                .street("123 Main St")
                .city("Springfield")
                .state("IL")
                .pincode(62701L)
                .userId(USER_ID)
                .build();
    }

    // ==================== getOrderStatus Tests ====================

    @Test
    void getOrderStatus_shouldReturnSuccessMessage() {
        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.getOrderStatus();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Order service is up and running!", response.getBody().getData());
    }

    // ==================== addNewAddress Tests ====================

    @Test
    void addNewAddress_shouldAddAddressAndReturnSuccess() {
        // Arrange
        Address mockAddress = Address.builder().id("1").name("John Doe").build();
        when(addressService.addAddress(any(AddressDTO.class), anyString())).thenReturn(mockAddress);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.addNewAddress(addressDTO, VALID_TOKEN);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Address Created Sucessfully", response.getBody().getData());
        verify(addressService, times(1)).addAddress(addressDTO, VALID_TOKEN);
    }

    @Test
    void addNewAddress_shouldHandleNullAuthHeader() {
        // Arrange
        Address mockAddress = Address.builder().id("1").name("John Doe").build();
        when(addressService.addAddress(any(AddressDTO.class), nullable(String.class))).thenReturn(mockAddress);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.addNewAddress(addressDTO, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(addressService, times(1)).addAddress(addressDTO, null);
    }

    @Test
    void addNewAddress_shouldHandleAddressDTOWithNullFields() {
        // Arrange
        AddressDTO incompleteAddress = AddressDTO.builder()
                .Id("1")
                .name("John Doe")
                .build();
        Address mockAddress = Address.builder().id("1").name("John Doe").build();
        when(addressService.addAddress(any(AddressDTO.class), nullable(String.class))).thenReturn(mockAddress);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.addNewAddress(incompleteAddress, VALID_TOKEN);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(addressService, times(1)).addAddress(incompleteAddress, VALID_TOKEN);
    }

    // ==================== removeAddress Tests ====================

    @Test
    void removeAddress_shouldRemoveAddressAndReturnSuccess() {
        // Arrange
        String addressId = "1";
        doNothing().when(addressService).removeAddress(addressId);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.removeAddress(addressId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Address Removed Sucessfully", response.getBody().getData());
        verify(addressService, times(1)).removeAddress(addressId);
    }

    @Test
    void removeAddress_shouldHandleNullAddressId() {
        // Arrange
        doNothing().when(addressService).removeAddress(null);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.removeAddress(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(addressService, times(1)).removeAddress(null);
    }

    // ==================== getAddress Tests ====================

    @Test
    void getAddress_shouldReturnAddressesAndSuccess() {
        // Arrange
        List<AddressDTO> addressList = Arrays.asList(addressDTO);
        when(addressService.getAddressesByUserId(VALID_TOKEN)).thenReturn(addressList);

        // Act
        ResponseEntity<ApiResponse<List<AddressDTO>>> response = ordersController.getAddress(VALID_TOKEN);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(" ", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().size());
        verify(addressService, times(1)).getAddressesByUserId(VALID_TOKEN);
    }

    @Test
    void getAddress_shouldReturnEmptyList_whenNoAddresses() {
        // Arrange
        when(addressService.getAddressesByUserId(VALID_TOKEN)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<ApiResponse<List<AddressDTO>>> response = ordersController.getAddress(VALID_TOKEN);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isEmpty());
        verify(addressService, times(1)).getAddressesByUserId(VALID_TOKEN);
    }

    @Test
    void getAddress_shouldHandleNullAuthHeader() {
        // Arrange
        when(addressService.getAddressesByUserId(null)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<ApiResponse<List<AddressDTO>>> response = ordersController.getAddress(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(addressService, times(1)).getAddressesByUserId(null);
    }

    // ==================== createOrder Tests ====================

    @Test
    void createOrder_shouldReturnCreatedOrderId_whenOrderCreated() throws JsonProcessingException {
        // Arrange
        when(orderService.createOrder(any(OrdersDTO.class), any(String.class))).thenReturn(ORDER_ID);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.createOrder(VALID_TOKEN, ordersDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals("Order created successfully", response.getBody().getMessage());
        assertEquals(ORDER_ID, response.getBody().getData());
        verify(orderService, times(1)).createOrder(ordersDTO, VALID_TOKEN);
    }

    @Test
    void createOrder_shouldReturnInternalServerError_whenOrderCreationFails() throws JsonProcessingException {
        // Arrange
        when(orderService.createOrder(any(OrdersDTO.class), any(String.class))).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.createOrder(VALID_TOKEN, ordersDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Failed to create order", response.getBody().getMessage());
        verify(orderService, times(1)).createOrder(ordersDTO, VALID_TOKEN);
    }

    @Test
    void createOrder_shouldReturnInternalServerError_whenJsonProcessingExceptionThrown()
            throws JsonProcessingException {
        // Arrange
        when(orderService.createOrder(any(OrdersDTO.class), any(String.class)))
                .thenThrow(new JsonProcessingException("JSON parsing error") {
                });

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> ordersController.createOrder(VALID_TOKEN, ordersDTO));
        verify(orderService, times(1)).createOrder(ordersDTO, VALID_TOKEN);
    }

    @Test
    void createOrder_shouldHandleOrdersDTOWithNullFields() throws JsonProcessingException {
        // Arrange
        OrdersDTO incompleteOrder = OrdersDTO.builder()
                .userId(USER_ID)
                .build();
        when(orderService.createOrder(any(OrdersDTO.class), any(String.class))).thenReturn("ord-999");

        // Act
        ResponseEntity<ApiResponse<String>> response = ordersController.createOrder(VALID_TOKEN, incompleteOrder);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(orderService, times(1)).createOrder(incompleteOrder, VALID_TOKEN);
    }

    // ==================== listOrders Tests ====================

    @Test
    void listOrders_shouldReturnOrders_whenUserHasOrders() {
        // Arrange
        Orders order1 = Orders.builder().OrderId(ORDER_ID).userId(USER_ID).build();
        Orders order2 = Orders.builder().OrderId("ord-789").userId(USER_ID).build();
        List<Orders> orderList = Arrays.asList(order1, order2);
        when(orderService.listOrdersForUser(USER_ID)).thenReturn(orderList);

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.listOrders(USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Orders fetched successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).listOrdersForUser(USER_ID);
    }

    @Test
    void listOrders_shouldReturnEmptyList_whenUserHasNoOrders() {
        // Arrange
        when(orderService.listOrdersForUser(USER_ID)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.listOrders(USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).listOrdersForUser(USER_ID);
    }

    @Test
    void listOrders_shouldHandleNullUserId() {
        // Arrange
        when(orderService.listOrdersForUser(null)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.listOrders(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).listOrdersForUser(null);
    }

    // ==================== getOrderDetails Tests ====================

    @Test
    void getOrderDetails_shouldReturnOrder_whenOrderExists() {
        // Arrange
        Orders order = Orders.builder().OrderId(ORDER_ID).userId(USER_ID).build();
        when(orderService.getOrderDetails(ORDER_ID)).thenReturn(order);

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.getOrderDetails(ORDER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Order details fetched", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).getOrderDetails(ORDER_ID);
    }

    @Test
    void getOrderDetails_shouldReturnNotFound_whenOrderDoesNotExist() {
        // Arrange
        when(orderService.getOrderDetails(ORDER_ID)).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.getOrderDetails(ORDER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Order not found", response.getBody().getMessage());
        verify(orderService, times(1)).getOrderDetails(ORDER_ID);
    }

    @Test
    void getOrderDetails_shouldHandleNullOrderId() {
        // Arrange
        when(orderService.getOrderDetails(null)).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse<Object>> response = ordersController.getOrderDetails(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).getOrderDetails(null);
    }

    // ==================== cancelOrder Tests ====================

    @Test
    void cancelOrder_shouldReturnSuccess_whenOrderCanceled() throws JsonProcessingException {
        // Arrange
        when(orderService.cancelOrder(ORDER_ID)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Void>> response = ordersController.cancelOrder(ORDER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Order canceled successfully", response.getBody().getMessage());
        verify(orderService, times(1)).cancelOrder(ORDER_ID);
    }

    @Test
    void cancelOrder_shouldReturnBadRequest_whenCancelFails() throws JsonProcessingException {
        // Arrange
        when(orderService.cancelOrder(ORDER_ID)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Void>> response = ordersController.cancelOrder(ORDER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Failed to cancel order", response.getBody().getMessage());
        verify(orderService, times(1)).cancelOrder(ORDER_ID);
    }

    // ==================== updateAddress Tests ====================

    @Test
    void updateAddress_shouldReturnSuccess_whenOrderExists() {
        // Arrange
        String orderId = "ord-456";
        String addressId = "1";
        when(orderService.updateOrderAddress(orderId, addressId)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Void>> response = ordersController.updateAddress(orderId, addressId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Order address updated successfully", response.getBody().getMessage());
        verify(orderService, times(1)).updateOrderAddress(orderId, addressId);
    }

    @Test
    void updateAddress_shouldReturnBadRequest_whenOrderNotFound() {
        // Arrange
        String orderId = "ord-999";
        String addressId = "999";
        when(orderService.updateOrderAddress(orderId, addressId)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Void>> response = ordersController.updateAddress(orderId, addressId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Failed to update order address", response.getBody().getMessage());
        verify(orderService, times(1)).updateOrderAddress(orderId, addressId);
    }

    @Test
    void cancelOrder_shouldHandleNullOrderId() throws JsonProcessingException {
        // Arrange
        when(orderService.cancelOrder(null)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Void>> response = ordersController.cancelOrder(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(orderService, times(1)).cancelOrder(null);
    }

    // ==================== Edge Cases ====================

    @Test
    void addNewAddress_shouldHandleAddressServiceException() {
        // Arrange
        doThrow(new RuntimeException("Address service error"))
                .when(addressService).addAddress(any(AddressDTO.class), anyString());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ordersController.addNewAddress(addressDTO, VALID_TOKEN));
        verify(addressService, times(1)).addAddress(addressDTO, VALID_TOKEN);
    }

    @Test
    void getAddress_shouldHandleAddressServiceException() {
        // Arrange
        when(addressService.getAddressesByUserId(VALID_TOKEN))
                .thenThrow(new RuntimeException("Address service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ordersController.getAddress(VALID_TOKEN));
        verify(addressService, times(1)).getAddressesByUserId(VALID_TOKEN);
    }

    @Test
    void createOrder_shouldHandleOrderServiceException() throws JsonProcessingException {
        // Arrange
        when(orderService.createOrder(any(OrdersDTO.class), any(String.class)))
                .thenThrow(new RuntimeException("Order service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ordersController.createOrder(VALID_TOKEN, ordersDTO));
        verify(orderService, times(1)).createOrder(ordersDTO, VALID_TOKEN);
    }

    @Test
    void listOrders_shouldHandleOrderServiceException() {
        // Arrange
        when(orderService.listOrdersForUser(USER_ID))
                .thenThrow(new RuntimeException("Order service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ordersController.listOrders(USER_ID));
        verify(orderService, times(1)).listOrdersForUser(USER_ID);
    }
}
