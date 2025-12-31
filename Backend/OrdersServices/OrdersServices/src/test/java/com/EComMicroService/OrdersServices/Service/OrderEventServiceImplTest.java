package com.EComMicroService.OrdersServices.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;
import com.EComMicroService.OrdersServices.Enums.EventStatus;
import com.EComMicroService.OrdersServices.Enums.EventType;
import com.EComMicroService.OrdersServices.KafkaEvents.Events;
import com.EComMicroService.OrdersServices.Repositorys.OrderEventRepository;
import com.EComMicroService.OrdersServices.Services.OrderEventServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderEventServiceImplTest {

    @Mock
    private OrderEventRepository orderEventRepository;

    @InjectMocks
    private OrderEventServiceImpl orderEventService;

    private OrdersDTO ordersDTO;

    @BeforeEach
    void setup() {
        ordersDTO = OrdersDTO.builder()
                .orderId("1001L")
                .userId("2001L")
                .discountAmount(BigDecimal.valueOf(50))
                .totalAmount(BigDecimal.valueOf(500))
                .items(Collections.emptyMap())
                .build();
    }

    // -------------------- saveOrderEvent --------------------

    @Test
    void saveOrderEventTest() {
        ArgumentCaptor<OrdersEventsLog> captor = ArgumentCaptor.forClass(OrdersEventsLog.class);

        orderEventService.saveOrderEvent(ordersDTO);

        verify(orderEventRepository, times(1)).save(captor.capture());

        OrdersEventsLog savedEvent = captor.getValue();
        Events event = savedEvent.getEvent();

        assertNotNull(savedEvent);
        assertEquals(ordersDTO.getOrderId(), savedEvent.getOrderId());
        assertEquals(EventStatus.PENDING, savedEvent.getPublished());

        assertNotNull(event);
        assertEquals(EventType.ORDER_CREATED, event.getEventType());
        assertEquals(ordersDTO.getOrderId(), event.getOrderId());
        assertEquals(ordersDTO.getUserId(), event.getUserId());
        assertEquals(ordersDTO.getDiscountAmount().longValue(), event.getDiscountAmount());
        assertEquals(ordersDTO.getTotalAmount().longValue(), event.getTotal());
    }

    // -------------------- getUnpublishedEvents --------------------

    @Test
    void getUnpublishedEventsTest() {
        OrdersEventsLog log = OrdersEventsLog.builder()
                .orderId("1L")
                .published(EventStatus.PENDING)
                .build();

        when(orderEventRepository.findAllNonPublishedEvent())
                .thenReturn(List.of(log));

        List<OrdersEventsLog> result = orderEventService.getUnpublishedEvents();

        assertEquals(1, result.size());
        assertEquals(EventStatus.PENDING, result.get(0).getPublished());

        verify(orderEventRepository, times(1))
                .findAllNonPublishedEvent();
    }

    // -------------------- UpdateOrderEventsLog --------------------

    @Test
    void updateOrderEventsLogTest() {
        OrdersEventsLog log = new OrdersEventsLog();

        when(orderEventRepository.save(log)).thenReturn(log);

        boolean result = orderEventService.UpdateOrderEventsLog(log);

        assertTrue(result);
        verify(orderEventRepository, times(1)).save(log);
    }

    @Test
    void updateOrderEventsLogFailTest() {
        OrdersEventsLog log = new OrdersEventsLog();

        when(orderEventRepository.save(log))
                .thenThrow(new RuntimeException("DB error"));

        boolean result = orderEventService.UpdateOrderEventsLog(log);

        assertFalse(result);
        verify(orderEventRepository, times(1)).save(log);
    }
}
