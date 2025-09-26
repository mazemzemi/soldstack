package com.mazemzemi.soldstack.user.application;

import com.mazemzemi.soldstack.user.application.exception.UserNotFoundException;
import com.mazemzemi.soldstack.user.domain.model.User;
import com.mazemzemi.soldstack.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("mohamed")
                .email("mohamed@example.com")
                .build();
    }

    @Test
    @DisplayName("createUser should save and return a new user with correct details")
    void createUser_ShouldReturnUser() {
        // Arrange
        // Le mock de 'save' doit retourner l'objet qu'il reçoit pour simuler un enregistrement réel
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User created = userService.createUser("mohamed", "mohamed@example.com");

        // Assert
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        assertNotNull(savedUser);
        assertEquals("mohamed", savedUser.getUsername());
        assertEquals("mohamed@example.com", savedUser.getEmail());

        assertEquals(savedUser, created);
    }

    @Test
    @DisplayName("getUser should return user when found")
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User found = userService.getUser(user.getId());

        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    @DisplayName("getUser should throw UserNotFoundException when not found")
    void getUserById_ShouldThrowException_WhenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(randomId));
    }

    @Test
    @DisplayName("getAllUsers should return a list of users")
    void getAllUsers_ShouldReturnList() {
        List<User> list = List.of(user);
        when(userRepository.findAll()).thenReturn(list);

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deleteUser should call repository delete when user exists")
    void deleteUser_ShouldCallRepository() {
        // Arrange: S'assurer que l'utilisateur existe avant de le supprimer
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(user.getId());

        // Assert: Vérifier que la méthode delete a bien été appelée avec le bon ID
        verify(userRepository, times(1)).delete(user.getId());
    }

    @Test
    @DisplayName("deleteUser should throw UserNotFoundException when user does not exist")
    void deleteUser_ShouldThrowException_WhenNotFound() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(randomId));
        // S'assurer que la méthode delete n'est jamais appelée si l'utilisateur n'existe pas
        verify(userRepository, never()).delete(any(UUID.class));
    }
}