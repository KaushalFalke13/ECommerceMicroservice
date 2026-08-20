package com.EComMicroService.OrdersServices.Repositorys;

import org.springframework.stereotype.Repository;
import com.EComMicroService.OrdersServices.Entity.OrdersEventsLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface OrderEventRepository extends JpaRepository<OrdersEventsLog,Long>{

    @Query("SELECT e FROM OrdersEventsLog e WHERE e.published = 'PENDING'")
    List<OrdersEventsLog> findAllNonPublishedEvent();

    }
