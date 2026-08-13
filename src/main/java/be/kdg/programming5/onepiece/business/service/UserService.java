package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);
    boolean usernameExists(String username);
    User register(String username, String email, String rawPassword);
}