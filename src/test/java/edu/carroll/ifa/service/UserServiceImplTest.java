package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.*;

/**
 * Unit test for the UserServiceImpl class to make sure we are interacting with the database correctly.
 */
@SpringBootTest
@Transactional
public class UserServiceImplTest {
    private static final String username1 = "bob_johnson";
    private static final String username2 = "ryan_daniels";
    private static final String icelandicName = "Davíð";
    private static final String arabicName = "ديفيد";
    private static final String mandarinName = "大衛";
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

    /**
     * Before each test is run we ensure that the UserRepository and UserService have been injected and that fakeUser is
     * in the database.
     */
    @BeforeEach
    public void beforeTest() {

        //Ensure fake records are in DB
        //It is actually not a hashed password until after the user is saved.
        boolean exists = userService.validateUser(fakeUser1.getUsername(), fakeUser1.getHashedPassword());
        //if the user does not exist in the database save the fakeUser.
        if (!exists)
            userService.saveUser(fakeUser1);

        exists = userService.validateUser(fakeUser2.getUsername(), fakeUser2.getHashedPassword());
        if (!exists)
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
     * username and a valid password.
     */
    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, but a valid password", userService.validateUser(username1 + "not", password1));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided a valid
     * username and an incorrect password.
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
     * This unit test checks to see that a null username and null password can not be validated
     */
    @Test
    public void validateUserNullUserNullPasswordTest() {
        assertFalse("validateUserNullUserNullPasswordTest: should fail to validate a user with null parameters", userService.validateUser(null, null));
    }

    /**
     * This unit test checks to see that a user cannot log in with their valid username and someone elses valid password.
     */
    @Test
    public void validateUserValidUser1ValidPassword2Test() {
        assertFalse("validateUserValidUser1ValidPassword2Test: should fail using a valid username from fakeUser1, and a valid password from fakeUser2", userService.validateUser(username1, password2));
    }

    /**
     * This unit test checks to see that multiple users can be validated
     */
    @Test
    public void validateMultipleUsersTest() {
        assertTrue("validateMultipleUser1Test: should be able to validate the first user: ", userService.validateUser(username1, password1));
        assertTrue("validateMultipleUser2Test: should be able to validate the second user as well: ", userService.validateUser(username2, password2));
    }

    /**
     * This unit test checks to see that foreign users can be validated in the database
     */
    @Test
    public void validateUserForeignUserValidPassword() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        assertTrue("Icelandic user should be added to database",userService.saveUser(icelandicUser));
        assertTrue("validateUserIcelandicUsernameValidPassword: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));

        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        assertTrue("Arabic user should be added to database",userService.saveUser(arabicUser));
        assertTrue("validateUserArabicUsernameValidPassword: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));

        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);
        assertTrue("Mandarin user should be added to database",userService.saveUser(mandarinUser));
        assertTrue("validateUserChineseUsernameValidPasswordTest: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }

    /**
     *
     */
    @Test
    public void validateUserForeignUserDifferentOrderTest() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);

        assertTrue("Icelandic user should be added to database",userService.saveUser(icelandicUser));
        assertTrue("Arabic user should be added to database",userService.saveUser(arabicUser));
        assertTrue("Mandarin user should be added to database",userService.saveUser(mandarinUser));

        assertTrue("validateUserIcelandicUsernameValidPassword: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));
        assertTrue("validateUserArabicUsernameValidPassword: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));
        assertTrue("validateUserChineseUsernameValidPasswordTest: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }


    /**
     * This unit test checks to see that a user cannot be saved into the database if they already exist in the database.
     */
    @Test

    public void saveUserExistingUserTest() {
        assertFalse("saveUserExistingUserTest: should fail using a user already in db", userService.saveUser(fakeUser1));
    }

    /**
     * This unit test checks to see that a new user can be saved into the database if they do not already exist in the database.
     */
    @Test
    public void saveUserNewUserTest() {

        User newUser = new User("new" + username1, password1, fname1, lname1, age1);
        assertTrue("saveUserNewUserTest: should succeed using a new user", userService.saveUser(newUser));
    }

    /**
     * This unit test checks to see that a null value cannot be saved to the database
     */
    @Test
    public void saveUserNullTest() {
        assertFalse("saveUserNullTest: should fail to add a null value", userService.saveUser(null));
    }

    /**
     * This unit test checks to see that a new User with no values cannot be saved into the database
     */
    @Test
    public void saveUserNullUserTest() {
        assertFalse("saveUserNullUserTest: should fail to add a null user", userService.saveUser(new User()));
    }

    /**
     * This unit test checks to see that a new User with no username cannot be saved into the database
     */
    @Test
    public void saveUserNullUsernameTest() {
        User noUsername = new User(null, "new"+ password1, "new"+fname1, "new"+lname1, age1);
        assertFalse("saveUserNullUsernameTest: should fail to add a user with no username", userService.saveUser(noUsername));
    }

    /**
     * This unit test checks to see that a new User with no password cannot be saved into the database
     */
    @Test
    public void saveUserNullPasswordTest() {
        User noPassword = new User("newUsername"+username1, null, "new"+fname1, "new"+lname1, age1);
        assertFalse("saveUserNullPasswordTest: should fail to add a user with no password", userService.saveUser(noPassword));
    }

    /**
     * This unit test checks to see that a new User with no first name cannot be saved into the database
     */
    @Test
    public void saveUserNullFnameTest() {
        User noFname = new User("newUsername"+username1, "new"+password1, null, "new"+lname1, age1);
        assertFalse("saveUserNullFnameTest: should fail to add a user with no first name", userService.saveUser(noFname));
    }

    /**
     * This unit test checks to see that a new User with no last name cannot be saved into the database
     */
    @Test
    public void saveUserNullLnameTest() {
        User noLname = new User("newUsername"+username1, "new"+password1, "new"+fname1, null, age1);
        assertFalse("saveUserNullLnameTest: should fail to add a user with no last name", userService.saveUser(noLname));
    }

    /**
     * This unit test checks to see that a new User with no age cannot be added to the database
     */
    @Test
    public void saveUserNullAgeTest() {
        User noAge = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, null);
        assertFalse("saveUserNullAgeTest: should fail to add a user with no last name", userService.saveUser(noAge));
    }

    /**
     * This unit test checks to see if a new User with a negative age cannot be added to the database
     */
    @Test
    public void saveUserNegativeAgeTest() {
        User negativeAge = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, Integer.MIN_VALUE);
        assertFalse("saveUserNegativeAgeTest: should fail to add a user with negative age", userService.saveUser(negativeAge));
    }

    /**
     * This unit test checks to see if a new user with an age of 1 can be added to the database since 1-125 is the cutoff
     */
    @Test
    public void saveUser1AgeTest() {
        User age1 = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, 1);
        assertTrue("saveUser1AgeTest: should succeed to add a user with an age of 1", userService.saveUser(age1));
    }

    /**
     * This unit test checks to see if a new user with an age of 125 can be added to the database since 1-125 is the cutoff
     */
    @Test
    public void saveUser125AgeTest() {
        User age125 = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, 125);
        assertTrue("saveUser125AgeTest: should succeed to add a user with an age of 125", userService.saveUser(age125));
    }

    /**
     * This unit test checks to see if a new user with an age of 0 cannot be added to the database
     */
    @Test
    public void saveUser0AgeTest() {
        User age0 = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, 0);
        assertFalse("saveUser0AgeTest: should fail to add a user with an age of 0", userService.saveUser(age0));
    }

    /**
     * This unit test checks to see if a new user with an age of Integer.MAX_VALUE cannot be added to the database
     */
    @Test
    public void saveUserMaxAgeTest() {
        User ageMax = new User("newUsername"+username1, "new"+password1, "new"+fname1, "new"+lname1, Integer.MAX_VALUE);
        assertFalse("saveUserMaxAgeTest: should fail to add a user with an age of Integer.MAX_VALUE", userService.saveUser(ageMax));
    }

    /**
     * This unit test checks to see if a user can update their account with a new password
     */
    @Test
    public void updatePasswordValidPasswordTest() {
        String newPassword = "Password1234";
        assertTrue("updatePasswordValidPasswordTest: should succeed to change the user password", userService.updatePassword(fakeUser1, newPassword, password1));
    }

    /**
     * This unit test checks to see that a password with less than 8 characters can not substitute an old password
     */
    @Test
    public void updatePasswordShortPasswordTest() {
        String shortPassword = "1234";
        assertFalse("updatePasswordShortPasswordTest: should fail to update the user password", userService.updatePassword(fakeUser1, shortPassword, password1));
    }

    /**
     * This unit test checks to see that a password with greater than 71 characters can not substitute an old password
     * due to BCrypt's password length limit
     */
    @Test
    public void updatePasswordLongPasswordTest() {
        String longPassword = "This is intentionally very long and greater than 72 characters so that the password will not be accepted.";
        assertFalse("updatePasswordLongPasswordTest: should fail to update the user password that is greater than 71 characters", userService.updatePassword(fakeUser1, longPassword, password1));
    }

    /**
     * This unit test checks to see if a null password can substitute an old password
     */
    @Test
    public void updatePasswordNullPasswordTest() {
        assertFalse("updatePasswordNullPasswordTest: should fail to udpate the user password provided a null password", userService.updatePassword(fakeUser1, null, password1));
    }

    /**
     * This unit test checks to see if a null user can have its password updated
     */
    @Test
    public void updatePasswordNullUserTest() {
        String newPassword = "Password1234";
        assertFalse("updatePasswordNullUserTest: should fail to update a password for a null user", userService.updatePassword(null, newPassword, password1));
    }

    /**
     * This unit test checks to see if an unsaved user can have its password updated
     */
    @Test
    public void updatePasswordUnsavedUserTest() {
        User unsavedUser = new User("djmacy", "1234567890", "David", "Macy", 35);
        String newPassword = "Password1234";
        assertFalse("updatePasswordUnsavedUserTest: should fail to update a password for a user that is not in the db", userService.updatePassword(unsavedUser, newPassword, password1));
    }

    /**
     * This unit test checks to see if passwords with foreign characters can be updated and that the same user can change his password multiple times
     */
    @Test
    public void updatePasswordForeignCharactersTest() {
        String mandarinPassword = "密码1234567";
        String arabicPassword = "كلمة المرور1234";
        String icelandicPassword = "Lykilorð1234";
        assertTrue("updatePasswordMandarinPasswordTest: should succeed to update password with mandarin characters", userService.updatePassword(fakeUser1, mandarinPassword, password1));
        assertTrue("updatePasswordArabicPasswordTest: should succeed to update password with arabic characters", userService.updatePassword(fakeUser1, arabicPassword, mandarinPassword));
        assertTrue("updatePasswordIcelandicPasswordTest: should succeed to update password with icelandic characters", userService.updatePassword(fakeUser1, icelandicPassword, arabicPassword));
    }

    /**
     * This unit test checks to see if a username associated with a user that exists can be deleted from the database
     */
    @Test
    public void deleteExistingUserTest() {
        assertTrue("deleteExistingUserTest: should succeed to delete a user that is already in the database", userService.deleteUser(fakeUser1.getUsername()));
    }

    /**
     * This unit test checks to see that a username associated with a user that does not exist in the database can not
     * be deleted and returns false.
     */
    @Test
    public void deleteNonExistingUserTest() {
        assertFalse("deleteNonExistingUserTest: should fail to delete a user that does not exist", userService.deleteUser("nonExistingUser"));
    }

    /**
     * This unit test checks to see that a user with a username that has foreign characters can be successfully deleted
     */
    @Test
    public void deleteForeignUserTest() {
        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);

        userService.saveUser(mandarinUser);
        userService.saveUser(arabicUser);
        userService.saveUser(icelandicUser);

        assertTrue("deleteForeignUserTestMandarin: should succeed to delete user with Mandarin username", userService.deleteUser(mandarinName));
        assertTrue("deleteForeignUserTestArabic: should succeed to delete user with Arabic username", userService.deleteUser(arabicName));
        assertTrue("deleteForeignUserTestIcelandic: should succeed to delete user with Icelandic username", userService.deleteUser(icelandicName));

    }

    /**
     * This unit test checks to see that deleting a null user is handled correctly
     */
    @Test
    public void deleteNullUserTest() {
        assertFalse("deleteNullUserTest: should fail to delete a null user", userService.deleteUser(null));
    }

    /**
     * This unit test checks to see that the user age matches the expected age of the user in the database
     */
    @Test
    public void getUserAgeTestExistingUserTest() {
        assertEquals("getUserAgeTestExistingUserTest: should equal the fakeUsers age, 17", age1, userService.getUserAge(fakeUser1.getUsername()));
    }

    /**
     * This unit test checks to see that -1 is returned if there is no user in the database.
     */
    @Test
    public void getUserAgeTestNonUserTest() {
        User newUser = new User("new1" + username1, password1, fname1, lname1, age1);
        assertEquals("getUserAgeTestNonUserTest: should equal -1 since user is not in db", -1, userService.getUserAge(newUser.getUsername()));
    }

    /**
     * This unit test checks to see that -1 is returned for a null user
     */
    @Test
    public void getUserAgeNullUserTest() {
        assertEquals("getUserAgeNullUserTest: should equal -1 since user is null", -1, userService.getUserAge(null));
    }

    /**
     * This unit test checks to see that the correct User object is returned given a username
     */
    @Test
    public void getUserValidUsernameTest() {
        assertEquals("getUserValidUsernameTest: the username associated with fakeUser1 should return the fakeUser1 object", fakeUser1, userService.getUserByUserName(username1));
    }

    /**
     * This unit test checks to see that null is returned given a username to an invalid User object
     */
    @Test
    public void getUserInvalidUsernameTest() {
        assertEquals("getUserInvalidUsernameTest: the fake username should return null", null, userService.getUserByUserName("FaKeUserNAME"));
    }

    /**
     * This unit test checks to see that the null is returned given a null username
     */
    @Test
    public void getUserNullUsernameTest() {
        assertEquals("getUserNullUsernameTest: the null username should return null", null, userService.getUserByUserName(null));
    }

    /**
     * This unit test checks to see that the raw password associated with the hashed password of the user matches
     */
    @Test
    public void passwordMatchesTest() {
        String hashedPassword = fakeUser1.getHashedPassword();
        assertTrue("passwordMatchesTest: the hashedPassword should match the raw password provided", userService.passwordMatches(password1, hashedPassword));
    }
}
