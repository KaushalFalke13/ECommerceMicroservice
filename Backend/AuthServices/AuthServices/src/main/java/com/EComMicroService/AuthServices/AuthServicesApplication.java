package com.EComMicroService.AuthServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServicesApplication {

	public static void main(String[] args) {

		// start Spring and get the application context
		// ConfigurableApplicationContext ctx
		SpringApplication.run(AuthServicesApplication.class, args);

		// // inspect the Environment for mail properties (safe: we do NOT print the
		// // password value)
		// Environment env = ctx.getEnvironment();
		// System.out.println("========== SPRING MAIL PROPS ==========");
		// System.out.println("spring.mail.host = " +
		// env.getProperty("spring.mail.host"));
		// System.out.println("spring.mail.username = " +
		// env.getProperty("spring.mail.username"));
		// System.out.println("spring.mail.password is null? " +
		// (env.getProperty("spring.mail.password") == null));
		// System.out.println("spring.mail.from = " +
		// env.getProperty("spring.mail.from"));
		// System.out.println("=======================================");
	}
}
