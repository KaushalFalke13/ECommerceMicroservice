package com.ecommicroservice.paymentservices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommicroservice.paymentservices.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderid(long orderId);

    List<Payment> findByUserid(String userId);

    List<Payment> findByPaymentStatus(String status);
}
