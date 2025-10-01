package com.mazemzemi.soldstack.auth.application;

import com.mazemzemi.soldstack.auth.application.port.TokenStore;
import com.mazemzemi.soldstack.auth.application.port.UserRepository;
import com.mazemzemi.soldstack.auth.domain.model.User;
import com.mazemzemi.soldstack.common.exception.ConflictException;
import com.mazemzemi.soldstack.common.exception.InvalidCredentialsException;
import com.mazemzemi.soldstack.common.exception.NotFoundException;
import com.mazemzemi.soldstack.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthService {

    public static final long TTL_SECONDS_ONE_HOUR = 3600;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenStore tokenStore,
                       @Value("${jwt.secret}") String jwtSecret) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.jwtUtil = new JwtUtil(jwtSecret);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        final Instant now = Instant.now();
        final Date expiration = Date.from(now.plus(TTL_SECONDS_ONE_HOUR, ChronoUnit.SECONDS));

        // Génération du JWT avec JwtUtil
        String token = jwtUtil.generateToken(user.getEmail(), expiration);

        // Stockage du token (optionnel si tu fais du blacklist / logout)
        tokenStore.storeToken(user.getEmail(), token, TTL_SECONDS_ONE_HOUR);

        return token;
    }

    public User register(String username, String email, String password) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ConflictException("An account with this email already exists.");
        });

        User newUser = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("USER")
                .build();

        return userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
    }

    public void logout(String email) {
        tokenStore.removeToken(email);
    }
}
