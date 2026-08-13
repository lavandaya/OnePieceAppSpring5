package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.Role;
import be.kdg.programming5.onepiece.business.domain.User;
import be.kdg.programming5.onepiece.business.exception.UsernameTakenException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_newUsername_savesUserWithHashedPasswordAndUserRole() {
        User registered = userService.register("robin", "robin@strawhat.com", "raw-password");

        assertThat(registered.getRole()).isEqualTo(Role.USER);
        assertThat(registered.getPassword()).isNotEqualTo("raw-password");
        assertThat(passwordEncoder.matches("raw-password", registered.getPassword())).isTrue();
    }

    @Test
    void register_takenUsername_throwsUsernameTakenException() {
        userService.register("robin", "robin@strawhat.com", "raw-password");

        assertThatThrownBy(() -> userService.register("robin", "other@strawhat.com", "other-password"))
                .isInstanceOf(UsernameTakenException.class);
    }
}