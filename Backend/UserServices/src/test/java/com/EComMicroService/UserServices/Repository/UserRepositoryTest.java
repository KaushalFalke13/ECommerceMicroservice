package com.ecommicroservice.userservices.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ecommicroservice.userservices.entity.UsersDetails;

@DataJpaTest
@DisplayName("User Repository Integration Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UsersDetails testUser;
    private String testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID().toString();
        testUser = UsersDetails.builder()
                .id(testUserId)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUser_ShouldPersistUser_WhenValid() {
        // Act
        UsersDetails saved = userRepository.save(testUser);

        // Assert
        assertNotNull(saved);
        assertEquals(testUser.getId(), saved.getId());
        assertEquals(testUser.getName(), saved.getName());
        assertEquals(testUser.getEmail(), saved.getEmail());
        assertEquals(testUser.getPassword(), saved.getPassword());
    }

    @Test
    @DisplayName("Should find user by ID when exists")
    void findById_ShouldReturnUser_WhenExists() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<UsersDetails> found = userRepository.findById(testUserId);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testUser.getName(), found.get().getName());
        assertEquals(testUser.getEmail(), found.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void findById_ShouldReturnEmpty_WhenUserNotFound() {
        // Act
        Optional<UsersDetails> found = userRepository.findById("non-existent-id");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_ShouldReturnUser_WhenExists() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        UsersDetails found = userRepository.findByEmail(testUser.getEmail());

        // Assert
        assertNotNull(found);
        assertEquals(testUser.getId(), found.getId());
        assertEquals(testUser.getName(), found.getName());
    }

    @Test
    @DisplayName("Should return null when email not found")
    void findByEmail_ShouldReturnNull_WhenEmailNotFound() {
        // Act
        UsersDetails found = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertNull(found);
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_ShouldUpdateFields() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        UsersDetails userToUpdate = userRepository.findById(testUserId).orElseThrow();
        userToUpdate.setName("Jane Doe");
        userToUpdate.setEmail("jane.doe@example.com");
        entityManager.flush();

        // Assert
        UsersDetails updated = userRepository.findById(testUserId).orElseThrow();
        assertEquals("Jane Doe", updated.getName());
        assertEquals("jane.doe@example.com", updated.getEmail());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void deleteById_ShouldRemoveUser() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        userRepository.deleteById(testUserId);
        entityManager.flush();

        // Assert
        Optional<UsersDetails> found = userRepository.findById(testUserId);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByEmail(testUser.getEmail());

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should check if user exists by email returns false when not exists")
    void existsByEmail_ShouldReturnFalse_WhenNotExists() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertFalse(exists);
    }
}
