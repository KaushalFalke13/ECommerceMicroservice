package com.EComMicroService.PaymentServices.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long paymentid;
    private String userid;
    private String paymentStatus;
    private String transactionId;
    private long orderid;
    private double amount;
    private String paymentMode;
    private String referenceNumber;

}
