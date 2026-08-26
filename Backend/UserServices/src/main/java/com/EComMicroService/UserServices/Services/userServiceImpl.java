package com.ecommicroservice.userservices.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommicroservice.userservices.dto.UsersDTO;
import com.ecommicroservice.userservices.entity.UsersDetails;
import com.ecommicroservice.userservices.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class userServiceImpl implements userService {

    private static final String USER_SERVICE = "userService";

    @Autowired
    private UserRepository userRepository;

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "saveUserFallback")
    @Retry(name = USER_SERVICE)
    @Bulkhead(name = USER_SERVICE)
    @RateLimiter(name = USER_SERVICE)
    public UsersDetails saveUser(UsersDTO usersDTO) {
        UsersDetails users = UsersDetails.builder()
                .id(UUID.randomUUID().toString())
                .name(usersDTO.getName())
                .email(usersDTO.getEmail())
                .password(usersDTO.getPassword())
                .build();
        return userRepository.save(users);
    }

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getUserByIdFallback")
    @Retry(name = USER_SERVICE)
    @Bulkhead(name = USER_SERVICE)
    @RateLimiter(name = USER_SERVICE)
    public UsersDTO getUserById(String id) {
        userRepository.findById(id);
        return new UsersDTO();
    }

    // ============ FALLBACK METHODS ============

    public UsersDetails saveUserFallback(UsersDTO usersDTO, Throwable t) {
        log.error("Save user fallback triggered: {}", t.getMessage());
        throw new RuntimeException("User service is temporarily unavailable. Please try again later.", t);
    }

    public UsersDTO getUserByIdFallback(String id, Throwable t) {
        log.error("Get user by id fallback triggered for id {}: {}", id, t.getMessage());
        throw new RuntimeException("User service is temporarily unavailable. Please try again later.", t);
    }

}
