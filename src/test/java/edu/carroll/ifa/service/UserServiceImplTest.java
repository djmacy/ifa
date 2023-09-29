package edu.carroll.ifa.service;

import java.util.List;
import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.*;

/**
 * Unit test for the UserServiceImpl class to make sure we are interacting with the database correctly.
 */
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

    /**
     * Before each test is run we ensure that the UserRepository and UserService have been injected and that fakeUser is
     * in the database.
     */
    @BeforeEach
    public void beforeTest() {
        assertNotNull("userRepository must be injected", userRepo);
        assertNotNull("userService must be injected", userService);

        // Ensure fake record is in DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty())
            userRepo.save(fakeUser);
    }

    /**
     * This unit test checks to see if a user can successfully be validated in the database provided its raw password and
     * username.
     */
    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", userService.validateUser(username, password));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided an incorrect
     * password and a correct username.
     */
    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, but a valid password", userService.validateUser(username + "not", password));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided a correct
     * password and an incorrect username
     */
    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password + "extra"));
    }

    /**
     * This unit test checks to see that a user cannot be saved into the database if they already exist in the database.
     */
    @Test
    public void saveUserExistingUserTest() {
        assertFalse("saveUserExistingUserTest: should fail using a user already in db", userService.saveUser(fakeUser));
    }

    /**
     * This unit test checks to see that a new user can be saved into the database if they do not already exist in the databse.
     */
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
        Integer newAge = 11;
        User newUser = new User("new" + username, password, fname, lname, age);
        assertTrue("saveUserAgeTest: should succeed saving a new age with user in db", userService.saveUserAge(newUser, newAge));
        //change age back to original age so the test will pass if rerun
        userService.saveUserAge(newUser, age);
    }
     */

    /**
     * This unit test checks to see that the user age matches the expected age of the user in the database
     */
    @Test
    public void getUserAgeTestExistingUserTest() {
        assertEquals("getUserAgeTestExistingUserTest: should equal the fakeUsers age, 17", age, userService.getUserAge(fakeUser.getUsername()));
    }

    /**
     * This unit test checks to see that an age of -1 is returned if there is no user in the database.
     */
    @Test
    public void getUserAgeTestNonUserTest() {
        User newUser = new User("new1" + username, password, fname, lname, age);
        assertEquals("getUserAgeTestNonUserTest: should equal -1 since user is not in db", -1, userService.getUserAge(newUser.getUsername()));
    }
}
