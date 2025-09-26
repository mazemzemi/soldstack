package com.mazemzemi.soldstack.user.application;

import com.mazemzemi.soldstack.user.application.exception.UserNotFoundException;
import com.mazemzemi.soldstack.user.domain.model.User;
import com.mazemzemi.soldstack.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User createUser(String username, String email) {
        User user = new User(UUID.randomUUID(), username, email);
        return repository.save(user);
    }

    public User getUser(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void deleteUser(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        repository.delete(id);
    }
}
