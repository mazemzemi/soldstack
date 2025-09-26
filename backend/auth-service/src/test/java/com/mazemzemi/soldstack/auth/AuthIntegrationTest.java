package com.mazemzemi.soldstack.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazemzemi.soldstack.auth.application.port.UserRepository;
import com.mazemzemi.soldstack.auth.interfaces.dto.request.LoginRequest;
import com.mazemzemi.soldstack.auth.interfaces.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
class AuthIntegrationTest {


    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * Configure dynamiquement les propriétés Spring pour se connecter au conteneur Redis.
     * C'est la méthode la plus robuste pour éviter les conflits de ports.
     */
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
    }

    /**
     * Nettoie la base de données avant chaque test pour garantir l'isolation.
     */
    @BeforeEach
    void setUp() {
        // Dans un vrai projet avec une base de données SQL, vous nettoieriez les tables ici.
        // Pour notre cas, si le UserRepository était en mémoire, on le viderait.
        // Si c'est une autre implémentation, assurez-vous qu'elle est propre.
    }

    @Test
    void shouldRegisterLoginAndLogoutSuccessfully() throws Exception {
        // --- 1. Inscription (Register) ---
        var registerRequest = new RegisterRequest("testuser", "test@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated()) // Vérifie le statut HTTP 201
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("User registered successfully.")))
                .andExpect(jsonPath("$.data.username", is("testuser")))
                .andExpect(jsonPath("$.data.email", is("test@example.com")));

        // --- 2. Connexion (Login) ---
        var loginRequest = new LoginRequest("test@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // Vérifie le statut HTTP 200
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.token", notNullValue())); // Vérifie que le token est bien présent

        // --- 3. Tentative de connexion avec un mauvais mot de passe ---
        var badLoginRequest = new LoginRequest("test@example.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badLoginRequest)))
                .andExpect(status().isUnauthorized()); // Vérifie que l'accès est non autorisé
    }

    @Test
    void shouldFailRegistrationWhenEmailAlreadyExists() throws Exception {
        // Arrange: Crée un premier utilisateur
        var request = new RegisterRequest("user1", "duplicate@example.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Act & Assert: Tente de créer un deuxième utilisateur avec le même email
        var duplicateRequest = new RegisterRequest("user2", "duplicate@example.com", "password456");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict()) // Attend une erreur 409 Conflict
                .andExpect(jsonPath("$.message", is("An account with this email already exists.")));
    }
}