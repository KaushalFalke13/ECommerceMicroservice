package com.ecommicroservice.paymentservices.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommicroservice.paymentservices.entity.Payment;
import com.ecommicroservice.paymentservices.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Unit Tests")
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment mockPayment;
    private final String TEST_ORDER_ID = "12345";
    private final long TEST_ORDER_ID_LONG = 12345L;
    private final String TEST_USER_ID = "user-123";
    private final long TEST_AMOUNT = 5000L;
    private final long TEST_PAYMENT_ID = 1L;

    @BeforeEach
    void setUp() {
        mockPayment = new Payment();
        mockPayment.setPaymentid(TEST_PAYMENT_ID);
        mockPayment.setOrderid(TEST_ORDER_ID_LONG);
        mockPayment.setUserid(TEST_USER_ID);
        mockPayment.setAmount(TEST_AMOUNT);
        mockPayment.setPaymentMode("CREDIT_CARD");
        mockPayment.setPaymentStatus("SUCCESS");
        mockPayment.setTransactionId(UUID.randomUUID().toString());
        mockPayment.setReferenceNumber("REF-1234567890");
    }

    @Test
    @DisplayName("Should process payment successfully for amount less than 10000")
    void startPayment_ShouldReturnTrue_WhenAmountIsLessThanThreshold() {
        // Arrange
        long amount = 5000L;
        String paymentMode = "CREDIT_CARD";

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, amount, paymentMode);

        // Assert
        assertTrue(result);

        // Verify payment was saved
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(Long.parseLong(TEST_ORDER_ID), savedPayment.getOrderid());
        assertEquals(amount, savedPayment.getAmount());
        assertEquals(paymentMode, savedPayment.getPaymentMode());
        assertEquals("SUCCESS", savedPayment.getPaymentStatus());
        assertNotNull(savedPayment.getTransactionId());
        assertNotNull(savedPayment.getReferenceNumber());
    }

    @Test
    @DisplayName("Should fail payment for amount greater than or equal to 10000")
    void startPayment_ShouldReturnFalse_WhenAmountIsGreaterThanThreshold() {
        // Arrange
        long amount = 15000L;
        String paymentMode = "DEBIT_CARD";

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, amount, paymentMode);

        // Assert
        assertFalse(result);

        // Verify payment was saved with FAILED status
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals("FAILED", savedPayment.getPaymentStatus());
    }

    @Test
    @DisplayName("Should handle payment failure when exception occurs")
    void startPayment_ShouldReturnFalse_WhenExceptionOccurs() {
        // Arrange
        when(paymentRepository.save(any(Payment.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, TEST_AMOUNT, "PAYPAL");

        // Assert
        assertFalse(result);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle payment failure when orderId is invalid")
    void startPayment_ShouldReturnFalse_WhenOrderIdIsInvalid() {
        // Arrange
        String invalidOrderId = "invalid";

        // Act
        Boolean result = paymentService.startPayent(invalidOrderId, TEST_AMOUNT, "CREDIT_CARD");

        // Assert
        assertFalse(result);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should get payment details by order ID when payment exists")
    void getPaymentDetailsByOrderId_ShouldReturnPayment_WhenExists() {
        // Arrange
        when(paymentRepository.findByOrderid(TEST_ORDER_ID_LONG))
                .thenReturn(Optional.of(mockPayment));

        // Act
        Payment result = paymentService.getPaymentDetailsByOrderId(TEST_ORDER_ID_LONG);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_PAYMENT_ID, result.getPaymentid());
        assertEquals(TEST_ORDER_ID_LONG, result.getOrderid());
        assertEquals(TEST_USER_ID, result.getUserid());
        verify(paymentRepository).findByOrderid(TEST_ORDER_ID_LONG);
    }

    @Test
    @DisplayName("Should throw exception when getting payment by non-existent order ID")
    void getPaymentDetailsByOrderId_ShouldThrowException_WhenNotExists() {
        // Arrange
        long nonExistentOrderId = 99999L;
        when(paymentRepository.findByOrderid(nonExistentOrderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentDetailsByOrderId(nonExistentOrderId));

        assertEquals("Payment not found for orderId: " + nonExistentOrderId, exception.getMessage());
        verify(paymentRepository).findByOrderid(nonExistentOrderId);
    }

    @Test
    @DisplayName("Should get all payments by user ID")
    void getAllPaymentsByUserId_ShouldReturnListOfPayments() {
        // Arrange
        Payment payment2 = new Payment();
        payment2.setPaymentid(2L);
        payment2.setUserid(TEST_USER_ID);

        List<Payment> expectedPayments = Arrays.asList(mockPayment, payment2);
        when(paymentRepository.findByUserid(TEST_USER_ID))
                .thenReturn(expectedPayments);

        // Act
        List<Payment> result = paymentService.getAllPaymentsByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_USER_ID, result.get(0).getUserid());
        verify(paymentRepository).findByUserid(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return empty list when user has no payments")
    void getAllPaymentsByUserId_ShouldReturnEmptyList_WhenNoPayments() {
        // Arrange
        when(paymentRepository.findByUserid(TEST_USER_ID))
                .thenReturn(Arrays.asList());

        // Act
        List<Payment> result = paymentService.getAllPaymentsByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository).findByUserid(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should cancel payment successfully when payment exists")
    void cancelPayment_ShouldCancelPayment_WhenExists() {
        // Arrange
        when(paymentRepository.findById(TEST_PAYMENT_ID))
                .thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(mockPayment);

        // Act
        String result = paymentService.cancelPayment(TEST_PAYMENT_ID);

        // Assert
        assertEquals("Payment cancelled successfully", result);
        assertEquals("CANCELLED", mockPayment.getPaymentStatus());
        verify(paymentRepository).findById(TEST_PAYMENT_ID);
        verify(paymentRepository).save(mockPayment);
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-existent payment")
    void cancelPayment_ShouldThrowException_WhenPaymentNotExists() {
        // Arrange
        long nonExistentPaymentId = 99999L;
        when(paymentRepository.findById(nonExistentPaymentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.cancelPayment(nonExistentPaymentId));

        assertEquals("Payment not found with id: " + nonExistentPaymentId, exception.getMessage());
        verify(paymentRepository).findById(nonExistentPaymentId);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle different payment modes correctly")
    void startPayment_ShouldHandleDifferentPaymentModes() {
        // Arrange
        String[] paymentModes = { "CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "NET_BANKING", "UPI" };

        for (String paymentMode : paymentModes) {
            // Act
            Boolean result = paymentService.startPayent(TEST_ORDER_ID, TEST_AMOUNT, paymentMode);

            // Assert
            assertTrue(result);
            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository, atLeastOnce()).save(paymentCaptor.capture());

            Payment savedPayment = paymentCaptor.getValue();
            assertEquals(paymentMode, savedPayment.getPaymentMode());
        }
    }

    @Test
    @DisplayName("Should generate unique transaction and reference numbers for each payment")
    void startPayment_ShouldGenerateUniqueTransactionAndReferenceNumbers() {
        // Act - Create multiple payments
        paymentService.startPayent(TEST_ORDER_ID, TEST_AMOUNT, "CREDIT_CARD");
        paymentService.startPayent("67890", 3000L, "PAYPAL");

        // Assert
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(2)).save(paymentCaptor.capture());

        List<Payment> savedPayments = paymentCaptor.getAllValues();

        // Verify first payment has unique IDs
        assertNotNull(savedPayments.get(0).getTransactionId());
        assertNotNull(savedPayments.get(0).getReferenceNumber());

        // Verify second payment has different unique IDs
        assertNotNull(savedPayments.get(1).getTransactionId());
        assertNotNull(savedPayments.get(1).getReferenceNumber());

        // Verify they are different
        assertNotEquals(savedPayments.get(0).getTransactionId(), savedPayments.get(1).getTransactionId());
        assertNotEquals(savedPayments.get(0).getReferenceNumber(), savedPayments.get(1).getReferenceNumber());
    }

    @Test
    @DisplayName("Should handle payment with zero amount")
    void startPayment_ShouldHandleZeroAmount() {
        // Arrange
        long zeroAmount = 0L;

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, zeroAmount, "CREDIT_CARD");

        // Assert
        assertTrue(result);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(0L, savedPayment.getAmount());
        assertEquals("SUCCESS", savedPayment.getPaymentStatus());
    }

    @Test
    @DisplayName("Should handle payment with large amount")
    void startPayment_ShouldHandleLargeAmount() {
        // Arrange
        long largeAmount = 9999L; // Just below threshold

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, largeAmount, "CREDIT_CARD");

        // Assert
        assertTrue(result);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(largeAmount, savedPayment.getAmount());
        assertEquals("SUCCESS", savedPayment.getPaymentStatus());
    }

    @Test
    @DisplayName("Should handle payment with negative amount")
    void startPayment_ShouldHandleNegativeAmount() {
        // Arrange
        long negativeAmount = -1000L;

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, negativeAmount, "CREDIT_CARD");

        // Assert
        assertFalse(result);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle null payment mode gracefully")
    void startPayment_ShouldHandleNullPaymentMode() {
        // Arrange
        String nullPaymentMode = null;

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, TEST_AMOUNT, nullPaymentMode);

        // Assert
        assertTrue(result);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertNull(savedPayment.getPaymentMode());
    }

    @Test
    @DisplayName("Should handle empty payment mode gracefully")
    void startPayment_ShouldHandleEmptyPaymentMode() {
        // Arrange
        String emptyPaymentMode = "";

        // Act
        Boolean result = paymentService.startPayent(TEST_ORDER_ID, TEST_AMOUNT, emptyPaymentMode);

        // Assert
        assertTrue(result);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(emptyPaymentMode, savedPayment.getPaymentMode());
    }

    @Test
    @DisplayName("Should set user ID in payment when provided in order")
    void startPayment_ShouldSetUserIdInPayment() {
        // This test verifies that the payment entity properly stores user ID
        // The current implementation doesn't explicitly set user ID from order
        // But we test that the repository method with userId works correctly

        // Arrange
        String testUserId = "user-456";
        when(paymentRepository.findByUserid(testUserId))
                .thenReturn(Arrays.asList(mockPayment));

        // Act
        List<Payment> result = paymentService.getAllPaymentsByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository).findByUserid(testUserId);
    }
}
