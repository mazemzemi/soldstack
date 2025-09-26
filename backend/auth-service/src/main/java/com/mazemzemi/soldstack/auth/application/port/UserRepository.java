package com.mazemzemi.soldstack.auth.application.port;


import com.mazemzemi.soldstack.auth.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);
}

