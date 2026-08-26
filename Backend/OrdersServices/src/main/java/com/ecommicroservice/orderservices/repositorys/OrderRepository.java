package com.ecommicroservice.orderservices.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommicroservice.orderservices.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    @SuppressWarnings("unchecked")
    Orders save(Orders order);

    List<Orders> findAllByUserId(String userId);

}
