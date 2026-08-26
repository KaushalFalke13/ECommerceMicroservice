package com.ecommicroservice.orderservices.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void constructor_shouldInitializeWithDefaultValues() {
        // Act
        ApiResponse<String> response = new ApiResponse<>();

        // Assert
        assertEquals(0, response.getStatus());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void constructor_shouldInitializeWithStatusAndMessage() {
        // Act
        ApiResponse<String> response = new ApiResponse<>(200, "Success");

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void constructor_shouldInitializeWithAllFields() {
        // Arrange
        String data = "Test Data";

        // Act
        ApiResponse<String> response = new ApiResponse<>(201, "Created", data);

        // Assert
        assertEquals(201, response.getStatus());
        assertEquals("Created", response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void constructor_shouldHandleNullData() {
        // Act
        ApiResponse<String> response = new ApiResponse<>(400, "Bad Request", null);

        // Assert
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void setterAndGetter_shouldUpdateFieldsCorrectly() {
        // Arrange
        ApiResponse<String> response = new ApiResponse<>();

        // Act
        response.setStatus(404);
        response.setMessage("Not Found");
        response.setData("Resource not available");

        // Assert
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getMessage());
        assertEquals("Resource not available", response.getData());
    }

    @Test
    void setterAndGetter_shouldHandleIntegerData() {
        // Arrange
        ApiResponse<Integer> response = new ApiResponse<>();

        // Act
        response.setStatus(200);
        response.setMessage("OK");
        response.setData(42);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(42, response.getData());
    }

    @Test
    void setterAndGetter_shouldHandleObjectData() {
        // Arrange
        class TestObject {
            String name;
            int value;

            TestObject(String name, int value) {
                this.name = name;
                this.value = value;
            }
        }
        TestObject obj = new TestObject("test", 123);
        ApiResponse<TestObject> response = new ApiResponse<>();

        // Act
        response.setStatus(200);
        response.setMessage("Success");
        response.setData(obj);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("test", response.getData().name);
        assertEquals(123, response.getData().value);
    }
}
