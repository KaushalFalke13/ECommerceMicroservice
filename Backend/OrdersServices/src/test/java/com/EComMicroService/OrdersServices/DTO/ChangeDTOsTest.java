package com.EComMicroService.OrdersServices.DTO;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.EComMicroService.OrdersServices.Entity.Address;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;

class ChangeDTOsTest {

    private ChangeDTOs changeDTOs;
    private OrdersDTO ordersDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        changeDTOs = new ChangeDTOs();

        ordersDTO = OrdersDTO.builder()
                .orderId("ORD-001")
                .userId("USER-001")
                .totalAmount(BigDecimal.valueOf(150.00))
                .discountAmount(BigDecimal.valueOf(10.00))
                .items(Map.of("item1", 2))
                .build();

        addressDTO = AddressDTO.builder()
                .Id("1")
                .name("John Doe")
                .street("123 Main St")
                .city("Springfield")
                .state("IL")
                .pincode(62701L)
                .userId("USER-001")
                .build();
    }

    // -------------------- changeDTOtoOrders tests --------------------

    @Test
    void changeDTOtoOrders_shouldConvertDTOToEntity() {
        // Act
        Orders result = changeDTOs.changeDTOtoOrders(ordersDTO);

        // Assert
        assertNotNull(result);
        // The method generates a new UUID for OrderId and orderNumber, so we can't
        // assert equality
        assertNotNull(result.getOrderId());
        assertNotNull(result.getOrderNumber());
        assertEquals(ordersDTO.getUserId(), result.getUserId());
        assertEquals(ordersDTO.getTotalAmount(), result.getTotalAmount());
        assertEquals(ordersDTO.getDiscountAmount(), result.getDiscountAmount());
        assertEquals(OrderStatus.CREATED, result.getOrderStatus()); // Default status
        // finalAmount should be set to totalAmount
        assertEquals(ordersDTO.getTotalAmount(), result.getFinalAmount());
    }

    @Test
    void changeDTOtoOrders_shouldHandleNullDTO() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            changeDTOs.changeDTOtoOrders(null);
        });
    }

    @Test
    void changeDTOtoOrders_shouldHandleNullFields() {
        // Arrange
        OrdersDTO nullFieldsDTO = OrdersDTO.builder().build();

        // Act
        Orders result = changeDTOs.changeDTOtoOrders(nullFieldsDTO);

        // Assert
        assertNotNull(result);
        // The method generates new UUIDs for OrderId and orderNumber even when DTO
        // fields are null
        assertNotNull(result.getOrderId());
        assertNotNull(result.getOrderNumber());
        assertNull(result.getUserId());
        assertNull(result.getTotalAmount());
        assertNull(result.getDiscountAmount());
        assertNull(result.getFinalAmount());
        assertEquals(OrderStatus.CREATED, result.getOrderStatus());
    }

    // -------------------- changeDTOtoAddress tests --------------------

    @Test
    void changeDTOtoAddress_shouldConvertDTOToEntity() {
        // Act
        Address result = changeDTOs.changeDTOtoAddress(addressDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressDTO.getId(), result.getId());
        assertEquals(addressDTO.getName(), result.getName());
        assertEquals(addressDTO.getStreet(), result.getStreet());
        assertEquals(addressDTO.getCity(), result.getCity());
        assertEquals(addressDTO.getState(), result.getState());
        assertEquals(addressDTO.getPincode(), result.getPincode());
        assertEquals(addressDTO.getUserId(), result.getUserId());
    }

    @Test
    void changeDTOtoAddress_shouldHandleNullDTO() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            changeDTOs.changeDTOtoAddress(null);
        });
    }

    @Test
    void changeDTOtoAddress_shouldHandleNullFields() {
        // Arrange
        AddressDTO nullFieldsDTO = AddressDTO.builder().build();

        // Act
        Address result = changeDTOs.changeDTOtoAddress(nullFieldsDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getStreet());
        assertNull(result.getCity());
        assertNull(result.getState());
        assertNull(result.getPincode());
        assertNull(result.getUserId());
    }

    // -------------------- changAddressToDTO tests --------------------

    @Test
    void changAddressToDTO_shouldConvertEntityToDTO() {
        // Arrange
        Address address = Address.builder()
                .id("1")
                .name("John Doe")
                .street("123 Main St")
                .city("Springfield")
                .state("IL")
                .pincode(62701L)
                .userId("USER-001")
                .build();

        // Act
        AddressDTO result = changeDTOs.changAddressToDTO(address);

        // Assert
        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getName(), result.getName());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getPincode(), result.getPincode());
        assertEquals(address.getUserId(), result.getUserId());
    }

    @Test
    void changAddressToDTO_shouldHandleNullEntity() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            changeDTOs.changAddressToDTO(null);
        });
    }

    @Test
    void changAddressToDTO_shouldHandleNullFields() {
        // Arrange
        Address nullFieldsAddress = Address.builder().build();

        // Act
        AddressDTO result = changeDTOs.changAddressToDTO(nullFieldsAddress);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getStreet());
        assertNull(result.getCity());
        assertNull(result.getState());
        assertNull(result.getPincode());
        assertNull(result.getUserId());
    }

    // -------------------- Edge Cases --------------------

    @Test
    void changeDTOtoOrders_shouldHandleLargeValues() {
        // Arrange
        ordersDTO.setTotalAmount(BigDecimal.valueOf(Long.MAX_VALUE));
        ordersDTO.setDiscountAmount(BigDecimal.valueOf(Long.MAX_VALUE));

        // Act
        Orders result = changeDTOs.changeDTOtoOrders(ordersDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(Long.MAX_VALUE), result.getTotalAmount());
        assertEquals(BigDecimal.valueOf(Long.MAX_VALUE), result.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(Long.MAX_VALUE), result.getFinalAmount());
    }

    @Test
    void changeDTOtoOrders_shouldHandleNegativeValues() {
        // Arrange
        ordersDTO.setTotalAmount(BigDecimal.valueOf(-100));
        ordersDTO.setDiscountAmount(BigDecimal.valueOf(-10));

        // Act
        Orders result = changeDTOs.changeDTOtoOrders(ordersDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(-100), result.getTotalAmount());
        assertEquals(BigDecimal.valueOf(-10), result.getDiscountAmount());
    }

    // -------------------- Additional coverage for remaining methods
    // --------------------

    @Test
    void changeOrdersToDto_shouldReturnOrdersDTO() {
        // Arrange
        Orders orders = Orders.builder()
                .OrderId("ORD-001")
                .orderNumber("ON-001")
                .userId("USER-001")
                .totalAmount(BigDecimal.valueOf(150.00))
                .discountAmount(BigDecimal.valueOf(10.00))
                .finalAmount(BigDecimal.valueOf(140.00))
                .orderStatus(OrderStatus.CREATED)
                .build();

        // Act
        OrdersDTO result = changeDTOs.changeOrdersToDto(orders);

        // Assert
        assertNotNull(result);
        // The method currently returns an empty DTO (builder without fields)
        assertNull(result.getOrderId());
        assertNull(result.getUserId());
        assertNull(result.getTotalAmount());
        assertNull(result.getDiscountAmount());
        assertNull(result.getItems());
    }

    @Test
    void changeOrdersToDto_shouldHandleNullOrders() {
        // Act
        OrdersDTO result = changeDTOs.changeOrdersToDto(null);

        // Assert
        assertNotNull(result);
        // The method ignores the input parameter and returns an empty DTO
        assertNull(result.getOrderId());
        assertNull(result.getUserId());
        assertNull(result.getTotalAmount());
        assertNull(result.getDiscountAmount());
        assertNull(result.getItems());
    }

    @Test
    void changeEvtToDto_shouldReturnOrdersDTO() {
        // Arrange
        String orderId = "ORD-001";
        String ordersJson = "{\"order\":\"test\"}";

        // Act
        OrdersDTO result = changeDTOs.changeEvtToDto(orderId, ordersJson);

        // Assert
        assertNotNull(result);
        // The method currently returns an empty DTO (builder without fields)
        assertNull(result.getOrderId());
        assertNull(result.getUserId());
        assertNull(result.getTotalAmount());
        assertNull(result.getDiscountAmount());
        assertNull(result.getItems());
    }

    @Test
    void changeEvtToDto_shouldHandleNullParameters() {
        // Act
        OrdersDTO result = changeDTOs.changeEvtToDto(null, null);

        // Assert
        assertNotNull(result);
        assertNull(result.getOrderId());
        assertNull(result.getUserId());
        assertNull(result.getTotalAmount());
        assertNull(result.getDiscountAmount());
        assertNull(result.getItems());
    }
}
