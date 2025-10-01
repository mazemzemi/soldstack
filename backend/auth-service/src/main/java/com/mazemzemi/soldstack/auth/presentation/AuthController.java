package com.mazemzemi.soldstack.auth.presentation;

import com.mazemzemi.soldstack.auth.application.AuthService;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.auth.presentation.dto.request.LoginRequest;
import com.mazemzemi.soldstack.auth.presentation.dto.request.RegisterRequest;
import com.mazemzemi.soldstack.auth.presentation.dto.response.AuthResponse;
import com.mazemzemi.soldstack.auth.presentation.dto.response.UserResponse;
import com.mazemzemi.soldstack.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
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
     * Endpoint pour récupérer les informations de l'utilisateur actuellement authentifié.
     * Nécessite un token JWT valide dans l'en-tête "Authorization".
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        // Le nom du principal est l'email de l'utilisateur, défini par notre filtre JWT.
        String email = authentication.getName();
        User currentUser = authService.getUserByEmail(email);

        // Mapper vers le DTO de réponse pour ne pas exposer les données sensibles.
        UserResponse userResponse = new UserResponse(currentUser.getId(), currentUser.getUsername(), currentUser.getEmail());

        return ResponseEntity.ok(ApiResponse.success(userResponse, "Current user data retrieved successfully."));
    }

    /**
     * Endpoint pour la déconnexion.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        String email = authentication.getName();
        authService.logout(email);
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully."));
    }
}

