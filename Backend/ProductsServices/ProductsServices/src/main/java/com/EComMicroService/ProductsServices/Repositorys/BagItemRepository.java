package com.EComMicroService.ProductsServices.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EComMicroService.ProductsServices.Entity.Bag;
import com.EComMicroService.ProductsServices.Entity.BagItem;

public interface BagItemRepository extends JpaRepository<BagItem, Long> {

    BagItem findByBagAndProductId(Bag bag, String productId);

}
