package com.EComMicroService.ProductsServices.Repositorys;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import com.EComMicroService.ProductsServices.Entity.Bag;
import com.EComMicroService.ProductsServices.Entity.BagItem;
import jakarta.persistence.LockModeType;

public interface BagItemRepository extends JpaRepository<BagItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BagItem> findByBagAndProductId(Bag bag, String productId);

    void deleteByBagAndProductId(Bag bag, String productId);
    List<BagItem> findAllByBag(Bag bag);
}
