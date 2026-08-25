package com.EComMicroService.AuthServices.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.EComMicroService.AuthServices.Entity.Users;
import com.EComMicroService.AuthServices.ExceptionHandler.EmailAlreadyExistsException;
import com.EComMicroService.AuthServices.Repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Users Service Unit Tests")
class UsersServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersServiceImpl usersService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String ENCODED_PASSWORD = "encodedPassword";
    private final String TEST_USER_ID = UUID.randomUUID().toString();
    private Users testUser;

    @BeforeEach
    void setUp() {
        List<String> roles = List.of("USER");
        testUser = Users.builder()
                .userId(TEST_USER_ID)
                .email(TEST_EMAIL)
                .password(ENCODED_PASSWORD)
                .role(roles)
                .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void registerUser_ShouldRegisterUser_WhenEmailIsNotRegistered() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(null);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        String result = usersService.registerUser(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertEquals("User registered successfully", result);
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should throw exception when email already registered")
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(testUser);

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class,
                () -> usersService.registerUser(TEST_EMAIL, TEST_PASSWORD));
        assertEquals("Email already registered", exception.getMessage());
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should login user successfully with correct credentials")
    void loginUser_ShouldReturnTrue_WhenCredentialsAreCorrect() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(testUser);
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // Act
        boolean result = usersService.loginUser(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertTrue(result);
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should fail login when user not found")
    void loginUser_ShouldReturnFalse_WhenUserNotFound() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        // Act
        boolean result = usersService.loginUser(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertFalse(result);
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should fail login when password is incorrect")
    void loginUser_ShouldReturnFalse_WhenPasswordIsIncorrect() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(testUser);
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // Act
        boolean result = usersService.loginUser(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertFalse(result);
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("Should update password successfully when user exists")
    void updatePassword_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        String newPassword = "newPassword123";
        String newEncodedPassword = "newEncodedPassword";
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(testUser);
        when(passwordEncoder.encode(newPassword)).thenReturn(newEncodedPassword);
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        boolean result = usersService.updatePassword(TEST_EMAIL, newPassword);

        // Assert
        assertTrue(result);
        assertEquals(newEncodedPassword, testUser.getPassword());
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should return false when updating password for non-existent user")
    void updatePassword_ShouldReturnFalse_WhenUserNotFound() {
        // Arrange
        when(usersRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        // Act
        boolean result = usersService.updatePassword(TEST_EMAIL, "newPassword");

        // Assert
        assertFalse(result);
        verify(usersRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should get user by ID when exists")
    void getUserById_ShouldReturnUser_WhenExists() {
        // Arrange
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));

        // Act
        Users result = usersService.getUserById(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(TEST_EMAIL, result.getEmail());
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return null when user not found by ID")
    void getUserById_ShouldReturnNull_WhenUserNotFound() {
        // Arrange
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act
        Users result = usersService.getUserById(TEST_USER_ID);

        // Assert
        assertNull(result);
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should add new role to user when role not already present")
    void addNewRoles_ShouldReturnTrue_WhenRoleAddedSuccessfully() {
        // Arrange
        String newRole = "ADMIN";
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        boolean result = usersService.addNewRoles(TEST_USER_ID, newRole);

        // Assert
        assertTrue(result);
        assertTrue(testUser.getRole().contains(newRole));
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should not add duplicate role to user")
    void addNewRoles_ShouldNotAddDuplicateRole() {
        // Arrange
        String existingRole = "USER";
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        boolean result = usersService.addNewRoles(TEST_USER_ID, existingRole);

        // Assert
        assertTrue(result);
        assertEquals(1, testUser.getRole().size()); // Only one role should exist
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should return false when adding role to non-existent user")
    void addNewRoles_ShouldReturnFalse_WhenUserNotFound() {
        // Arrange
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act
        boolean result = usersService.addNewRoles(TEST_USER_ID, "ADMIN");

        // Assert
        assertFalse(result);
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should remove role from user when role exists")
    void removeRoles_ShouldReturnTrue_WhenRoleRemovedSuccessfully() {
        // Arrange
        String roleToRemove = "USER";
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        boolean result = usersService.removeRoles(TEST_USER_ID, roleToRemove);

        // Assert
        assertTrue(result);
        assertFalse(testUser.getRole().contains(roleToRemove));
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should not remove role when role doesn't exist")
    void removeRoles_ShouldReturnTrue_WhenRoleDoesNotExist() {
        // Arrange
        String nonExistentRole = "ADMIN";
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        // Act
        boolean result = usersService.removeRoles(TEST_USER_ID, nonExistentRole);

        // Assert
        assertTrue(result);
        assertEquals(1, testUser.getRole().size()); // Role count unchanged
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should return false when removing role from non-existent user")
    void removeRoles_ShouldReturnFalse_WhenUserNotFound() {
        // Arrange
        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act
        boolean result = usersService.removeRoles(TEST_USER_ID, "ADMIN");

        // Assert
        assertFalse(result);
        verify(usersRepository, times(1)).findById(TEST_USER_ID);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should handle multiple roles correctly")
    void addAndRemoveRoles_ShouldHandleMultipleRolesCorrectly() {
        // Arrange
        Users userWithMultipleRoles = Users.builder()
                .userId(TEST_USER_ID)
                .email(TEST_EMAIL)
                .password(ENCODED_PASSWORD)
                .role(Arrays.asList("USER", "EDITOR"))
                .build();

        when(usersRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(userWithMultipleRoles));
        when(usersRepository.save(any(Users.class))).thenReturn(userWithMultipleRoles);

        // Act - Add ADMIN role
        boolean addResult = usersService.addNewRoles(TEST_USER_ID, "ADMIN");

        // Assert
        assertTrue(addResult);
        assertTrue(userWithMultipleRoles.getRole().contains("ADMIN"));
        assertEquals(3, userWithMultipleRoles.getRole().size());

        // Act - Remove EDITOR role
        boolean removeResult = usersService.removeRoles(TEST_USER_ID, "EDITOR");

        // Assert
        assertTrue(removeResult);
        assertFalse(userWithMultipleRoles.getRole().contains("EDITOR"));
        assertEquals(2, userWithMultipleRoles.getRole().size());

        verify(usersRepository, times(2)).findById(TEST_USER_ID);
        verify(usersRepository, times(2)).save(userWithMultipleRoles);
    }
}
