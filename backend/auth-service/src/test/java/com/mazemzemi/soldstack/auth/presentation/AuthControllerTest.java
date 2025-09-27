package com.mazemzemi.soldstack.auth.presentation;

import com.mazemzemi.soldstack.auth.application.AuthService;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.auth.presentation.dto.request.LoginRequest;
import com.mazemzemi.soldstack.auth.presentation.dto.request.RegisterRequest;
import com.mazemzemi.soldstack.auth.presentation.dto.response.AuthResponse;
import com.mazemzemi.soldstack.auth.presentation.dto.response.UserResponse;
import com.mazemzemi.soldstack.common.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPassword("secret"); // Même si on ne le retourne jamais
    }

    @Test
    void register_shouldReturnCreatedUserResponse() {
        RegisterRequest request = new RegisterRequest("john_doe", "john@example.com", "secret");

        when(authService.register(request.username(), request.email(), request.password())).thenReturn(user);

        ResponseEntity<ApiResponse<UserResponse>> response = authController.register(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().username()).isEqualTo("john@example.com");
        assertThat(response.getBody().message()).isEqualTo("User registered successfully.");

        verify(authService).register("john_doe", "john@example.com", "secret");
    }

    @Test
    void login_shouldReturnTokenResponse() {
        LoginRequest request = new LoginRequest("john@example.com", "secret");

        when(authService.login(request.email(), request.password())).thenReturn("fake-jwt-token");

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().token()).isEqualTo("fake-jwt-token");
        assertThat(response.getBody().message()).isEqualTo("Login successful.");

        verify(authService).login("john@example.com", "secret");
    }

    @Test
    void getCurrentUser_shouldReturnUserResponse() {
        when(authentication.getName()).thenReturn("john@example.com");
        when(authService.getUserByEmail("john@example.com")).thenReturn(user);

        ResponseEntity<ApiResponse<UserResponse>> response = authController.getCurrentUser(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().email()).isEqualTo("john@example.com");
        assertThat(response.getBody().message()).isEqualTo("Current user data retrieved successfully.");

        verify(authService).getUserByEmail("john@example.com");
    }

    @Test
    void logout_shouldReturnSuccessMessage() {
        when(authentication.getName()).thenReturn("john@example.com");

        ResponseEntity<ApiResponse<Void>> response = authController.logout(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Logged out successfully.");

        verify(authService).logout("john@example.com");
    }
}
