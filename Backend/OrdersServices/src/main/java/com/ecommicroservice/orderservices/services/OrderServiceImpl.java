package com.ecommicroservice.orderservices.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommicroservice.orderservices.client.ProductsServiceClient;
import com.ecommicroservice.orderservices.dto.ApiResponse;
import com.ecommicroservice.orderservices.dto.BagItemDTO;
import com.ecommicroservice.orderservices.dto.ChangeDTOs;
import com.ecommicroservice.orderservices.dto.OrdersDTO;
import com.ecommicroservice.orderservices.entity.Orders;
import com.ecommicroservice.orderservices.enums.OrderStatus;
import com.ecommicroservice.orderservices.repositorys.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    // private final ObjectMapper mapper = new ObjectMapper();
    private final ChangeDTOs changeDTOs;
    private final OrderRepository orderRepository;
    private final OrderEventService orderEventService;
    private final ProductsServiceClient productsServiceClient;
    private final NotificationService notificationService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderEventService orderEventService,
            ChangeDTOs changeDTOs, ProductsServiceClient productsServiceClient,
            NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderEventService = orderEventService;
        this.changeDTOs = changeDTOs;
        this.productsServiceClient = productsServiceClient;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public String createOrder(OrdersDTO order, String authHeader) throws JsonProcessingException {
        // Validate bag items from Products service
        ApiResponse<List<BagItemDTO>> bagResponse = productsServiceClient.getBagItems(authHeader);
        if (bagResponse == null || bagResponse.getData() == null || bagResponse.getData().isEmpty()) {
            throw new RuntimeException("Bag is empty or invalid");
        }

        // Calculate total from bag items
        Float bagTotal = bagResponse.getData().stream()
                .map(BagItemDTO::getTotalPrice)
                .reduce(0f, Float::sum);

        // Set order total and items
        order.setTotalAmount(bagTotal);
        order.setItems(bagResponse.getData().stream()
                .collect(Collectors.toMap(BagItemDTO::getProductId, BagItemDTO::getQuantity)));

        Orders newOrder = changeDTOs.changeDTOtoOrders(order);
        newOrder.setOrderStatus(OrderStatus.PAYMENT_PENDING);
        String orderId = orderRepository.save(newOrder).getOrderId();

        // Publish order created event
        orderEventService.saveOrderEvent(order);

        // Clear the bag after successful order creation
        productsServiceClient.clearBag(authHeader);

        // Send order confirmation notification
        notificationService.sendOrderConfirmation(newOrder, order.getUserEmail());

        return orderId;
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Object listOrdersForUser(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Object getOrderDetails(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public Boolean cancelOrder(String orderId) throws JsonProcessingException {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        if (!order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            // Publish cancellation event to release inventory
            orderEventService.saveOrderEvent(changeDTOs.changeOrdersToDto(order));
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteOrder(String orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        } else {
            orderRepository.delete(order);
            return true;
        }
    }

    @Override
    public Orders updateOrderStatus(String orderId, OrderStatus status) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setOrderStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean updateOrderAddress(String orderId, String addressId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        order.setAddressId(addressId);
        orderRepository.save(order);
        return true;
    }
}
