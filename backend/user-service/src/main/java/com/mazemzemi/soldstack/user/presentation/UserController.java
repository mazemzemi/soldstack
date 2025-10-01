package com.mazemzemi.soldstack.user.presentation;

import com.mazemzemi.soldstack.user.application.UserService;
import com.mazemzemi.soldstack.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public User create(@RequestBody User user) {
        return service.createUser(user.getUsername(), user.getEmail());
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return service.getUser(id);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteUser(id);
    }
}
