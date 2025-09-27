package com.mazemzemi.soldstack.auth.application.port;


import com.mazemzemi.soldstack.auth.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteAll();
}

