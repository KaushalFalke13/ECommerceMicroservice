package com.EComMicroService.PaymentServices.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EComMicroService.PaymentServices.Entity.Payment;
import com.EComMicroService.PaymentServices.Service.PaymentService;

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
    public ResponseEntity<Boolean> startPayment(
            @RequestParam String orderId,
            @RequestParam long amount,
            @RequestParam String paymentMode) {
        log.info("Received request to start payment for orderId: {}, amount: {}, paymentMode: {}",
                orderId, amount, paymentMode);
        Boolean result = paymentService.startPayent(orderId, amount, paymentMode);
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result);
    }
}
