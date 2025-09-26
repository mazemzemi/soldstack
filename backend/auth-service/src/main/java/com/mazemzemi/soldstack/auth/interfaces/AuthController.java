package com.mazemzemi.soldstack.auth.interfaces;

import com.mazemzemi.soldstack.auth.application.AuthService;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.auth.interfaces.dto.request.LoginRequest;
import com.mazemzemi.soldstack.auth.interfaces.dto.request.RegisterRequest;
import com.mazemzemi.soldstack.auth.interfaces.dto.response.AuthResponse;
import com.mazemzemi.soldstack.auth.interfaces.dto.response.UserResponse;
import com.mazemzemi.soldstack.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        User newUser = authService.register(request.username(), request.email(), request.password());

        // Mapper l'entité User vers un DTO de réponse pour ne pas exposer le mot de passe
        UserResponse userResponse = new UserResponse(newUser.getId(), newUser.getUsername(), newUser.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(userResponse, "User registered successfully."));
    }

    /**
     * Endpoint pour la connexion.
     * Amélioré pour utiliser un corps de requête JSON (plus sécurisé).
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful."));
    }

    /**
     * Endpoint pour la déconnexion.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam String email) {
        authService.logout(email);
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully."));
    }
}

