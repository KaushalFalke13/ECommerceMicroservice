package com.EComMicroService.ProductsServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EComMicroService.ProductsServices.Entity.Bag;
import com.EComMicroService.ProductsServices.Entity.BagItem;
import com.EComMicroService.ProductsServices.Repositorys.BagItemRepository;
import com.EComMicroService.ProductsServices.Repositorys.BagRepository;

@Service
@Transactional
public class BagServiceImpl implements BagService {

    public BagServiceImpl(BagRepository bagRepository, BagItemRepository bagItemRepository) {
        this.bagRepository = bagRepository;
        this.bagItemRepository = bagItemRepository;
    }

    private final BagRepository bagRepository;
    private final BagItemRepository bagItemRepository;

    @Override
    public int addItem(String userId, String productId) {

        Bag bag = bagRepository.findByUserId(userId)
                .orElseGet(() -> bagRepository.save(
                        Bag.builder()
                           .userId(userId)
                           .build()
                ));

                BagItem item = bagItemRepository
                .findByBagAndProductId(bag, productId)
                .orElseGet(() -> {
                    BagItem newItem = BagItem.builder()
                            .bag(bag)         
                            .productId(productId)
                            .quantity(0)
                            .build();
                    bag.addItem(newItem);  
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + 1);   
        bagRepository.save(bag);
        return bagItemRepository.findAllByBag(bag).size();
     }

    @Override
    public void removeItem(String userId, String productId) {
        // bagRepository.removeItemFromBag(userId, productId);
    }

    @Override
    public List<String> getItems(String userId) {
        // return bagRepository.getItemsInBag(userId);
        return null;
    }

}
