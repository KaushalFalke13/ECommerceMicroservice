package com.EComMicroService.PaymentServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EComMicroService.PaymentServices.Entity.Payment;
import com.EComMicroService.PaymentServices.Repository.PaymentRepository;
import com.EComMicroService.PaymentServices.Service.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Tests")
class PaymentServiceIntegrationTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private final String TEST_ORDER_ID = "12345";
    private final long TEST_ORDER_ID_LONG = 12345L;
    private final String TEST_USER_ID = "user-123";
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = createPayment(5000, "CREDIT_CARD", "SUCCESS");
    }

    @Test
    @DisplayName("Should start payment and return success for amount less than threshold")
    void startPayment_ShouldReturnSuccess_WhenAmountIsLessThanThreshold() {
        // Arrange
        String orderId = TEST_ORDER_ID;
        long amount = 5000;
        String paymentMode = "CREDIT_CARD";
        Payment expectedPayment = createPayment(amount, paymentMode, "SUCCESS");
        expectedPayment.setOrderid(Long.parseLong(orderId));

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        // Act
        Boolean result = paymentService.startPayent(orderId, amount, paymentMode);

        // Assert
        assertTrue(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should start payment and return failure for amount greater than threshold")
    void startPayment_ShouldReturnFailure_WhenAmountIsGreaterThanThreshold() {
        // Arrange
        String orderId = TEST_ORDER_ID;
        long amount = 15000;
        String paymentMode = "DEBIT_CARD";
        Payment expectedPayment = createPayment(amount, paymentMode, "FAILED");
        expectedPayment.setOrderid(Long.parseLong(orderId));

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        // Act
        Boolean result = paymentService.startPayent(orderId, amount, paymentMode);

        // Assert
        assertFalse(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should get payment by order ID when exists")
    void getPaymentByOrderId_ShouldReturnPayment_WhenExists() {
        // Arrange
        long orderId = TEST_ORDER_ID_LONG;
        Payment expectedPayment = createPayment(5000, "CREDIT_CARD", "SUCCESS");
        expectedPayment.setOrderid(orderId);

        when(paymentRepository.findByOrderid(orderId)).thenReturn(Optional.of(expectedPayment));

        // Act
        Payment result = paymentService.getPaymentDetailsByOrderId(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getOrderid());
        assertEquals(5000, result.getAmount());
        assertEquals("SUCCESS", result.getPaymentStatus());
        verify(paymentRepository, times(1)).findByOrderid(orderId);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent payment by order ID")
    void getPaymentByOrderId_ShouldThrowException_WhenNotExists() {
        // Arrange
        long nonExistentOrderId = 99999L;
        when(paymentRepository.findByOrderid(nonExistentOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentDetailsByOrderId(nonExistentOrderId));
        assertEquals("Payment not found for orderId: " + nonExistentOrderId, exception.getMessage());
    }

    @Test
    @DisplayName("Should get all payments by user ID")
    void getPaymentsByUserId_ShouldReturnList_WhenUserHasPayments() {
        // Arrange
        Payment payment1 = createPayment(5000, "CREDIT_CARD", "SUCCESS");
        payment1.setUserid(TEST_USER_ID);
        Payment payment2 = createPayment(3000, "PAYPAL", "SUCCESS");
        payment2.setUserid(TEST_USER_ID);
        List<Payment> expectedPayments = List.of(payment1, payment2);

        when(paymentRepository.findByUserid(TEST_USER_ID)).thenReturn(expectedPayments);

        // Act
        List<Payment> result = paymentService.getAllPaymentsByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_USER_ID, result.get(0).getUserid());
        verify(paymentRepository, times(1)).findByUserid(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should cancel payment successfully when exists")
    void cancelPayment_ShouldCancelPayment_WhenExists() {
        // Arrange
        long paymentId = 1L;
        Payment payment = createPayment(5000, "CREDIT_CARD", "SUCCESS");
        payment.setPaymentid(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        String result = paymentService.cancelPayment(paymentId);

        // Assert
        assertEquals("Payment cancelled successfully", result);
        assertEquals("CANCELLED", payment.getPaymentStatus());
        verify(paymentRepository, times(1)).findById(paymentId);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-existent payment")
    void cancelPayment_ShouldThrowException_WhenNotExists() {
        // Arrange
        long nonExistentPaymentId = 999L;
        when(paymentRepository.findById(nonExistentPaymentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.cancelPayment(nonExistentPaymentId));
        assertEquals("Payment not found with id: " + nonExistentPaymentId, exception.getMessage());
    }

    @Test
    @DisplayName("Should handle payment with different payment modes")
    void payment_ShouldHandleDifferentPaymentModes() {
        // Arrange
        String[] paymentModes = { "CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "NET_BANKING", "UPI" };
        String orderId = TEST_ORDER_ID;
        long amount = 1000;

        for (String paymentMode : paymentModes) {
            Payment expectedPayment = createPayment(amount, paymentMode, "SUCCESS");
            expectedPayment.setOrderid(Long.parseLong(orderId));
            when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

            // Act
            Boolean result = paymentService.startPayent(orderId, amount, paymentMode);

            // Assert
            assertTrue(result);
        }

        // Verify save was called 5 times (once for each payment mode)
        verify(paymentRepository, times(paymentModes.length)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle multiple payments for different orders")
    void multiplePayments_ShouldHandleCorrectly() {
        // Arrange
        String orderId1 = "12345";
        String orderId2 = "67890";
        long amount1 = 5000;
        long amount2 = 3000;
        String paymentMode1 = "CREDIT_CARD";
        String paymentMode2 = "PAYPAL";

        Payment payment1 = createPayment(amount1, paymentMode1, "SUCCESS");
        payment1.setOrderid(Long.parseLong(orderId1));
        Payment payment2 = createPayment(amount2, paymentMode2, "SUCCESS");
        payment2.setOrderid(Long.parseLong(orderId2));

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment1).thenReturn(payment2);

        // Act
        Boolean result1 = paymentService.startPayent(orderId1, amount1, paymentMode1);
        Boolean result2 = paymentService.startPayent(orderId2, amount2, paymentMode2);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle edge case - zero amount payment")
    void startPayment_ShouldHandleZeroAmount() {
        // Arrange
        String orderId = TEST_ORDER_ID;
        long amount = 0;
        String paymentMode = "CREDIT_CARD";
        Payment expectedPayment = createPayment(amount, paymentMode, "SUCCESS");
        expectedPayment.setOrderid(Long.parseLong(orderId));

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        // Act
        Boolean result = paymentService.startPayent(orderId, amount, paymentMode);

        // Assert
        assertTrue(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle edge case - null payment mode")
    void startPayment_ShouldHandleNullPaymentMode() {
        // Arrange
        String orderId = TEST_ORDER_ID;
        long amount = 5000;
        String paymentMode = null;
        Payment expectedPayment = createPayment(amount, "UNKNOWN", "SUCCESS");
        expectedPayment.setOrderid(Long.parseLong(orderId));

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        // Act
        Boolean result = paymentService.startPayent(orderId, amount, paymentMode);

        // Assert
        assertTrue(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // Helper methods
    private Payment createPayment(long amount, String paymentMode, String status) {
        Payment payment = new Payment();
        payment.setPaymentid(UUID.randomUUID().getMostSignificantBits());
        payment.setOrderid(TEST_ORDER_ID_LONG);
        payment.setUserid(TEST_USER_ID);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode != null ? paymentMode : "UNKNOWN");
        payment.setPaymentStatus(status);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setReferenceNumber("REF-" + System.currentTimeMillis());
        return payment;
    }
}
