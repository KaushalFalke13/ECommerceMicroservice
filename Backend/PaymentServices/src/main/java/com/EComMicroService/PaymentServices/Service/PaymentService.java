package com.EComMicroService.PaymentServices.Service;

import java.util.List;

import com.EComMicroService.PaymentServices.Entity.Payment;

public interface PaymentService {

    Boolean startPayent(String orderId, long amount, String paymentMode);

    Payment getPaymentDetailsByOrderId(long orderId);

    List<Payment> getAllPaymentsByUserId(String userId);

    String cancelPayment(long PaymentId);
}
