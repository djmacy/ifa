package edu.carroll.ifa.service;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.List;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
public class UserServiceImplTest {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String username = "bob_johnson";
    private static final String password = passwordEncoder.encode("1234");
    private static final String fname = "Bob";
    private static final String lname = "Johnson";
    private static final Integer age = 17;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    private User fakeUser = new User(username, password, fname, lname, age);

    @BeforeEach
    public void beforeTest() {
        assertNotNull("userRepository must be injected", userRepo);
        assertNotNull("userService must be injected", userService);

        // Ensure fake record is in DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty())
            userRepo.save(fakeUser);
    }
/*
    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using th same user/pass info", userService.validateUser(username, password));
    }
 */
}
