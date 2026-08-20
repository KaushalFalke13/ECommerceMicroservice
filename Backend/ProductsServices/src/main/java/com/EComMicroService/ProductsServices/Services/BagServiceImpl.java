package com.EComMicroService.ProductsServices.Services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.EComMicroService.ProductsServices.Configuration.jwtUtil;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.Bag;
import com.EComMicroService.ProductsServices.Entity.BagItem;
import com.EComMicroService.ProductsServices.Entity.products;
import com.EComMicroService.ProductsServices.Repositorys.BagItemRepository;
import com.EComMicroService.ProductsServices.Repositorys.BagRepository;
import com.EComMicroService.ProductsServices.Repositorys.productRepository;

@Service
@Transactional
public class BagServiceImpl implements BagService {

    private final BagRepository bagRepository;
    private final BagItemRepository bagItemRepository;
    private final jwtUtil jwtUtil;
    private final helperServices helperService;
    private final productRepository productRepository;

    public BagServiceImpl(BagRepository bagRepository, BagItemRepository bagItemRepository,
            jwtUtil jwtUtil, productRepository productRepository, helperServices helperService) {
        this.bagRepository = bagRepository;
        this.bagItemRepository = bagItemRepository;
        this.jwtUtil = jwtUtil;
        this.helperService = helperService;
        this.productRepository = productRepository;
    }

    @Override
    public List<productDTO> addItem(String authHeader, String productId) {
        String userId = getUserIdFromAuthHeader(authHeader);

        Bag bag = bagRepository.findByUserId(userId)
                .orElseGet(() -> bagRepository.save(
                        Bag.builder()
                                .userId(userId)
                                .build()));

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

        return mapToProductDTOList(bagItemRepository.findAllByBag(bag));
    }

    @Override
    public List<productDTO> removeItem(String authHeader, String productId) {
        String userId = getUserIdFromAuthHeader(authHeader);
        List<BagItem> items = null;

        Bag bag = bagRepository.findByUserId(userId)
                .orElse(null);
        if (bag != null) {
            bagItemRepository.deleteByBagAndProductId(bag, productId);
            items = bagItemRepository.findAllByBag(bag);
        }

        return mapToProductDTOList(items);
    }

    @Override
    public List<productDTO> getItems(String authHeader) {
        String userId = getUserIdFromAuthHeader(authHeader);
        List<BagItem> items = null;
        Bag bag = bagRepository.findByUserId(userId)
                .orElse(null);
        if (bag != null) {
            items = bagItemRepository.findAllByBag(bag);
        }
        return mapToProductDTOList(items);
    }

    private List<productDTO> mapToProductDTOList(List<BagItem> items) {
        List<productDTO> bagitem = new ArrayList<>();
        for (BagItem item : items) {
            products product = productRepository.findById(item.getProductId()).orElse(null);
            productDTO productDTO = helperService.changeProductAndQtyToDto(product, item.getQuantity());
            bagitem.add(productDTO);
        }
        return bagitem;
    }

    private String getUserIdFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid");
        }
        return jwtUtil.getUserIdFromToken(token);
    }

}
