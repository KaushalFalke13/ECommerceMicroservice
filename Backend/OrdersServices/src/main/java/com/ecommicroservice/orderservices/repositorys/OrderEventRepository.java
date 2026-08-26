package com.ecommicroservice.orderservices.repositorys;

import org.springframework.stereotype.Repository;
import com.ecommicroservice.orderservices.entity.OrdersEventsLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrderEventRepository extends JpaRepository<OrdersEventsLog, Long> {

    @Query("SELECT e FROM OrdersEventsLog e WHERE e.published = 'PENDING'")
    List<OrdersEventsLog> findAllNonPublishedEvent();

}
