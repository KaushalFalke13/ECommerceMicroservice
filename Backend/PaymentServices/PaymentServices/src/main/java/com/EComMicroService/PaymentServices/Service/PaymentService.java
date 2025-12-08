package com.EComMicroService.PaymentServices.Service;

import java.util.List;

import com.EComMicroService.PaymentServices.Entity.Payment;

public interface PaymentService {

    String startPayent(long orderId, double amount, String paymentMode);

    Payment getPaymentDetailsByOrderId(long orderId);

    List<Payment> getAllPaymentsByUserId(String userId);

    String cancelPayment(long PaymentId);
}
