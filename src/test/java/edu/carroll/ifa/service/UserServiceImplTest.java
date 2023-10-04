package edu.carroll.ifa.service;

import java.util.List;

import edu.carroll.ifa.IfaApplication;
import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.*;

/**
 * Unit test for the UserServiceImpl class to make sure we are interacting with the database correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IfaApplication.class)
@Transactional
public class UserServiceImplTest {

    private static final String username1 = "bob_johnson";
    private static final String username2 = "ryan_daniels";
    private static final String password1 = "1234";
    private static final String password2 = "password";
    private static final String fname1 = "Bob";
    private static final String fname2 = "Ryan";
    private static final String lname1 = "Johnson";
    private static final String lname2 = "Daniels";
    private static final Integer age1 = 17;
    private static final Integer age2 = 14;
    private final User fakeUser1 = new User(username1, password1, fname1, lname1, age1);
    private final User fakeUser2 = new User(username2, password2, fname2, lname2, age2);
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

        // Ensure fake records are in DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username1);
        if (users.isEmpty())
            userService.saveUser(fakeUser1);

        final List<User> users2 = userRepo.findByUsernameIgnoreCase(username2);
        if (users2.isEmpty())
            userService.saveUser(fakeUser2);
    }

    /**
     * This unit test checks to see if a user can successfully be validated in the database provided its raw password and
     * username.
     */
    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", userService.validateUser(username1, password1));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided an incorrect
     * incorrect username and a correct password.
     */
    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, but a valid password", userService.validateUser(username1 + "not", password1));
    }

    /**
     * This unit test checks to see that a valid user and an incorrect username
     */
    @Test
    public void validateUserValidUserInvalidPasswordTest() {
        assertFalse("validateUserValidUserInvalidPasswordTest: should fail using a valid user, and an invalid password", userService.validateUser(username1, password1+"not"));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided an incorrect
     * password and an incorrect username
     */
    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username1 + "not", password1 + "extra"));
    }

    /**
     * This unit test checks to see that a null user cannot successfully be validated in the database provided a password that exists in the db.
     */
    @Test
    public void validateUserNullUserInvalidPasswordTest() {
        assertFalse("validateUserNullUserInvalidPasswordTest: should fail using a null user, and a valid password", userService.validateUser(null, password1));
    }

    /**
     * This unit test checks to see that a valid user cannot successfully be validated in the database with a valid username and a null password.
     */
    @Test
    public void validateUserValidUserNullPasswordTest() {
        assertFalse("validateUserValidUserNullPasswordTest: should fail using a valid username, and a null password", userService.validateUser(username1, null));
    }

    /**
     * This unit test checks to see that a user cannot log in with their valid username and someone elses valid password.
     */
    @Test
    public void validateUserValidUser1ValidPassword2Test() {
        assertFalse("validateUserValidUser1ValidPassword2Test: should fail using a valid username from fakeUser1, and a valid password from fakeUser2", userService.validateUser(username1, password2));
    }

    /**
     *
     */

    /**
     * This unit test checks to see that a user cannot be saved into the database if they already exist in the database.
     */
    @Test
    public void saveUserExistingUserTest() {
        assertFalse("saveUserExistingUserTest: should fail using a user already in db", userService.saveUser(fakeUser1));
    }

    /**
     * This unit test checks to see that a new user can be saved into the database if they do not already exist in the databse.
     */
    @Test
    public void saveUserNewUserTest() {
        User newUser = new User("new" + username1, password1, fname1, lname1, age1);
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
        assertEquals("getUserAgeTestExistingUserTest: should equal the fakeUsers age, 17", age1, userService.getUserAge(fakeUser1.getUsername()));
    }

    /**
     * This unit test checks to see that an age of -1 is returned if there is no user in the database.
     */
    @Test
    public void getUserAgeTestNonUserTest() {
        User newUser = new User("new1" + username1, password1, fname1, lname1, age1);
        assertEquals("getUserAgeTestNonUserTest: should equal -1 since user is not in db", -1, userService.getUserAge(newUser.getUsername()));
    }
}
