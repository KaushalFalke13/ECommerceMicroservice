package com.EComMicroService.ProductsServices.Services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.EComMicroService.ProductsServices.Entity.Bag;
import com.EComMicroService.ProductsServices.Entity.BagItem;
import com.EComMicroService.ProductsServices.Repositorys.BagItemRepository;
import com.EComMicroService.ProductsServices.Repositorys.BagRepository;

public class BagServiceImpl implements BagService {

    public BagServiceImpl(BagRepository bagRepository, BagItemRepository bagItemRepository) {
        this.bagRepository = bagRepository;
        this.bagItemRepository = bagItemRepository;
    }

    private final BagRepository bagRepository;
    private final BagItemRepository bagItemRepository;

    @Override
    @Transactional
    public void addItem(String userId, String productId) {

    Bag bag = bagRepository.findByUserId(userId)
        .orElseGet(() -> {
            Bag b = new Bag();
            b.setUserId(userId);
            return bagRepository.save(b);
        });

    BagItem item = bagItemRepository
        .findByBagAndProductId(bag, productId)
        .orElse(null);

    if (item == null) {
        bag.addItem(
            BagItem.builder()
                .productId(productId)
                .quantity(1)
                .build()
        );
    } else {
        item.setQuantity(item.getQuantity() + 1);
    }

    bagRepository.save(bag);


    }

    @Override
    public void removeItem(String userId, String productId) {
        // bagRepository.removeItemFromBag(userId, productId);
    }   

    @Override
    public List<String> getItems(String userId) {
    //    return bagRepository.getItemsInBag(userId);
    return null;
    }

}
