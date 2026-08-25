package com.EComMicroService.UserServices.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Entity.UsersDetails;
import com.EComMicroService.UserServices.Repository.UserRepository;
import com.EComMicroService.UserServices.Services.userServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private userServiceImpl userService;

    private UsersDTO testUserDTO;
    private UsersDetails testUserDetails;
    private String testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID().toString();

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
    @DisplayName("Should save user successfully")
    void saveUser_ShouldReturnSavedUser_WhenValidDTO() {
        // Arrange
        when(userRepository.save(any(UsersDetails.class))).thenReturn(testUserDetails);

        // Act
        UsersDetails result = userService.saveUser(testUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDetails.getId(), result.getId());
        assertEquals(testUserDetails.getName(), result.getName());
        assertEquals(testUserDetails.getEmail(), result.getEmail());
        assertEquals(testUserDetails.getPassword(), result.getPassword());
        verify(userRepository, times(1)).save(any(UsersDetails.class));
    }

    @Test
    @DisplayName("Should generate a unique ID when saving user")
    void saveUser_ShouldGenerateUniqueId_WhenSavingNewUser() {
        // Arrange
        when(userRepository.save(any(UsersDetails.class))).thenAnswer(invocation -> {
            UsersDetails saved = invocation.getArgument(0);
            assertNotNull(saved.getId());
            return saved;
        });

        // Act
        UsersDetails result = userService.saveUser(testUserDTO);

        // Assert
        assertNotNull(result.getId());
        verify(userRepository, times(1)).save(any(UsersDetails.class));
    }

    @Test
    @DisplayName("Should map DTO fields to entity correctly")
    void saveUser_ShouldMapDTOToEntityCorrectly() {
        // Arrange
        when(userRepository.save(any(UsersDetails.class))).thenAnswer(invocation -> {
            UsersDetails saved = invocation.getArgument(0);
            saved.setId(testUserId);
            return saved;
        });

        // Act
        UsersDetails result = userService.saveUser(testUserDTO);

        // Assert
        assertEquals(testUserDTO.getName(), result.getName());
        assertEquals(testUserDTO.getEmail(), result.getEmail());
        assertEquals(testUserDTO.getPassword(), result.getPassword());
    }

    @Test
    @DisplayName("Should handle null DTO fields gracefully")
    void saveUser_ShouldHandleNullFields() {
        // Arrange
        UsersDTO nullDTO = new UsersDTO();
        nullDTO.setName(null);
        nullDTO.setEmail(null);
        nullDTO.setPassword(null);

        when(userRepository.save(any(UsersDetails.class))).thenAnswer(invocation -> {
            UsersDetails saved = invocation.getArgument(0);
            return saved;
        });

        // Act
        UsersDetails result = userService.saveUser(nullDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        verify(userRepository, times(1)).save(any(UsersDetails.class));
    }

    @Test
    @DisplayName("Should return empty UsersDTO when getting user by ID (current implementation)")
    void getUserById_ShouldReturnEmptyDTO_ForAnyId() {
        // Act
        UsersDTO result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Should call repository findById when getting user by ID")
    void getUserById_ShouldCallRepositoryFindById() {
        // Act
        userService.getUserById(testUserId);

        // Assert
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Should handle non-existent user ID gracefully")
    void getUserById_ShouldHandleNonExistentId() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        UsersDTO result = userService.getUserById(nonExistentId);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        verify(userRepository, times(1)).findById(nonExistentId);
    }
}
