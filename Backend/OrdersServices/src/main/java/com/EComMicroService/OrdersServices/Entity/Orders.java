package com.EComMicroService.OrdersServices.Entity;

import java.math.BigDecimal;

import com.EComMicroService.OrdersServices.Enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    private String OrderId;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String userId;

    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // PAYMENT
    // @Enumerated(EnumType.STRING)
    // private PaymentStatus paymentStatus;

    // @Enumerated(EnumType.STRING)
    // private PaymentMethod paymentMethod;


    // @Column(columnDefinition = "TEXT")
    // private String shippingAddressJson;

    // private Long addressId;

    // private String trackingNumber;
    // private String carrier;
    // private LocalDateTime estimatedDeliveryDate;
    // private LocalDateTime deliveredAt;
    // private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // private List<OrderItem> items;
    
    // private BigDecimal taxAmount;
    // private BigDecimal shippingFee;

}
