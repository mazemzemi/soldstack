package com.mazemzemi.soldstack.user;

import com.mazemzemi.soldstack.user.application.UserService;
import com.mazemzemi.soldstack.user.domain.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userService.createUser("integration", "integration@example.com");
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(testUser.getId());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        ResponseEntity<User> response = restTemplate.getForEntity(
                "/users/" + testUser.getId(),
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        User newUser = User.builder()
                .username("newuser")
                .email("newuser@example.com")
                .build();

        ResponseEntity<User> response = restTemplate.postForEntity("/users", newUser, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());

        // cleanup
        userService.deleteUser(response.getBody().getId());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        User tmpUser = userService.createUser("tmp", "tmp@example.com");

        restTemplate.delete("/users/" + tmpUser.getId());

        assertThrows(RuntimeException.class, () -> userService.getUser(tmpUser.getId()));
    }
}
