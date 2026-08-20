package com.EComMicroService.ProductsServices.Repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.EComMicroService.ProductsServices.Entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    Optional<Watchlist> findByUserId(String userId);
    Watchlist save(Watchlist watchlist);

}
