package com.mazemzemi.soldstack.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazemzemi.soldstack.auth.AuthApplication;
import com.mazemzemi.soldstack.auth.application.port.UserRepository;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.auth.presentation.dto.request.LoginRequest;
import com.mazemzemi.soldstack.auth.presentation.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {AuthApplication.class, AuthIntegrationTest.TestConfig.class})
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private User user;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public AuthService authService() {
            return Mockito.mock(AuthService.class); // 🔹 on mock AuthService pour certains tests
        }
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
    }

    @Test
    void register_shouldCreateUser() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "test@example.com", "password123");
        when(authService.register("test@example.com", "test@example.com", "password123")).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("User registered successfully.")))
                .andExpect(jsonPath("$.data.username", is("test@example.com")))
                .andExpect(jsonPath("$.data.email", is("test@example.com")));
    }

    @Test
    void login_shouldReturnJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message", is("Login successful.")));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void me_shouldReturnCurrentUser() throws Exception {
        when(authService.getUserByEmail("test@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.username", is("test@example.com")))
                .andExpect(jsonPath("$.data.email", is("test@example.com")));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void logout_shouldSucceed() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
