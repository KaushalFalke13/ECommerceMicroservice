package com.EComMicroService.PaymentServices.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EComMicroService.PaymentServices.Entity.Payment;
import com.EComMicroService.PaymentServices.Repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Boolean startPayent(String orderId, long amount, String paymentMode) {
        log.info("Starting payment for orderId: {}, amount: {}, paymentMode: {}", orderId, amount, paymentMode);

        // Validate amount - must not be negative
        if (amount < 0) {
            log.warn("Invalid payment amount: {} - must not be negative", amount);
            return false;
        }

        try {
            // Simulate payment processing - in real world, this would call a payment
            // gateway
            boolean paymentSuccessful = processPayment(amount, paymentMode);

            Payment payment = new Payment();
            payment.setOrderid(Long.parseLong(orderId));
            payment.setAmount(amount);
            payment.setPaymentMode(paymentMode);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setReferenceNumber("REF-" + System.currentTimeMillis());
            payment.setPaymentStatus(paymentSuccessful ? "SUCCESS" : "FAILED");

            paymentRepository.save(payment);

            log.info("Payment {} for orderId: {}", paymentSuccessful ? "SUCCESS" : "FAILED", orderId);
            return paymentSuccessful;

        } catch (Exception e) {
            log.error("Payment failed for orderId: {}", orderId, e);
            return false;
        }
    }

    @Override
    public Payment getPaymentDetailsByOrderId(long orderId) {
        log.info("Fetching payment details for orderId: {}", orderId);
        return paymentRepository.findByOrderid(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for orderId: " + orderId));
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(String userId) {
        log.info("Fetching all payments for userId: {}", userId);
        return paymentRepository.findByUserid(userId);
    }

    @Override
    @Transactional
    public String cancelPayment(long paymentId) {
        log.info("Cancelling payment with id: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        payment.setPaymentStatus("CANCELLED");
        paymentRepository.save(payment);

        return "Payment cancelled successfully";
    }

    private boolean processPayment(long amount, String paymentMode) {
        // Simulate payment gateway call
        // In real implementation, this would integrate with Stripe, PayPal, etc.
        try {
            // Simulate network delay
            Thread.sleep(500);

            // For demo purposes, always succeed for amount < 10000
            // Fail for amount >= 10000 to simulate payment failure scenarios
            if (amount >= 10000) {
                log.warn("Payment failed for amount: {} (simulated failure)", amount);
                return false;
            }

            log.info("Payment processed successfully for amount: {}", amount);
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
