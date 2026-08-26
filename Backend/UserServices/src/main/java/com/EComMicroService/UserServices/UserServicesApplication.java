package com.ecommicroservice.userservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServicesApplication {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(UserServicesApplication.class);
        app.run(args);
    }
}
