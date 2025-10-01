package com.mazemzemi.soldstack.user.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazemzemi.soldstack.common.security.JwtUtil;
import com.mazemzemi.soldstack.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.config.name=application-integrationTest-eureka")
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private String jwtToken;

    @BeforeEach
    void setup() {
        // Générer un token JWT valide pour les tests
        JwtUtil jwtUtil = new JwtUtil("k3F7jN1pY9X2b6QvM+RZ4W8vU2y0nL6hD9y5a7E4fG8=");
        Instant now = Instant.now();
        Date expiration = Date.from(now.plus(1, ChronoUnit.HOURS));
        jwtToken = jwtUtil.generateToken("test@example.com", expiration);
    }

    private String bearer() {
        return "Bearer " + jwtToken;
    }

    @Test
    @DisplayName("POST /users should create a new user")
    void createUser_shouldReturnCreatedUser() throws Exception {
        User user = new User(UUID.randomUUID(), "john_doe", "john@example.com");
        when(userService.createUser(user.getUsername(), user.getEmail()))
                .thenReturn(user);

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john_doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    @DisplayName("GET /users/{id} should return user by ID")
    void getUserById_shouldReturnUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id, "alice", "alice@example.com");
        when(userService.getUser(id)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", id)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("alice")))
                .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    @Test
    @DisplayName("GET /users should return all users")
    void getAllUsers_shouldReturnList() throws Exception {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "bob", "bob@example.com"),
                new User(UUID.randomUUID(), "charlie", "charlie@example.com")
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("bob")))
                .andExpect(jsonPath("$[1].username", is("charlie")));
    }

    @Test
    @DisplayName("DELETE /users/{id} should delete user")
    void deleteUser_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/v1/users/{id}", id)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk());
    }
}
