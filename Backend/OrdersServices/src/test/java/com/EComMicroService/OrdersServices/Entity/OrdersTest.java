package com.EComMicroService.OrdersServices.Entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.EComMicroService.OrdersServices.Enums.OrderStatus;

class OrdersTest {

    @Test
    void builder_shouldCreateOrderWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String orderId = "ORD-001";
        String orderNumber = "ON-001";
        String userId = "USER-001";
        BigDecimal totalAmount = BigDecimal.valueOf(150.00);
        BigDecimal discountAmount = BigDecimal.valueOf(10.00);
        BigDecimal finalAmount = BigDecimal.valueOf(140.00);
        OrderStatus status = OrderStatus.CREATED;

        // Act
        Orders order = Orders.builder()
                .OrderId(orderId)
                .orderNumber(orderNumber)
                .userId(userId)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .orderStatus(status)
                .createdAt(now)
                .build();

        // Assert
        assertEquals(orderId, order.getOrderId());
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(userId, order.getUserId());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(discountAmount, order.getDiscountAmount());
        assertEquals(finalAmount, order.getFinalAmount());
        assertEquals(status, order.getOrderStatus());
        assertEquals(now, order.getCreatedAt());
    }

    @Test
    void builder_shouldCreateOrderWithDefaultValues() {
        // Act
        Orders order = Orders.builder()
                .orderNumber("ON-002")
                .userId("USER-002")
                .build();

        // Assert
        assertNull(order.getOrderId());
        assertEquals("ON-002", order.getOrderNumber());
        assertEquals("USER-002", order.getUserId());
        assertNull(order.getTotalAmount());
        assertNull(order.getDiscountAmount());
        assertNull(order.getFinalAmount());
        assertNull(order.getOrderStatus());
        assertNull(order.getCreatedAt());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyOrder() {
        // Act
        Orders order = new Orders();

        // Assert
        assertNull(order.getOrderId());
        assertNull(order.getOrderNumber());
        assertNull(order.getUserId());
        assertNull(order.getTotalAmount());
        assertNull(order.getDiscountAmount());
        assertNull(order.getFinalAmount());
        assertNull(order.getOrderStatus());
        assertNull(order.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldCreateOrderWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String orderId = "ORD-003";
        String orderNumber = "ON-003";
        String userId = "USER-003";
        BigDecimal totalAmount = BigDecimal.valueOf(200.00);
        BigDecimal discountAmount = BigDecimal.valueOf(20.00);
        BigDecimal finalAmount = BigDecimal.valueOf(180.00);
        OrderStatus status = OrderStatus.COMPLETED;
        String addressId = "ADDR-001";

        // Act
        Orders order = new Orders(orderId, orderNumber, userId, totalAmount, discountAmount, finalAmount, status, now, addressId);

        // Assert
        assertEquals(orderId, order.getOrderId());
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(userId, order.getUserId());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(discountAmount, order.getDiscountAmount());
        assertEquals(finalAmount, order.getFinalAmount());
        assertEquals(status, order.getOrderStatus());
        assertEquals(now, order.getCreatedAt());
        assertEquals(addressId, order.getAddressId());
    }

    @Test
    void getItems_shouldThrowUnsupportedOperationException() {
        // Arrange
        Orders order = new Orders();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            order.getItems();
        });
    }

    @Test
    void setterAndGetter_shouldUpdateFieldsCorrectly() {
        // Arrange
        Orders order = new Orders();
        LocalDateTime now = LocalDateTime.now();

        // Act
        order.setOrderId("ORD-004");
        order.setOrderNumber("ON-004");
        order.setUserId("USER-004");
        order.setTotalAmount(BigDecimal.valueOf(300.00));
        order.setDiscountAmount(BigDecimal.valueOf(30.00));
        order.setFinalAmount(BigDecimal.valueOf(270.00));
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setCreatedAt(now);

        // Assert
        assertEquals("ORD-004", order.getOrderId());
        assertEquals("ON-004", order.getOrderNumber());
        assertEquals("USER-004", order.getUserId());
        assertEquals(BigDecimal.valueOf(300.00), order.getTotalAmount());
        assertEquals(BigDecimal.valueOf(30.00), order.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(270.00), order.getFinalAmount());
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        assertEquals(now, order.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode_shouldWork() {
        // Arrange
        Orders order1 = Orders.builder()
                .OrderId("ORD-005")
                .orderNumber("ON-005")
                .userId("USER-005")
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();

        Orders order3 = Orders.builder()
                .OrderId("ORD-006")
                .orderNumber("ON-006")
                .userId("USER-006")
                .build();

        // Assert
        assertNotEquals(order1, order3);
        assertNotEquals(order1.hashCode(), order3.hashCode());
    }
}
