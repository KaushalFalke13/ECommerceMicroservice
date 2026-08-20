package com.EComMicroService.ProductsServices.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.EComMicroService.ProductsServices.Entity.products;
import jakarta.transaction.Transactional;

@Repository
public interface productRepository extends JpaRepository<products, String> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
                UPDATE products p
                SET p.stock = p.stock - :qty,
                    p.reservedStock = p.reservedStock + :qty
                WHERE p.id = :productId AND p.stock >= :qty
            """)
    int reserveStockIfAvailable(@Param("productId") String productId, @Param("qty") int qty);

    @Modifying
    @Transactional
    @Query("""
                UPDATE products p
                SET p.stock = p.stock + :qty,
                    p.reservedStock = p.reservedStock - :qty
                WHERE p.id = :productId AND p.reservedStock >= :qty
            """)
    int releaseReservedStock(@Param("productId") String productId, @Param("qty") int qty);

    @Modifying
    @Transactional
    @Query("""
                UPDATE products p
                SET p.reservedStock = p.reservedStock - :qty
                WHERE p.id = :productId AND p.reservedStock >= :qty
            """)
    int confirmReservedStock(@Param("productId") String productId, @Param("qty") int qty);
}
