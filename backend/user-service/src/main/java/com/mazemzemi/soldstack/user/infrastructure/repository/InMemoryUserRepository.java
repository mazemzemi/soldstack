package com.mazemzemi.soldstack.user.infrastructure.repository;

import com.mazemzemi.soldstack.user.domain.model.User;
import com.mazemzemi.soldstack.user.domain.repository.UserRepository;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(storage.get(email));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
