package com.mazemzemi.soldstack.auth.application;

import com.mazemzemi.soldstack.auth.application.port.TokenStore;
import com.mazemzemi.soldstack.auth.application.port.UserRepository;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.common.exception.ConflictException;
import com.mazemzemi.soldstack.common.exception.InvalidCredentialsException;
import com.mazemzemi.soldstack.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenStore tokenStore;

    private final String jwtSecret = "c29sZHN0YWNrLWJhY2tlbmQtYXV0aGVudGlmaWNhdGlvbi1zZXJ2aWNlLXNlY3JldC1rZXk=";


    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, tokenStore, jwtSecret);
    }

    @Test
    @DisplayName("Register should succeed for a new user")
    void register_shouldSucceed_whenUserIsNew() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = authService.register("testuser", "test@example.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Register should throw ConflictException if email already exists")
    void register_shouldThrowConflictException_whenEmailExists() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(User.builder().build()));

        // Act & Assert
        assertThrows(ConflictException.class, () -> {
            authService.register("testuser", "test@example.com", "password123");
        });
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login should succeed with valid credentials")
    void login_shouldSucceed_withValidCredentials() {
        // Arrange
        User user = User.builder().email("test@example.com").password("encodedPassword").build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act
        String token = authService.login("test@example.com", "password123");

        // Assert
        assertNotNull(token);
        assertFalse(token.isBlank());
        // Verify that the generated token is the one being stored.
        verify(tokenStore, times(1)).storeToken(eq("test@example.com"), eq(token), anyLong());
    }

    @Test
    @DisplayName("Login should throw NotFoundException for non-existent user")
    void login_shouldThrowNotFoundException_forNonExistentUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            authService.login("nouser@example.com", "password123");
        });
    }

    @Test
    @DisplayName("Login should throw InvalidCredentialsException for wrong password")
    void login_shouldThrowInvalidCredentialsException_forWrongPassword() {
        // Arrange
        User user = User.builder().email("test@example.com").password("encodedPassword").build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login("test@example.com", "wrongpassword");
        });
        verify(tokenStore, never()).storeToken(anyString(), anyString(), anyLong());
    }
}