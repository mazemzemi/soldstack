package com.mazemzemi.soldstack.auth.infrastructure.persistence.db;

import com.mazemzemi.soldstack.auth.application.port.UserRepository;
import com.mazemzemi.soldstack.auth.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        usersByEmail.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersByEmail.values());
    }

    @Override
    public void deleteAll() {
        usersByEmail.clear();
    }
}

