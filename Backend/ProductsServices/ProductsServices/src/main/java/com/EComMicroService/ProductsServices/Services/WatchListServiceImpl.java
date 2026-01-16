package com.EComMicroService.ProductsServices.Services;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.EComMicroService.ProductsServices.Configuration.jwtUtil;
import com.EComMicroService.ProductsServices.DTO.productDTO;
import com.EComMicroService.ProductsServices.Entity.Watchlist;
import com.EComMicroService.ProductsServices.Entity.WatchlistItem;
import com.EComMicroService.ProductsServices.Repositorys.WatchlistItemRepository;
import com.EComMicroService.ProductsServices.Repositorys.WatchlistRepository;

@Service
public class WatchListServiceImpl implements WatchListService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistItemRepository watchlistItemRepository;
    private final jwtUtil jwtUtil;
    private final productService productService;

    public WatchListServiceImpl(WatchlistRepository watchlistRepository,
            WatchlistItemRepository watchlistItemRepository, jwtUtil jwtUtil, productService productService) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistItemRepository = watchlistItemRepository;
        this.jwtUtil = jwtUtil;
        this.productService = productService;
    }

    @Override
    public List<productDTO> addItem(String authHeader, String productId) {
        String userId = getUserIdFromAuthHeader(authHeader);

        Watchlist watchlist = watchlistRepository.findByUserId(userId)
                .orElseGet(() -> watchlistRepository.save(
                        Watchlist.builder()
                                .userId(userId)
                                .build()));

        WatchlistItem item = watchlistItemRepository
                .findByWatchlistAndProductId(watchlist, productId)
                .orElseGet(() -> {
                    WatchlistItem newItem = WatchlistItem.builder()
                            .watchlist(watchlist)
                            .productId(productId)
                            .quantity(0)
                            .build();
                    watchlist.addItem(newItem);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + 1);
        watchlistRepository.save(watchlist);
        return mapToProductDTOList(watchlistItemRepository.findAllByWatchlist(watchlist));
    }

    @Override
    public List<productDTO> removeItem(String authHeader, String productId) {
        String userId = getUserIdFromAuthHeader(authHeader);
        List<WatchlistItem> items = null;

        Watchlist Watchlist = watchlistRepository.findByUserId(userId)
                .orElse(null);
        if (Watchlist != null) {
            watchlistItemRepository.deleteByWatchlistAndProductId(Watchlist, productId);
            items = watchlistItemRepository.findAllByWatchlist(Watchlist);
        }

        return mapToProductDTOList(items);
    }

    @Override
    public List<productDTO> getItems(String authHeader) {
        String userId = getUserIdFromAuthHeader(authHeader);
        List<WatchlistItem> items = null;
        Watchlist Watchlist = watchlistRepository.findByUserId(userId)
                .orElse(null);
        if (Watchlist != null) {
            items = watchlistItemRepository.findAllByWatchlist(Watchlist);
        }
        return mapToProductDTOList(items);
    }

    private List<productDTO> mapToProductDTOList(List<WatchlistItem> items) {
        return items.stream().map(item -> {
            productDTO product = productService.getProductById(item.getProductId());
            return product;
        }).toList();
    }

    private String getUserIdFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid");
        }
        return jwtUtil.getUserIdFromToken(token);
    }

}
