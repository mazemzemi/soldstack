package com.mazemzemi.soldstack.user.infrastructure.config;

import com.mazemzemi.soldstack.user.application.UserService;
import com.mazemzemi.soldstack.user.domain.repository.UserRepository;
import com.mazemzemi.soldstack.user.infrastructure.repository.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public UserService userService(UserRepository repository) {
        return new UserService(repository);
    }
}
