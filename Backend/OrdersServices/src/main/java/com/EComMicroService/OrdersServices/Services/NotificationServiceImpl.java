package com.EComMicroService.OrdersServices.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.Entity.Orders;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender emailSender;

    @Override
    public void sendOrderConfirmation(Orders order, String userEmail) {
        logger.info("Order Confirmation Email sent to {} for Order ID: {}", userEmail, order.getOrderId());
        logger.info("Order Details: Total: {}, Status: {}", order.getTotalAmount(), order.getOrderStatus());

        String subject = "Order Confirmation - " + order.getOrderId();
        String content = buildEmailContent(order);

        sendEmail(userEmail, subject, content);
    }

    @Override
    public void sendOrderStatusUpdate(Orders order, String userEmail) {
        logger.info("Order Status Update Email sent to {} for Order ID: {} - Status: {}",
                userEmail, order.getOrderId(), order.getOrderStatus());

        String subject = "Order Status Update - " + order.getOrderId();
        String content = buildStatusUpdateContent(order);

        sendEmail(userEmail, subject, content);
    }

    private void sendEmail(String to, String subject, String text) {
        if (emailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                emailSender.send(message);
                logger.info("Email sent successfully to {}", to);
            } catch (Exception e) {
                logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            }
        } else {
            // Fallback: log the email content when no email sender is configured
            logger.info("Email would be sent to: {}", to);
            logger.info("Subject: {}", subject);
            logger.info("Content: {}", text);
        }
    }

    private String buildEmailContent(Orders order) {
        return String.format("""
                Thank you for your order!

                Order ID: %s
                Order Number: %s
                Total Amount: $%.2f
                Status: %s

                We'll notify you when your order ships.

                If you have any questions, please contact our support team.
                """,
                order.getOrderId(),
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getOrderStatus());
    }

    private String buildStatusUpdateContent(Orders order) {
        return String.format("""
                Order Status Update

                Order ID: %s
                Order Number: %s
                Current Status: %s
                Total Amount: $%.2f

                To view more details, please log in to your account.
                """,
                order.getOrderId(),
                order.getOrderNumber(),
                order.getOrderStatus(),
                order.getTotalAmount());
    }
}
