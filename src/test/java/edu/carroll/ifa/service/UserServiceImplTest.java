package edu.carroll.ifa.service;

import java.util.List;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class UserServiceImplTest {

    private static final String username = "bob_johnson";
    private static final String password = "1234";
    private static final String fname = "Bob";
    private static final String lname = "Johnson";
    private static final Integer age = 17;
    private User fakeUser = new User(username, password, fname, lname, age);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void beforeTest() {
        assertNotNull("userRepository must be injected", userRepo);
        assertNotNull("userService must be injected", userService);

        // Ensure fake record is in DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty())
            userRepo.save(fakeUser);
    }

    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", userService.validateUser(username, password));
    }

    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, but a valid password", userService.validateUser(username + "not", password));
    }

    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password + "extra"));
    }

    @Test
    public void saveUserExistingUserTest() {
        assertFalse("saveUserExistingUserTest: should fail using a user already in db", userService.saveUser(fakeUser));
    }

    @Test
    public void saveUserNewUserTest() {
        User newUser = new User("new" + username, password, fname, lname, age);
        assertTrue("saveUserNewUserTest: should succeed using a new user", userService.saveUser(newUser));
        //deletes the user for the next time we run this test so he is not already there
        userService.deleteUser(newUser.getUsername());

    }
    /*
    @Test
    public void saveUserAgeTest() {
        Integer newAge = 10;
        User newUser = new User("new" + username, password, fname, lname, age);
        assertTrue("saveUserAgeTest: should succeed saving a new age with user in db", userService.saveUserAge(fakeUser, newAge));
    }

     */

    @Test
    public void getUserAgeTestExistingUserTest() {
        assertEquals("getUserAgeTestExistingUserTest: should equal the fakeUsers age, 17", age, userService.getUserAge(fakeUser.getUsername()));
    }

    @Test
    public void getUserAgeTestNonUserTest() {
        User newUser = new User("new1" + username, password, fname, lname, age);
        assertEquals("getUserAgeTestNonUserTest: should equal -1 since user is not in db", -1, userService.getUserAge(newUser.getUsername()));
    }

}
