package com.mazemzemi.soldstack.user.application.presentation;


import com.mazemzemi.soldstack.user.application.UserService;
import com.mazemzemi.soldstack.user.domain.model.User;
import com.mazemzemi.soldstack.user.presentation.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
    }

    @Test
    void create_shouldReturnCreatedUser() {
        when(service.createUser(user.getUsername(), user.getEmail()))
                .thenReturn(user);

        User result = controller.create(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john_doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");

        verify(service).createUser("john_doe", "john@example.com");
    }

    @Test
    void getById_shouldReturnUser() {
        when(service.getUser(user.getId())).thenReturn(user);

        User result = controller.getById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());

        verify(service).getUser(user.getId());
    }

    @Test
    void getAll_shouldReturnListOfUsers() {
        when(service.getAllUsers()).thenReturn(List.of(user));

        List<User> result = controller.getAll();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("john_doe");

        verify(service).getAllUsers();
    }

    @Test
    void delete_shouldCallServiceDelete() {
        UUID id = user.getId();

        controller.delete(id);

        verify(service).deleteUser(id);
    }
}
