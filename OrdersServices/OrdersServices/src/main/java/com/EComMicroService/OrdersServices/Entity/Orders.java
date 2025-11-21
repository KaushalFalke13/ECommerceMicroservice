package com.EComMicroService.OrdersServices.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private Long userId;  // from Auth service

    // AMOUNTS
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingFee;
    private BigDecimal finalAmount;

    // PAYMENT
    // @Enumerated(EnumType.STRING)
    // private PaymentStatus paymentStatus;

    // @Enumerated(EnumType.STRING)
    // private PaymentMethod paymentMethod;

    // ORDER STATUS
    // @Enumerated(EnumType.STRING)
    // private OrderStatus orderStatus;

    @Column(columnDefinition = "TEXT")
    private String shippingAddressJson;

    private Long addressId; 

    private String trackingNumber;
    private String carrier;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // private List<OrderItem> items;

    
}
