package com.EComMicroService.ProductsServices.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @OneToMany(mappedBy = "bag", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BagItem> items = new ArrayList<>();

    public void addItem(BagItem item) {
        items.add(item);
        item.setBag(this);
    }

    public void removeItem(BagItem item) {
        items.remove(item);
        item.setBag(null);
    }
}
