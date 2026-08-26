package com.ecommicroservice.orderservices.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ecommicroservice.orderservices.enums.OrderStatus;

class OrdersTest {

    @Test
    void builder_shouldCreateOrderWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String orderId = "ORD-001";
        String orderNumber = "ON-001";
        String userId = "USER-001";
        float totalAmount = 150.00f;
        float discountAmount = 10.00f;
        float finalAmount = 140.00f;
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
        assertEquals(totalAmount, order.getTotalAmount(), 0.001);
        assertEquals(discountAmount, order.getDiscountAmount(), 0.001);
        assertEquals(finalAmount, order.getFinalAmount(), 0.001);
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
        assertEquals(0.0f, order.getTotalAmount());
        assertEquals(0.0f, order.getDiscountAmount());
        assertEquals(0.0f, order.getFinalAmount());
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
        assertEquals(0.0f, order.getTotalAmount());
        assertEquals(0.0f, order.getDiscountAmount());
        assertEquals(0.0f, order.getFinalAmount());
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
        String userEmail = "user@example.com";
        float totalAmount = 200.00f;
        float discountAmount = 20.00f;
        float finalAmount = 180.00f;
        OrderStatus status = OrderStatus.COMPLETED;
        String addressId = "ADDR-001";

        // Act
        Orders order = new Orders(orderId, orderNumber, userId, userEmail, totalAmount, discountAmount, finalAmount,
                status, now, addressId);

        // Assert
        assertEquals(orderId, order.getOrderId());
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(userId, order.getUserId());
        assertEquals(userEmail, order.getUserEmail());
        assertEquals(totalAmount, order.getTotalAmount(), 0.001);
        assertEquals(discountAmount, order.getDiscountAmount(), 0.001);
        assertEquals(finalAmount, order.getFinalAmount(), 0.001);
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
        order.setTotalAmount(300.00f);
        order.setDiscountAmount(30.00f);
        order.setFinalAmount(270.00f);
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setCreatedAt(now);

        // Assert
        assertEquals("ORD-004", order.getOrderId());
        assertEquals("ON-004", order.getOrderNumber());
        assertEquals("USER-004", order.getUserId());
        assertEquals(300.00f, order.getTotalAmount(), 0.001);
        assertEquals(30.00f, order.getDiscountAmount(), 0.001);
        assertEquals(270.00f, order.getFinalAmount(), 0.001);
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
                .totalAmount(100.00f)
                .discountAmount(10.00f)
                .finalAmount(90.00f)
                .build();

        Orders order3 = Orders.builder()
                .OrderId("ORD-006")
                .orderNumber("ON-006")
                .userId("USER-006")
                .totalAmount(200.00f)
                .discountAmount(20.00f)
                .finalAmount(180.00f)
                .build();

        // Assert
        assertNotEquals(order1, order3);
        assertNotEquals(order1.hashCode(), order3.hashCode());
    }
}
