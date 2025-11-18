package com.EComMicroService.AuthServices.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;

    public EmailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpToEmail(String email) {
        String OTP = ((Math.random() * 9000) + 1000)+"";

        // store OTP with expiry time in redis

        String subject = "Email Verification OTP";
        String message = "Your OTP for email verification is: " + OTP;
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);   
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("fhalkekaushal13@gmail.com");
        mailSender.send(mailMessage);
    }

    public boolean verifyOTP(String email, String otp){
    //    get otp from redis and compare

        return true;
    }

}
