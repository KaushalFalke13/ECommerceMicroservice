package com.ecommicroservice.authservices.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommicroservice.authservices.dto.ApiResponse;
import com.ecommicroservice.authservices.dto.UsersDTO;
import com.ecommicroservice.authservices.entity.Users;
import com.ecommicroservice.authservices.service.EmailVerificationService;
import com.ecommicroservice.authservices.service.JwtToken;
import com.ecommicroservice.authservices.service.UsersService;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Controller Unit Tests")
class AuthControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private EmailVerificationService emailService;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthController authController;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_OTP = "123456";
    private final String TEST_USER_ID = "user-123";
    private final String TEST_TOKEN = "jwt-token-123";

    private UsersDTO testUserDTO;
    private Users testUser;

    @BeforeEach
    void setUp() {
        testUserDTO = new UsersDTO();
        testUserDTO.setEmail(TEST_EMAIL);
        testUserDTO.setPassword(TEST_PASSWORD);

        testUser = Users.builder()
                .userId(TEST_USER_ID)
                .email(TEST_EMAIL)
                .password("encodedPassword")
                .role(List.of("USER"))
                .build();
    }

    @Test
    @DisplayName("Should return success message for test API")
    void testApi_ShouldReturnSuccessMessage() {
        // Act
        String result = authController.testApi();

        // Assert
        assertEquals("Auth Service is up and running", result);
    }

    @Test
    @DisplayName("Should send OTP for email verification")
    void requestEmailVerification_ShouldSendOtp_WhenEmailIsValid() {
        // Arrange
        doNothing().when(emailService).sendOtpToEmail(TEST_EMAIL);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.requestEmailVerification(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("OTP sent to email", response.getBody().getMessage());
        verify(emailService, times(1)).sendOtpToEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Should verify email successfully with valid OTP")
    void verifyEmailWithOtp_ShouldReturnSuccess_WhenOtpIsValid() {
        // Arrange
        when(emailService.verifyOTP(TEST_EMAIL, TEST_OTP)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.verifyEmailWithOtp(TEST_EMAIL, TEST_OTP);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Email verified successfully", response.getBody().getMessage());
        verify(emailService, times(1)).verifyOTP(TEST_EMAIL, TEST_OTP);
    }

    @Test
    @DisplayName("Should return bad request when OTP is invalid")
    void verifyEmailWithOtp_ShouldReturnBadRequest_WhenOtpIsInvalid() {
        // Arrange
        when(emailService.verifyOTP(TEST_EMAIL, TEST_OTP)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.verifyEmailWithOtp(TEST_EMAIL, TEST_OTP);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid or expired OTP", response.getBody().getMessage());
        verify(emailService, times(1)).verifyOTP(TEST_EMAIL, TEST_OTP);
    }

    @Test
    @DisplayName("Should register user successfully")
    void registerUser_ShouldReturnCreated_WhenUserRegistered() {
        // Arrange
        when(usersService.registerUser(TEST_EMAIL, TEST_PASSWORD)).thenReturn("User registered successfully");

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.registerUser(testUserDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(201, response.getBody().getStatus());
        assertEquals("User registered successfully", response.getBody().getMessage());
        verify(usersService, times(1)).registerUser(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void loginUser_ShouldReturnToken_WhenCredentialsAreCorrect() throws Exception {
        // Arrange
        when(usersService.loginUser(TEST_EMAIL, TEST_PASSWORD)).thenReturn(true);
        when(usersService.loadUserByUsername(TEST_EMAIL)).thenReturn(testUser);
        when(jwtToken.generateToken(TEST_EMAIL, testUser.getRole(), TEST_USER_ID)).thenReturn(TEST_TOKEN);

        // Act
        ResponseEntity<?> response = authController.loginUser(testUserDTO, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(TEST_TOKEN, responseBody.get("token"));
        assertEquals(TEST_EMAIL, responseBody.get("email"));
        assertEquals(testUser.getRole(), responseBody.get("role"));
        verify(usersService, times(1)).loginUser(TEST_EMAIL, TEST_PASSWORD);
        verify(usersService, times(1)).loadUserByUsername(TEST_EMAIL);
        verify(jwtToken, times(1)).generateToken(TEST_EMAIL, testUser.getRole(), TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return unauthorized when login credentials are incorrect")
    void loginUser_ShouldReturnUnauthorized_WhenCredentialsAreIncorrect() {
        // Arrange
        when(usersService.loginUser(TEST_EMAIL, TEST_PASSWORD)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.loginUser(testUserDTO, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<?> responseBody = (ApiResponse<?>) response.getBody();
        assertEquals(401, responseBody.getStatus());
        assertEquals("Invalid email or password", responseBody.getMessage());
        verify(usersService, times(1)).loginUser(TEST_EMAIL, TEST_PASSWORD);
        verify(usersService, never()).loadUserByUsername(anyString());
        verify(jwtToken, never()).generateToken(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Should send OTP for forget password")
    void forgetPassword_ShouldSendOtp_WhenEmailIsValid() {
        // Arrange
        doNothing().when(emailService).sendOtpToEmail(TEST_EMAIL);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.forgetPassword(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("OTP Send Successfully", response.getBody().getMessage());
        verify(emailService, times(1)).sendOtpToEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Should reset password successfully with valid OTP")
    void resetPassword_ShouldUpdatePassword_WhenOtpIsValid() {
        // Arrange
        String newPassword = "newPassword123";
        when(emailService.verifyOTP(TEST_EMAIL, TEST_OTP)).thenReturn(true);
        when(usersService.updatePassword(TEST_EMAIL, newPassword)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.resetPassword(TEST_EMAIL, TEST_OTP, newPassword);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("Password updated successfully", response.getBody().getMessage());
        verify(emailService, times(1)).verifyOTP(TEST_EMAIL, TEST_OTP);
        verify(usersService, times(1)).updatePassword(TEST_EMAIL, newPassword);
    }

    @Test
    @DisplayName("Should return bad request when resetting password with invalid OTP")
    void resetPassword_ShouldReturnBadRequest_WhenOtpIsInvalid() {
        // Arrange
        String newPassword = "newPassword123";
        when(emailService.verifyOTP(TEST_EMAIL, TEST_OTP)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.resetPassword(TEST_EMAIL, TEST_OTP, newPassword);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Invalid or expired OTP", response.getBody().getMessage());
        verify(emailService, times(1)).verifyOTP(TEST_EMAIL, TEST_OTP);
        verify(usersService, never()).updatePassword(anyString(), anyString());
    }

    @Test
    @DisplayName("Should add role to user when authorized")
    void changeRoles_ShouldAddRole_WhenUserExists() {
        // Arrange
        String role = "ADMIN";
        when(usersService.addNewRoles(TEST_USER_ID, role)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<?>> response = authController.changeRoles(TEST_USER_ID, role);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(201, response.getBody().getStatus());
        assertEquals("Roles Changed Successfully", response.getBody().getMessage());
        verify(usersService, times(1)).addNewRoles(TEST_USER_ID, role);
    }

    @Test
    @DisplayName("Should remove role from user when authorized")
    void removeRoles_ShouldRemoveRole_WhenUserExists() {
        // Arrange
        String role = "USER";
        when(usersService.removeRoles(TEST_USER_ID, role)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<?>> response = authController.removeRoles(TEST_USER_ID, role);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(201, response.getBody().getStatus());
        assertEquals("Roles Changed Successfully", response.getBody().getMessage());
        verify(usersService, times(1)).removeRoles(TEST_USER_ID, role);
    }

    @Test
    @DisplayName("Should handle login server error gracefully")
    void loginUser_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        // Arrange
        when(usersService.loginUser(TEST_EMAIL, TEST_PASSWORD)).thenReturn(true);
        when(usersService.loadUserByUsername(TEST_EMAIL)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authController.loginUser(testUserDTO, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Login failed due to server error", responseBody.get("message"));
        verify(usersService, times(1)).loginUser(TEST_EMAIL, TEST_PASSWORD);
        verify(usersService, times(1)).loadUserByUsername(TEST_EMAIL);
    }
}
