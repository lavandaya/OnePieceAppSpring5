package be.kdg.programming5.onepiece.business.service.impl;

import be.kdg.programming5.onepiece.business.domain.User;
import be.kdg.programming5.onepiece.business.exception.UsernameTakenException;
import be.kdg.programming5.onepiece.business.service.UserService;
import be.kdg.programming5.onepiece.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.kdg.programming5.onepiece.business.domain.Role;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    User user = new User(username, passwordEncoder.encode(rawPassword), email, Role.USER);
    @Override
    public Optional<User> getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public boolean usernameExists(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User register(String username, String email, String rawPassword) {
        if (repository.existsByUsername(username)) {
            throw new UsernameTakenException(username);
        }
        User user = new User(username, passwordEncoder.encode(rawPassword), email);
        repository.save(user);
        logger.debug("Registered new user {}", user);
        return user;
    }
}