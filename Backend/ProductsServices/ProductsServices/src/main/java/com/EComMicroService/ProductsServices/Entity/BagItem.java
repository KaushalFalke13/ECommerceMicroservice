package com.EComMicroService.ProductsServices.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bag_items", uniqueConstraints = @UniqueConstraint(columnNames = { "bag_id", "product_id" }))
public class BagItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bag_id", nullable = false)
    @JsonBackReference
    private Bag bag;

    @Column(name = "product_id", nullable = false)
    private String productId;

    private int quantity;

}
