package com.EComMicroService.productsservices.Repositorys;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import com.EComMicroService.productsservices.Entity.Watchlist;
import com.EComMicroService.productsservices.Entity.WatchlistItem;
import jakarta.persistence.LockModeType;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WatchlistItem> findByWatchlistAndProductId(Watchlist Watchlist, String productId);

    void deleteByWatchlistAndProductId(Watchlist Watchlist, String productId);

    List<WatchlistItem> findAllByWatchlist(Watchlist Watchlist);

}
