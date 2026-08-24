package com.EComMicroService.OrdersServices.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;
import com.EComMicroService.OrdersServices.Enums.EventStatus;
import com.EComMicroService.OrdersServices.Enums.EventType;
import com.EComMicroService.OrdersServices.KafkaEvents.Events;
import com.EComMicroService.OrdersServices.Repositorys.OrderEventRepository;

@DataJpaTest
@ActiveProfiles("test")
class OrderEventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderEventRepository orderEventRepository;

    private final String ORDER_ID_1 = "ord-001";
    private final String ORDER_ID_2 = "ord-002";
    private final String USER_ID = "user-001";

    private OrdersEventsLog event1;
    private OrdersEventsLog event2;

    @BeforeEach
    void setUp() {
        Events events1 = Events.builder()
                .orderId(ORDER_ID_1)
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        Events events2 = Events.builder()
                .orderId(ORDER_ID_2)
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        event1 = OrdersEventsLog.builder()
                .orderId(ORDER_ID_1)
                .event(events1)
                .published(EventStatus.PENDING)
                .build();

        event2 = OrdersEventsLog.builder()
                .orderId(ORDER_ID_2)
                .event(events2)
                .published(EventStatus.PENDING)
                .build();
    }

    // ==================== Save and Find Tests ====================

    @Test
    void save_shouldPersistEvent_whenEventIsValid() {
        // Act
        OrdersEventsLog savedEvent = orderEventRepository.save(event1);

        // Assert
        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getOrderEventId());
        assertEquals(ORDER_ID_1, savedEvent.getOrderId());
        assertEquals(EventStatus.PENDING, savedEvent.getPublished());
        assertNotNull(savedEvent.getEvent());
        assertEquals(USER_ID, savedEvent.getEvent().getUserId());
    }

    @Test
    void findById_shouldReturnEvent_whenEventExists() {
        // Arrange
        OrdersEventsLog savedEvent = entityManager.persist(event1);
        entityManager.flush();

        // Act
        Optional<OrdersEventsLog> foundEvent = orderEventRepository.findById(savedEvent.getOrderEventId());

        // Assert
        assertThat(foundEvent).isPresent();
        assertEquals(ORDER_ID_1, foundEvent.get().getOrderId());
        assertEquals(USER_ID, foundEvent.get().getEvent().getUserId());
    }

    @Test
    void findById_shouldReturnEmpty_whenEventDoesNotExist() {
        // Act
        Optional<OrdersEventsLog> foundEvent = orderEventRepository.findById(999L);

        // Assert
        assertThat(foundEvent).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllEvents() {
        // Arrange
        entityManager.persist(event1);
        entityManager.persist(event2);
        entityManager.flush();

        // Act
        List<OrdersEventsLog> events = orderEventRepository.findAll();

        // Assert
        assertNotNull(events);
        assertEquals(2, events.size());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoEvents() {
        // Act
        List<OrdersEventsLog> events = orderEventRepository.findAll();

        // Assert
        assertNotNull(events);
        assertTrue(events.isEmpty());
    }

    // ==================== findAllNonPublishedEvent Tests ====================

    @Test
    void findAllNonPublishedEvent_shouldReturnAllPendingEvents() {
        // Arrange
        Events eventsPublished = Events.builder()
                .orderId("ord-003")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog publishedEvent = OrdersEventsLog.builder()
                .orderId("ord-003")
                .event(eventsPublished)
                .published(EventStatus.PUBLISHED)
                .build();

        entityManager.persist(event1);
        entityManager.persist(event2);
        entityManager.persist(publishedEvent);
        entityManager.flush();

        // Act
        List<OrdersEventsLog> pendingEvents = orderEventRepository.findAllNonPublishedEvent();

        // Assert
        assertNotNull(pendingEvents);
        assertEquals(2, pendingEvents.size());
        assertTrue(pendingEvents.stream().allMatch(e -> EventStatus.PENDING.equals(e.getPublished())));
        assertTrue(pendingEvents.stream().anyMatch(e -> ORDER_ID_1.equals(e.getOrderId())));
        assertTrue(pendingEvents.stream().anyMatch(e -> ORDER_ID_2.equals(e.getOrderId())));
    }

    @Test
    void findAllNonPublishedEvent_shouldReturnEmptyList_whenNoPendingEvents() {
        // Arrange
        Events events1 = Events.builder()
                .orderId("ord-004")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        Events events2 = Events.builder()
                .orderId("ord-005")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog publishedEvent1 = OrdersEventsLog.builder()
                .orderId("ord-004")
                .event(events1)
                .published(EventStatus.PUBLISHED)
                .build();

        OrdersEventsLog publishedEvent2 = OrdersEventsLog.builder()
                .orderId("ord-005")
                .event(events2)
                .published(EventStatus.PUBLISHED)
                .build();

        entityManager.persist(publishedEvent1);
        entityManager.persist(publishedEvent2);
        entityManager.flush();

        // Act
        List<OrdersEventsLog> pendingEvents = orderEventRepository.findAllNonPublishedEvent();

        // Assert
        assertNotNull(pendingEvents);
        assertTrue(pendingEvents.isEmpty());
    }

    @Test
    void findAllNonPublishedEvent_shouldReturnEmptyList_whenNoEventsExist() {
        // Act
        List<OrdersEventsLog> pendingEvents = orderEventRepository.findAllNonPublishedEvent();

        // Assert
        assertNotNull(pendingEvents);
        assertTrue(pendingEvents.isEmpty());
    }

    @Test
    void findAllNonPublishedEvent_shouldReturnOnlyPendingEvents_whenMixedStatuses() {
        // Arrange
        Events eventsPending1 = Events.builder()
                .orderId("ord-006")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        Events eventsPublished = Events.builder()
                .orderId("ord-007")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        Events eventsPending2 = Events.builder()
                .orderId("ord-008")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog pendingEvent1 = OrdersEventsLog.builder()
                .orderId("ord-006")
                .event(eventsPending1)
                .published(EventStatus.PENDING)
                .build();

        OrdersEventsLog publishedEvent = OrdersEventsLog.builder()
                .orderId("ord-007")
                .event(eventsPublished)
                .published(EventStatus.PUBLISHED)
                .build();

        OrdersEventsLog pendingEvent2 = OrdersEventsLog.builder()
                .orderId("ord-008")
                .event(eventsPending2)
                .published(EventStatus.PENDING)
                .build();

        entityManager.persist(pendingEvent1);
        entityManager.persist(publishedEvent);
        entityManager.persist(pendingEvent2);
        entityManager.flush();

        // Act
        List<OrdersEventsLog> pendingEvents = orderEventRepository.findAllNonPublishedEvent();

        // Assert
        assertNotNull(pendingEvents);
        assertEquals(2, pendingEvents.size());
        assertTrue(pendingEvents.stream().allMatch(e -> EventStatus.PENDING.equals(e.getPublished())));
        assertTrue(pendingEvents.stream().anyMatch(e -> "ord-006".equals(e.getOrderId())));
        assertTrue(pendingEvents.stream().anyMatch(e -> "ord-008".equals(e.getOrderId())));
        assertFalse(pendingEvents.stream().anyMatch(e -> "ord-007".equals(e.getOrderId())));
    }

    // ==================== Delete Tests ====================

    @Test
    void delete_shouldRemoveEvent_whenEventExists() {
        // Arrange
        OrdersEventsLog savedEvent = entityManager.persist(event1);
        entityManager.flush();

        // Act
        orderEventRepository.deleteById(savedEvent.getOrderEventId());
        entityManager.flush();

        // Assert
        Optional<OrdersEventsLog> foundEvent = orderEventRepository.findById(savedEvent.getOrderEventId());
        assertThat(foundEvent).isEmpty();
    }

    @Test
    void delete_shouldNotThrow_whenEventDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> orderEventRepository.deleteById(999L));
    }

    // ==================== Update Tests ====================

    @Test
    void update_shouldModifyEventStatus_whenEventExists() {
        // Arrange
        OrdersEventsLog savedEvent = entityManager.persist(event1);
        entityManager.flush();

        // Act
        savedEvent.setPublished(EventStatus.PUBLISHED);
        Events updatedEvents = savedEvent.getEvent();
        updatedEvents.setOrderNumber("UPDATED-001");
        savedEvent.setEvent(updatedEvents);
        OrdersEventsLog updatedEvent = orderEventRepository.save(savedEvent);
        entityManager.flush();

        // Assert
        assertEquals(EventStatus.PUBLISHED, updatedEvent.getPublished());
        assertEquals("UPDATED-001", updatedEvent.getEvent().getOrderNumber());
    }

    // ==================== Edge Cases ====================

    @Test
    void save_shouldHandleEventWithComplexEventsObject() {
        // Arrange
        Events complexEvent = Events.builder()
                .orderId("ord-009")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .orderNumber("ORD-009")
                .total(5000L)
                .discountAmount(500L)
                .build();

        OrdersEventsLog complexLog = OrdersEventsLog.builder()
                .orderId("ord-009")
                .event(complexEvent)
                .published(EventStatus.PENDING)
                .build();

        // Act
        OrdersEventsLog savedEvent = orderEventRepository.save(complexLog);

        // Assert
        assertNotNull(savedEvent);
        assertEquals("ord-009", savedEvent.getOrderId());
        assertEquals(USER_ID, savedEvent.getEvent().getUserId());
        assertEquals(EventStatus.PENDING, savedEvent.getPublished());
        assertEquals(5000L, savedEvent.getEvent().getTotal());
        assertEquals(500L, savedEvent.getEvent().getDiscountAmount());
    }

    @Test
    void save_shouldHandleEventWithMinimalFields() {
        // Arrange
        Events minimalEvent = Events.builder()
                .orderId("ord-010")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog minimalLog = OrdersEventsLog.builder()
                .orderId("ord-010")
                .event(minimalEvent)
                .build();

        // Act
        OrdersEventsLog savedEvent = orderEventRepository.save(minimalLog);

        // Assert
        assertNotNull(savedEvent);
        assertEquals("ord-010", savedEvent.getOrderId());
        assertNull(savedEvent.getPublished());
    }

    @Test
    void findAllNonPublishedEvent_shouldReturnAllPendingEvents_whenMultiplePendingExist() {
        // Arrange - Create multiple pending events
        Events events1 = Events.builder()
                .orderId("ord-011")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        Events events2 = Events.builder()
                .orderId("ord-012")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog pendingEvent1 = OrdersEventsLog.builder()
                .orderId("ord-011")
                .event(events1)
                .published(EventStatus.PENDING)
                .build();

        OrdersEventsLog pendingEvent2 = OrdersEventsLog.builder()
                .orderId("ord-012")
                .event(events2)
                .published(EventStatus.PENDING)
                .build();

        entityManager.persist(pendingEvent1);
        entityManager.persist(pendingEvent2);
        entityManager.flush();

        // Act
        List<OrdersEventsLog> pendingEvents = orderEventRepository.findAllNonPublishedEvent();

        // Assert
        assertNotNull(pendingEvents);
        assertEquals(2, pendingEvents.size());
        assertTrue(pendingEvents.stream().allMatch(e -> EventStatus.PENDING.equals(e.getPublished())));
    }

    @Test
    void save_shouldHandleEventWithPublishedStatus() {
        // Arrange
        Events eventsPublished = Events.builder()
                .orderId("ord-013")
                .userId(USER_ID)
                .eventType(EventType.ORDER_PENDING)
                .build();

        OrdersEventsLog publishedLog = OrdersEventsLog.builder()
                .orderId("ord-013")
                .event(eventsPublished)
                .published(EventStatus.PUBLISHED)
                .build();

        // Act
        OrdersEventsLog savedEvent = orderEventRepository.save(publishedLog);

        // Assert
        assertNotNull(savedEvent);
        assertEquals(EventStatus.PUBLISHED, savedEvent.getPublished());
        assertNotNull(savedEvent.getEvent());
    }
}
