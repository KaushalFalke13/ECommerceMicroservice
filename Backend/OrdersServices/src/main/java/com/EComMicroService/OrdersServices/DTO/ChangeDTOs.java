package com.EComMicroService.OrdersServices.DTO;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.Entity.Address;
import com.EComMicroService.OrdersServices.Entity.Orders;
import com.EComMicroService.OrdersServices.Enums.OrderStatus;

@Service
public class ChangeDTOs {

    public Address changeDTOtoAddress(AddressDTO address){
         return Address.builder()
                .id(address.getId())
                .name(address.getName())
                .number(address.getNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .isDefault(address.isDefault())
                .type(address.getType())
                .userId(address.getUserId())
                .build();
    }

    public AddressDTO changAddressToDTO(Address address){
        return AddressDTO.builder()
                .Id(address.getId())
                .name(address.getName())
                .number(address.getNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .isDefault(address.isDefault())
                .type(address.getType())
                .userId(address.getUserId())
                .build();
    }

    public  Orders changeDTOtoOrders(OrdersDTO dto) {
        return Orders.builder()
                .OrderId(UUID.randomUUID().toString())
                .userId(dto.getUserId())
                .orderNumber(UUID.randomUUID().toString())
                .totalAmount(dto.getTotalAmount())
                .discountAmount(dto.getDiscountAmount())
                .finalAmount(dto.getTotalAmount())
                .orderStatus(OrderStatus.CREATED)
                .build();
    }

    public  OrdersDTO changeOrdersToDto(Orders orders) {
        OrdersDTO ordersDTO = OrdersDTO.builder()
                // .orderId(orders.getOrderId())
                .build();
        return ordersDTO;
    }

    public  OrdersDTO changeEvtToDto(String orderId, String orders) {
        OrdersDTO ordersDTO = OrdersDTO.builder()
                .build();
        return ordersDTO;
    }
}
