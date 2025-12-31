package com.EComMicroService.OrdersServices.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;
import com.EComMicroService.OrdersServices.Repositorys.OrderRepository;
import com.EComMicroService.OrdersServices.Services.OrderEventService;
import com.EComMicroService.OrdersServices.Services.OrderServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventService orderEventService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrdersDTO ordersDTO;
    private Orders order;

    @BeforeEach
    void setup() {
        ordersDTO = OrdersDTO.builder()
        .userId("USER-1")
        .totalAmount(BigDecimal.valueOf(500))
        .build();

        order =  Orders.builder()
        .OrderId("ORD-1")
        .userId("USER-1") 
        .orderStatus(OrderStatus.CREATED)  
        .build();
    }

    // -------------------- createOrder --------------------

    @Test
    void createOrder_shouldSaveOrderAndPublishEvent() throws JsonProcessingException {
        when(orderRepository.save(any(Orders.class)))
                .thenReturn(order);

        String orderId = orderService.createOrder(ordersDTO);

        assertEquals("ORD-1", orderId);
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(orderEventService, times(1))
                .saveOrderEvent(ordersDTO);
    }

    // -------------------- getOrdersByUserId --------------------

    @Test
    void getOrdersByUserId_shouldReturnOrders() {
        when(orderRepository.findAllByUserId("USER-1"))
                .thenReturn(List.of(order));

        List<Orders> result =
                orderService.getOrdersByUserId("USER-1");

        assertEquals(1, result.size());
        verify(orderRepository, times(1))
                .findAllByUserId("USER-1");
    }

    // -------------------- listOrdersForUser --------------------

    @Test
    void listOrdersForUser_shouldReturnOrders() {
        when(orderRepository.findAllByUserId("USER-1"))
                .thenReturn(List.of(order));

        Object result =
                orderService.listOrdersForUser("USER-1");

        assertNotNull(result);
        verify(orderRepository, times(1))
                .findAllByUserId("USER-1");
    }

    // -------------------- getOrderDetails --------------------

    @Test
    void getOrderDetails_shouldReturnOrder_whenFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.of(order));

        Object result =
                orderService.getOrderDetails("ORD-1");

        assertNotNull(result);
        verify(orderRepository, times(1))
                .findById("ORD-1");
    }

    @Test
    void getOrderDetails_shouldReturnNull_whenNotFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.empty());

        Object result =
                orderService.getOrderDetails("ORD-1");

        assertNull(result);
    }

    // -------------------- cancelOrder --------------------

    @Test
    void cancelOrder_shouldReturnFalse_whenOrderNotFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.empty());

        Boolean result =
                orderService.cancelOrder("ORD-1");

        assertFalse(result);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_shouldCancelOrder_whenNotDelivered() {
        order.setOrderStatus(OrderStatus.CREATED);

        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.of(order));
        when(orderRepository.save(order))
                .thenReturn(order);

        Boolean result =
                orderService.cancelOrder("ORD-1");

        assertTrue(result);
        assertEquals(OrderStatus.CANCELED,
                order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void cancelOrder_shouldReturnFalse_whenOrderDelivered() {
        order.setOrderStatus(OrderStatus.DELEVERED);

        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.of(order));

        Boolean result =
                orderService.cancelOrder("ORD-1");

        assertFalse(result);
        verify(orderRepository, never()).save(any());
    }

    // -------------------- deleteOrder --------------------

    @Test
    void deleteOrder_shouldReturnFalse_whenOrderNotFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.empty());

        Boolean result =
                orderService.deleteOrder("ORD-1");

        assertFalse(result);
        verify(orderRepository, never()).delete(any());
    }

    @Test
    void deleteOrder_shouldDeleteOrder_whenFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.of(order));

        Boolean result =
                orderService.deleteOrder("ORD-1");

        assertTrue(result);
        verify(orderRepository, times(1)).delete(order);
    }

    // -------------------- updateOrderStatus --------------------

    @Test
    void updateOrderStatus_shouldUpdateStatus_whenOrderFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.of(order));
        when(orderRepository.save(order))
                .thenReturn(order);

        Orders updatedOrder =
                orderService.updateOrderStatus("ORD-1",
                        OrderStatus.SHIPPED);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.SHIPPED,
                updatedOrder.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenOrderNotFound() {
        when(orderRepository.findById("ORD-1"))
                .thenReturn(Optional.empty());

        Orders result =
                orderService.updateOrderStatus("ORD-1",
                        OrderStatus.SHIPPED);

        assertNull(result);
        verify(orderRepository, never()).save(any());
    }
}
