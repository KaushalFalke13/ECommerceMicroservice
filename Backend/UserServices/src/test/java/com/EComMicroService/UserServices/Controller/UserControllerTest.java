package com.EComMicroService.UserServices.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Entity.UsersDetails;
import com.EComMicroService.UserServices.Services.userService;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller Unit Tests")
class UserControllerTest {

    @Mock
    private userService userService;

    @InjectMocks
    private UserController userController;

    private UsersDTO testUserDTO;
    private UsersDetails testUserDetails;
    private String testUserId;

    @BeforeEach
    void setUp() {
        testUserId = "test-user-123";

        testUserDTO = new UsersDTO();
        testUserDTO.setName("John Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setPassword("password123");

        testUserDetails = UsersDetails.builder()
                .id(testUserId)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should return home message")
    void home_ShouldReturnSuccessMessage() {
        // Act
        ResponseEntity<?> response = userController.home();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Service is up and running", response.getBody());
    }

    @Test
    @DisplayName("Should create user and return OK status")
    void createNewUser_ShouldReturnOkResponse_WhenUserIsSaved() {
        // Arrange
        when(userService.saveUser(any(UsersDTO.class))).thenReturn(testUserDetails);

        // Act
        ResponseEntity<?> response = userController.createNewUser(testUserDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created", response.getBody());
        verify(userService, times(1)).saveUser(testUserDTO);
    }

    @Test
    @DisplayName("Should get user by ID and return OK status")
    void getUserDetails_ShouldReturnOkResponse_WhenUserExists() {
        // Arrange
        UsersDTO expectedDTO = new UsersDTO();
        expectedDTO.setName("John Doe");
        expectedDTO.setEmail("john.doe@example.com");
        expectedDTO.setPassword("password123");
        when(userService.getUserById(testUserId)).thenReturn(expectedDTO);

        // Act
        ResponseEntity<?> response = userController.getUserDetails(testUserId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(userService, times(1)).getUserById(testUserId);
    }

    @Test
    @DisplayName("Should handle service exception when creating user")
    void createNewUser_ShouldPropagateServiceException() {
        // Arrange
        when(userService.saveUser(any(UsersDTO.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.createNewUser(testUserDTO));
        verify(userService, times(1)).saveUser(testUserDTO);
    }

    @Test
    @DisplayName("Should handle service exception when getting user")
    void getUserDetails_ShouldPropagateServiceException() {
        // Arrange
        when(userService.getUserById(testUserId)).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.getUserDetails(testUserId));
        verify(userService, times(1)).getUserById(testUserId);
    }
}
