package com.EComMicroService.ProductsServices.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class products {

    
    @Id  
    private String productId;
    
    @Column(length = 1000)
    private String description;
    private String title;
    private Integer MRP;
    private float price;
    private Integer stock;
    private Integer discount;

    @Column(unique = true)
    private String slug;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Images images; 
    
    @Builder.Default
    private Integer returnPeriod = 7;
    

    // private String color;
    //   @Builder.Default
    //   @Enumerated(EnumType.STRING)
    //   private Status ProductStatus = Status.ACTIVE;
    
    //   @OneToOne(cascade = CascadeType.ALL)
    //   @JoinColumn(name = "rating_id")
    //   private rating rating;

    //   @ManyToOne(cascade = CascadeType.ALL)
    //   @JoinColumn(name = "brand_id") 
    //   private Brands brand; 

    //   @JsonManagedReference
    //   @ManyToOne(cascade = CascadeType.ALL)
    //   @JoinColumn(name = "cat_id")  
    //   private Categories category;

    //   @ManyToMany(cascade = {CascadeType.ALL, CascadeType.MERGE })
    //   @Builder.Default
    //   @JoinTable(name = "Products_Keywords",
    //   joinColumns = @JoinColumn(name = "Product_id"),
    //   inverseJoinColumns = @JoinColumn  (name="searchKeywords_id"))  
    //   private Set<searchKeywords> searchKeyword = new HashSet<>();
    
    //   @JsonManagedReference
    //   @OneToMany(mappedBy = "products")
    //   private Set<WatchListCart> watchListCarts;

    //   @JsonManagedReference
    //   @OneToMany(mappedBy = "products")  
    //   private Set<BagCart> bagCart;

    //   @OneToMany(mappedBy = "products") 
    //   private List<OrdersProducts> ordersProducts;



}
