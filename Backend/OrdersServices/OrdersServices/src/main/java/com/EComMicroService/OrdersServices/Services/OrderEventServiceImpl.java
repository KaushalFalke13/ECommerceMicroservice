package com.EComMicroService.OrdersServices.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;
import com.EComMicroService.OrdersServices.Enums.EventStatus;
import com.EComMicroService.OrdersServices.Enums.EventType;
import com.EComMicroService.OrdersServices.KafkaEvents.Events;
import com.EComMicroService.OrdersServices.Repositorys.OrderEventRepository;

@Service
public class OrderEventServiceImpl implements OrderEventService {

    private final OrderEventRepository orderEventRepository;

    public OrderEventServiceImpl(OrderEventRepository orderEventRepository) {
        this.orderEventRepository = orderEventRepository;
    }

    
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveOrderEvent(OrdersDTO ordersDTO) {
        Events event = Events.builder()
                .orderId(ordersDTO.getOrderId())
                .eventType(EventType.ORDER_PENDING)
                // .shippingAddress(ordersDTO.())
                .discountAmount(ordersDTO.getDiscountAmount().longValue())
                .userId(ordersDTO.getUserId())
                .items(ordersDTO.getItems())
                .total(ordersDTO.getTotalAmount().longValue())
                .build();

        OrdersEventsLog ordersEvents = OrdersEventsLog.builder()
                .orderId(ordersDTO.getOrderId())
                .event(event)
                .published(EventStatus.PENDING)
                .build();
        orderEventRepository.save(ordersEvents);
    }

    @Override
    public List<OrdersEventsLog> getUnpublishedEvents() {
        return orderEventRepository.findAllNonPublishedEvent();
    }

    @Override
    public boolean UpdateOrderEventsLog(OrdersEventsLog eventsLog) {
        try {
            orderEventRepository.save(eventsLog);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
