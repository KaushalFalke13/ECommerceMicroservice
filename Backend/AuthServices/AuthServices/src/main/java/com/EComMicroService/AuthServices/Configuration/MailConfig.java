// package com.EComMicroService.AuthServices.Configuration;

// import java.util.Properties;

// import org.springframework.boot.autoconfigure.mail.MailProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.JavaMailSenderImpl;

// @Configuration
// public class MailConfig {

// private final MailProperties mailProperties;

// public MailConfig(MailProperties mailProperties) {
// this.mailProperties = mailProperties;
// }

// @Bean
// public JavaMailSender javaMailSender() {
// JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

// // set core connection properties from spring.mail.* (MailProperties)
// if (mailProperties.getHost() != null) {
// mailSender.setHost(mailProperties.getHost());
// }
// if (mailProperties.getPort() != null) {
// mailSender.setPort(mailProperties.getPort());
// }
// if (mailProperties.getUsername() != null) {
// mailSender.setUsername(mailProperties.getUsername());
// }
// if (mailProperties.getPassword() != null) {
// mailSender.setPassword(mailProperties.getPassword());
// }
// // protocol (optional)
// if (mailProperties.getProtocol() != null) {
// mailSender.setProtocol(mailProperties.getProtocol());
// }

// // JavaMail properties
// Properties props = mailSender.getJavaMailProperties();
// props.put("mail.transport.protocol",
// mailProperties.getProtocol() == null ? "smtp" :
// mailProperties.getProtocol());
// props.put("mail.smtp.auth",
// String.valueOf(mailProperties.getProperties().getOrDefault("mail.smtp.auth",
// "true")));
// props.put("mail.smtp.starttls.enable",
// String.valueOf(mailProperties.getProperties().getOrDefault("mail.smtp.starttls.enable",
// "true")));
// props.put("mail.debug",
// String.valueOf(mailProperties.getProperties().getOrDefault("mail.debug",
// "true")));

// return mailSender;
// }
// }
