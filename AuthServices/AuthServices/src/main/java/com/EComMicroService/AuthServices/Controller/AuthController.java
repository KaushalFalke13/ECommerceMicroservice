package com.EComMicroService.AuthServices.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.EComMicroService.AuthServices.DTO.ApiResponse;
import com.EComMicroService.AuthServices.DTO.UsersDTO;
import com.EComMicroService.AuthServices.Service.EmailVerificationService;
import com.EComMicroService.AuthServices.Service.JwtToken;
import com.EComMicroService.AuthServices.Service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersService usersService;
    private final EmailVerificationService emailService;
    private final JwtToken JwtToken;

    public AuthController(UsersService usersService, EmailVerificationService emailService, JwtToken JwtToken) {
        this.usersService = usersService;
        this.emailService = emailService;
        this.JwtToken = JwtToken;
    }

    @GetMapping("/login")
    public String login() {
        return "login Page";
    }

    @GetMapping("/SignUp")
    public String signUpPage() {
        return "SignUp Page";
    }

    @PostMapping("/requestEmailVerification")
    public ResponseEntity<ApiResponse<Void>> requestEmailVerification(
            @RequestParam @Valid @Email(message = "Invalid email") String email) {
        emailService.sendOtpToEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(200, "OTP sent to email"));
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<ApiResponse<Void>> verifyEmailWithOtp(
            @RequestParam @Email(message = "Invalid email") String email, @RequestParam String otp) {

        boolean verified = emailService.verifyOTP(email, otp);
        if (verified) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Email verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Invalid or expired OTP"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody UsersDTO userDTO) {
        usersService.registerUser(userDTO.getEmail(), userDTO.getPassword());
        return new ResponseEntity<>(new ApiResponse<>(201, "User registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> loginUser(@Valid @RequestBody UsersDTO userDTO,
            HttpServletResponse response) {
        boolean isUserVerified = usersService.loginUser(userDTO.getEmail(), userDTO.getPassword());
        if (!isUserVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Invalid email or password"));
        }
        try {
            String token = JwtToken.generateToken(userDTO.getEmail());
            response.setHeader("Authorization", "Bearer " + token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Login successful"));
    }

    @GetMapping("/forgetPassword")
    public ResponseEntity<ApiResponse<Void>> forgetPassword(
            @RequestParam @Valid @Email(message = "Invalid email") String email) {
        emailService.sendOtpToEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(200, "OTP Send Successfully"));
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam @Email(message = "Invalid email") String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        boolean isOtpValid = emailService.verifyOTP(email, otp);
        if (!isOtpValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Invalid or expired OTP"));
        }
        usersService.updatePassword(email, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(200, "Password updated successfully"));
    }
}
