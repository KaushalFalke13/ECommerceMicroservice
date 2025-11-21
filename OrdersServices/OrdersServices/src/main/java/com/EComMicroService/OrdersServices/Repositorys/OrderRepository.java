package com.EComMicroService.OrdersServices.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.EComMicroService.OrdersServices.Entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    @SuppressWarnings({ "unchecked" })
    Orders save(Orders order);

    List<Orders> findAllByUserId(String userId);

}
