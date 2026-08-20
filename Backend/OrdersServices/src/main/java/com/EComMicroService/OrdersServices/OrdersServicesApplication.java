package com.EComMicroService.OrdersServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrdersServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServicesApplication.class, args);
	}

}
