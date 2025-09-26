package com.mazemzemi.soldstack.user.domain.repository;

import com.mazemzemi.soldstack.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    void delete(UUID id);
}
