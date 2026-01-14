package com.EComMicroService.ProductsServices.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bags")
@Getter
@Setter
public class Bag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @OneToMany(
        mappedBy = "bag",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<BagItem> items = new ArrayList<>();

    // convenience methods
    public void addItem(BagItem item) {
        items.add(item);
        item.setBag(this);
    }

    public void removeItem(BagItem item) {
        items.remove(item);
        item.setBag(null);
    }

}
