package com.EComMicroService.OrdersServices.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;
import com.EComMicroService.OrdersServices.Repositorys.OrderRepository;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private final String USER_ID_1 = "user-001";
    private final String USER_ID_2 = "user-002";
    private final String ORDER_ID_1 = "ord-001";
    private final String ORDER_ID_2 = "ord-002";

    private Orders order1;
    private Orders order2;

    @BeforeEach
    void setUp() {
        order1 = Orders.builder()
                .OrderId(ORDER_ID_1)
                .userId(USER_ID_1)
                .orderNumber("ORD-001")
                .totalAmount(150.00f)
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        order2 = Orders.builder()
                .OrderId(ORDER_ID_2)
                .userId(USER_ID_1)
                .orderNumber("ORD-002")
                .totalAmount(250.00f)
                .orderStatus(OrderStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ==================== Save and Find Tests ====================

    @Test
    void save_shouldPersistOrder_whenOrderIsValid() {
        // Act
        Orders savedOrder = orderRepository.save(order1);

        // Assert
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getOrderId());
        assertEquals(ORDER_ID_1, savedOrder.getOrderId());
        assertEquals(USER_ID_1, savedOrder.getUserId());
    }

    @Test
    void findById_shouldReturnOrder_whenOrderExists() {
        // Arrange
        Orders savedOrder = entityManager.persist(order1);
        entityManager.flush();

        // Act
        Optional<Orders> foundOrder = orderRepository.findById(savedOrder.getOrderId());

        // Assert
        assertThat(foundOrder).isPresent();
        assertEquals(ORDER_ID_1, foundOrder.get().getOrderId());
        assertEquals(USER_ID_1, foundOrder.get().getUserId());
    }

    @Test
    void findById_shouldReturnEmpty_whenOrderDoesNotExist() {
        // Act
        Optional<Orders> foundOrder = orderRepository.findById("999L");

        // Assert
        assertThat(foundOrder).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllOrders() {
        // Arrange
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();

        // Act
        List<Orders> orders = orderRepository.findAll();

        // Assert
        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoOrders() {
        // Act
        List<Orders> orders = orderRepository.findAll();

        // Assert
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    // ==================== findByUserId Tests ====================

    @Test
    void findAllByUserId_shouldReturnAllOrdersForUser_whenUserHasMultipleOrders() {
        // Arrange
        Orders order3 = Orders.builder()
                .OrderId("ord-003")
                .userId(USER_ID_1)
                .orderNumber("ORD-003")
                .totalAmount(100.00f)
                .orderStatus(OrderStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .build();

        Orders orderOtherUser = Orders.builder()
                .OrderId("ord-004")
                .userId(USER_ID_2)
                .orderNumber("ORD-004")
                .totalAmount(50.00f)
                .orderStatus(OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
        entityManager.persist(orderOtherUser);
        entityManager.flush();

        // Act
        List<Orders> userOrders = orderRepository.findAllByUserId(USER_ID_1);

        // Assert
        assertNotNull(userOrders);
        assertEquals(3, userOrders.size());
        assertTrue(userOrders.stream().allMatch(o -> USER_ID_1.equals(o.getUserId())));
    }

    @Test
    void findAllByUserId_shouldReturnEmptyList_whenUserHasNoOrders() {
        // Arrange
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();

        // Act
        List<Orders> userOrders = orderRepository.findAllByUserId("non-existent-user");

        // Assert
        assertNotNull(userOrders);
        assertTrue(userOrders.isEmpty());
    }

    @Test
    void findAllByUserId_shouldReturnEmptyList_whenUserIdIsNull() {
        // Act
        List<Orders> userOrders = orderRepository.findAllByUserId(null);

        // Assert
        assertNotNull(userOrders);
        assertTrue(userOrders.isEmpty());
    }

    @Test
    void findAllByUserId_shouldReturnOrdersWithDifferentStatuses() {
        // Arrange
        Orders orderDelivered = Orders.builder()
                .OrderId("ord-005")
                .userId(USER_ID_1)
                .orderNumber("ORD-005")
                .totalAmount(300.00f)
                .orderStatus(OrderStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build();

        Orders orderCanceled = Orders.builder()
                .OrderId("ord-006")
                .userId(USER_ID_1)
                .orderNumber("ORD-006")
                .totalAmount(75.00f)
                .orderStatus(OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();

        entityManager.persist(order1);
        entityManager.persist(orderDelivered);
        entityManager.persist(orderCanceled);
        entityManager.flush();

        // Act
        List<Orders> userOrders = orderRepository.findAllByUserId(USER_ID_1);

        // Assert
        assertNotNull(userOrders);
        assertEquals(3, userOrders.size());
        assertTrue(userOrders.stream().anyMatch(o -> OrderStatus.CREATED.equals(o.getOrderStatus())));
        assertTrue(userOrders.stream().anyMatch(o -> OrderStatus.DELIVERED.equals(o.getOrderStatus())));
        assertTrue(userOrders.stream().anyMatch(o -> OrderStatus.CANCELLED.equals(o.getOrderStatus())));
    }

    // ==================== Delete Tests ====================

    @Test
    void delete_shouldRemoveOrder_whenOrderExists() {
        // Arrange
        Orders savedOrder = entityManager.persist(order1);
        entityManager.flush();

        // Act
        orderRepository.deleteById(savedOrder.getOrderId());
        entityManager.flush();

        // Assert
        Optional<Orders> foundOrder = orderRepository.findById(savedOrder.getOrderId());
        assertThat(foundOrder).isEmpty();
    }

    @Test
    void delete_shouldNotThrow_whenOrderDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> orderRepository.deleteById("999L"));
    }

    @Test
    void deleteAllByUserId_shouldRemoveAllOrdersForUser() {
        // Arrange
        entityManager.persist(order1);
        entityManager.persist(order2);
        Orders orderOtherUser = Orders.builder()
                .OrderId("ord-007")
                .userId(USER_ID_2)
                .orderNumber("ORD-007")
                .totalAmount(200.00f)
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        entityManager.persist(orderOtherUser);
        entityManager.flush();

        // Act - delete all orders for USER_ID_1
        orderRepository.deleteAllById(Arrays.asList(order1.getOrderId(), order2.getOrderId()));
        entityManager.flush();

        // Assert
        List<Orders> remainingOrders = orderRepository.findAll();
        assertEquals(1, remainingOrders.size());
        assertEquals(USER_ID_2, remainingOrders.get(0).getUserId());
    }

    // ==================== Update Tests ====================

    @Test
    void update_shouldModifyOrder_whenOrderExists() {
        // Arrange
        Orders savedOrder = entityManager.persist(order1);
        entityManager.flush();

        // Act
        savedOrder.setOrderStatus(OrderStatus.COMPLETED);
        savedOrder.setTotalAmount(200.00f);
        Orders updatedOrder = orderRepository.save(savedOrder);
        entityManager.flush();

        // Assert
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getOrderStatus());
        assertEquals(200.00f, updatedOrder.getTotalAmount(), 0.001);
    }

    // ==================== Edge Cases ====================

    @Test
    void save_shouldHandleOrderWithAllFields() {
        // Arrange
        Orders fullOrder = Orders.builder()
                .OrderId("ord-008")
                .userId(USER_ID_1)
                .orderNumber("ORD-008")
                .totalAmount(999.99f)
                .orderStatus(OrderStatus.PAID)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        Orders savedOrder = orderRepository.save(fullOrder);

        // Assert
        assertNotNull(savedOrder);
        assertEquals("ord-008", savedOrder.getOrderId());
        assertEquals(OrderStatus.PAID, savedOrder.getOrderStatus());
        assertEquals(999.99f, savedOrder.getTotalAmount(), 0.001);
    }

    @Test
    void findAllByUserId_shouldHandleUserWithLargeNumberOfOrders() {
        // Arrange
        for (int i = 1; i <= 25; i++) {
            Orders order = Orders.builder()
                    .OrderId("ord-" + String.format("%03d", i))
                    .userId(USER_ID_1)
                    .orderNumber("ORD-" + String.format("%03d", i))
                    .totalAmount(i * 10.0f)
                    .orderStatus(OrderStatus.CREATED)
                    .createdAt(LocalDateTime.now())
                    .build();
            entityManager.persist(order);
        }
        entityManager.flush();

        // Act
        List<Orders> userOrders = orderRepository.findAllByUserId(USER_ID_1);

        // Assert
        assertNotNull(userOrders);
        assertEquals(25, userOrders.size());
    }

    @Test
    void save_shouldHandleOrderWithNullOptionalFields() {
        // Arrange
        Orders minimalOrder = Orders.builder()
                .OrderId("ord-009")
                .userId(USER_ID_1)
                .orderNumber("ORD-009")
                .totalAmount(0.0f)
                .orderStatus(OrderStatus.CREATED)
                .build();

        // Act
        Orders savedOrder = orderRepository.save(minimalOrder);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(0.0f, savedOrder.getTotalAmount(), 0.001);
        assertNull(savedOrder.getCreatedAt());
    }
}
