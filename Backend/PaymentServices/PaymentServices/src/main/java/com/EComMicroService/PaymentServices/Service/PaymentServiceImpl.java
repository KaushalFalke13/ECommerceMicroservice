package com.EComMicroService.PaymentServices.Service;

import java.util.List;

import com.EComMicroService.PaymentServices.Entity.Payment;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public String startPayent(long orderId, double amount, String paymentMode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startPayent'");
    }

    @Override
    public Payment getPaymentDetailsByOrderId(long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentDetailsByOrderId'");
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPaymentsByUserId'");
    }

    @Override
    public String cancelPayment(long PaymentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelPayment'");
    }


}
