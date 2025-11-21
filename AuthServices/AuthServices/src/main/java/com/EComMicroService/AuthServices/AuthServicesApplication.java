package com.EComMicroService.AuthServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServicesApplication {

	public static void main(String[] args) {

		// Start Spring and get application context
		var ctx = SpringApplication.run(AuthServicesApplication.class, args);

		// ✅ CHECK ENV AFTER SPRING CONTEXT STARTS (reads from Spring
		// Environment/property sources)
		var env = ctx.getEnvironment();
		System.out.println("========== POST-START ENV CHECK ==========");
		System.out.println("MAIL_HOST = " + env.getProperty("MAIL_HOST"));
		System.out.println("MAIL_USER = " + env.getProperty("MAIL_USER"));
		System.out.println("MAIL_PASS = " + env.getProperty("MAIL_PASS"));
		System.out.println("MAIL_PORT = " + env.getProperty("MAIL_PORT"));
		System.out.println("==========================================");
	}
}
