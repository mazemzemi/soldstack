package com.mazemzemi.soldstack.auth.application.port;

public interface TokenStore {
    void storeToken(String email, String token, long ttlSeconds);
    String getToken(String email);
    void removeToken(String email);
}
