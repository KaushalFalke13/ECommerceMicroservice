package com.EComMicroService.OrdersServices.Controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EComMicroService.OrdersServices.DTO.AddressDTO;
import com.EComMicroService.OrdersServices.DTO.ApiResponse;
import com.EComMicroService.OrdersServices.DTO.OrdersDTO;
import com.EComMicroService.OrdersServices.Services.AddressService;
import com.EComMicroService.OrdersServices.Services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final AddressService addressService;

    public OrdersController(OrderService orderService, AddressService addressService) {
        this.orderService = orderService;
        this.addressService = addressService;
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getOrderStatus() {

        return ResponseEntity.ok(new ApiResponse<>(200, "Order service is up and running!"));
    }

    // @PatchMapping("/updateAddress")
    // public ResponseEntity<ApiResponse<Void>> updateAddress(
    // @RequestParam @NotBlank(message = "orderId is required") String orderId,
    // @RequestParam @NotBlank(message = "address is required") String newAddress) {
    // // boolean updated = orderService(orderId, newAddress);
    // boolean updated = true;
    // if (updated) {
    // return ResponseEntity.ok(new ApiResponse<>(200, "Order address updated
    // successfully"));
    // } else {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    // .body(new ApiResponse<>(400, "Failed to update order address"));
    // }
    // }

    @PostMapping("/addNewAddress")
    public ResponseEntity<ApiResponse<String>> addNewAddress(@RequestBody AddressDTO addressDTO,
            @RequestHeader("Authorization") String authHeader) {
        addressService.addAddress(addressDTO, authHeader);
        return ResponseEntity.ok(new ApiResponse<>(200, "Address Created Sucessfully"));
    }

    @PostMapping("/removeAddress")
    public ResponseEntity<ApiResponse<String>> removeAddress(@RequestBody Long addressId) {
        addressService.removeAddress(addressId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Address Removed Sucessfully"));
    }

    @GetMapping("/address")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAddress(@RequestHeader("Authorization") String authHeader) {
        List<AddressDTO> addresses = addressService.getAddressesByUserId(authHeader);
        return ResponseEntity.ok(new ApiResponse<>(200, " ", addresses));
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<String>> createOrder(@RequestHeader("Authorization") String authHeader ,@Valid @RequestBody OrdersDTO orderDTO)
            throws JsonProcessingException {
                
        String createdOrderId = orderService.createOrder(orderDTO);
        if (createdOrderId != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Order created successfully", createdOrderId));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to create order"));
        }
    }

    @GetMapping("/listOrders")
    public ResponseEntity<ApiResponse<Object>> listOrders(
            @RequestParam @NotBlank(message = "userId is required") String userId) {

        Object orders = orderService.listOrdersForUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Orders fetched successfully", orders));
    }

    @GetMapping("/orderDetails")
    public ResponseEntity<ApiResponse<Object>> getOrderDetails(
            @RequestParam @NotBlank(message = "orderId is required") String orderId) {

        Object details = orderService.getOrderDetails(orderId);
        if (details != null) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Order details fetched", details));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "Order not found"));
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @RequestParam @NotBlank(message = "orderId is required") String orderId) {

        boolean cancelled = orderService.cancelOrder(orderId);
        if (cancelled) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Order canceled successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Failed to cancel order"));
        }
    }

}