package com.EComMicroService.PaymentServices.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EComMicroService.PaymentServices.Entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderid(long orderId);

    List<Payment> findByUserid(String userId);

    List<Payment> findByPaymentStatus(String status);
}
