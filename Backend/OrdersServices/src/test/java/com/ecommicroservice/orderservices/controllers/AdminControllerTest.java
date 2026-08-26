package com.ecommicroservice.orderservices.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdminControllerTest {

    @Test
    void constructor_shouldCreateInstance() {
        // Act
        AdminController controller = new AdminController();

        // Assert
        assertNotNull(controller);
    }
}
