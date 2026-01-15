package com.EComMicroService.ProductsServices.Repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.EComMicroService.ProductsServices.Entity.Bag;


@Repository
public interface BagRepository extends JpaRepository<Bag, Long> {

    Optional<Bag> findByUserId(String userId);
    Bag save(Bag bag);
    
    // void addItemToBag(String userId, String productId);

    // void removeItemFromBag(String userId, String productId);

    // List<String> getItemsInBag(String userId);
}
