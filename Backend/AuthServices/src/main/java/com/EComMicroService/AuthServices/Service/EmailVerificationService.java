package com.EComMicroService.AuthServices.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HexFormat;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redis;
    private final SecureRandom secureRandom = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();
    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration ATTEMPTS_TTL = Duration.ofMinutes(10);
    private static final int MAX_ATTEMPTS = 3;
    private final String mailFrom;

    public EmailVerificationService(JavaMailSender mailSender, StringRedisTemplate redis,
            @Value("${spring.mail.from:}") String mailFrom) {
        this.mailSender = mailSender;
        this.redis = redis;
        this.mailFrom = mailFrom;
    }

    @PostConstruct
    private void validateConfig() {
        if (!StringUtils.hasText(mailFrom)) {
            System.err.println("[WARN] spring.mail.from not set; set a valid sender address.");
        }
    }

    @Async
    public void sendOtpToEmail(String rawEmail) {
        String email = normalizeIdentifier(rawEmail);

        String otp = generateNumericOtp(6);
        String hashed = computeHmacOrSha256Hex(otp);

        String key = otpKey("verify-email", email);
        // store hashed OTP with TTL and reset attempts
        redis.opsForValue().set(key, hashed, OTP_TTL.toMillis(), TimeUnit.MILLISECONDS);
        redis.delete(attemptsKey("verify-email", email));

        // build email (send async)
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Email Verification OTP");
        mailMessage.setText("Your OTP for email verification is: " + otp);
        if (StringUtils.hasText(mailFrom))
            mailMessage.setFrom(mailFrom);

        try {
            mailSender.send(mailMessage);
        } catch (Exception ex) {
            redis.delete(key);
            throw new RuntimeException("Failed to send OTP email", ex);
        }
    }

    public boolean verifyOTP(String rawEmail, String otpSupplied) {
        String email = normalizeIdentifier(rawEmail);

        if (!StringUtils.hasText(otpSupplied) || otpSupplied.length() > 10) {
            return false;
        }

        String key = otpKey("verify-email", email);
        String storedHash = redis.opsForValue().get(key);
        if (storedHash == null) {
            return false;
        }

        String providedHash = computeHmacOrSha256Hex(otpSupplied);

        boolean match = constantTimeHexEquals(storedHash, providedHash);

        if (match) {
            redis.delete(key);
            redis.delete(attemptsKey("verify-email", email));
            return true;
        } else {
            String attemptsKey = attemptsKey("verify-email", email);
            Long attempts = redis.opsForValue().increment(attemptsKey);
            if (attempts != null && attempts == 1) {
                redis.expire(attemptsKey, ATTEMPTS_TTL.toMillis(), TimeUnit.MILLISECONDS);
            }
            if (attempts != null && attempts >= MAX_ATTEMPTS) {
                redis.delete(key);
                return false;
            }
            return false;
        }
    }

    private String generateNumericOtp(int length) {
        if (length <= 0)
            length = 6;
        int bound = (int) Math.pow(10, length);
        int number = secureRandom.nextInt(bound); // 0 .. bound-1
        return String.format("%0" + length + "d", number); // preserves leading zeros
    }

    private String computeHmacOrSha256Hex(String input) {
        try {
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HEX.formatHex(md.digest(bytes));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to compute hash", ex);
        }
    }

    private boolean constantTimeHexEquals(String hexA, String hexB) {
        try {
            byte[] a = HEX.parseHex(hexA);
            byte[] b = HEX.parseHex(hexB);
            return MessageDigest.isEqual(a, b); // constant-time
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String otpKey(String purpose, String identifier) {
        return "otp:" + purpose + ":" + identifier;
    }

    private String attemptsKey(String purpose, String identifier) {
        return "otp:attempts:" + purpose + ":" + identifier;
    }

    private String normalizeIdentifier(String raw) {
        if (raw == null)
            return null;
        return raw.trim().toLowerCase();
    }

}
