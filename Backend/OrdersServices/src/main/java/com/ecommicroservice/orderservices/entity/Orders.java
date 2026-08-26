package com.ecommicroservice.orderservices.entity;

import java.time.LocalDateTime;

import com.ecommicroservice.orderservices.enums.OrderStatus;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    private String OrderId;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String userId;

    private String userEmail;

    private float totalAmount;
    private float discountAmount;
    private float finalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    public Object getItems() {
        throw new UnsupportedOperationException("Unimplemented method 'getItems'");
    }

    // PAYMENT
    // @Enumerated(EnumType.STRING)
    // private PaymentStatus paymentStatus;

    // @Enumerated(EnumType.STRING)
    // private PaymentMethod paymentMethod;

    // @Column(columnDefinition = "TEXT")
    // private String shippingAddressJson;

    private String addressId;

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
