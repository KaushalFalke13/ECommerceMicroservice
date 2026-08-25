package com.EComMicroService.PaymentServices.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EComMicroService.PaymentServices.DTO.PaymentRequestDTO;
import com.EComMicroService.PaymentServices.Entity.Payment;
import com.EComMicroService.PaymentServices.Exception.PaymentNotFoundException;
import com.EComMicroService.PaymentServices.Service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable long orderId) {
        log.info("Received request to get payment for orderId: {}", orderId);
        Payment payment = paymentService.getPaymentDetailsByOrderId(orderId);
        if (payment == null) {
            throw new PaymentNotFoundException("Payment not found for orderId: " + orderId);
        }
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable String userId) {
        log.info("Received request to get payments for userId: {}", userId);
        List<Payment> payments = paymentService.getAllPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/cancel/{paymentId}")
    public ResponseEntity<String> cancelPayment(@PathVariable long paymentId) {
        log.info("Received request to cancel payment with id: {}", paymentId);
        String result = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/start")
    public ResponseEntity<Boolean> startPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        log.info("Received request to start payment for orderId: {}, amount: {}, paymentMode: {}",
                paymentRequest.getOrderId(), paymentRequest.getAmount(), paymentRequest.getPaymentMode());
        Boolean result = paymentService.startPayent(
                paymentRequest.getOrderId(),
                paymentRequest.getAmount(),
                paymentRequest.getPaymentMode());
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result);
    }
}
