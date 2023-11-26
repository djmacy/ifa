package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import jakarta.transaction.Transactional;
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
    private static final String icelandicName = "Davíð123";
    private static final String arabicName = "123ديفيد";
    private static final String mandarinName = "大衛1234";
    private static final String password1 = "123456789";
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
     * This unit test checks to see if a user can successfully be validated in the database provided its raw password and
     * username.
     */
    @Test
    public void validateUserSuccessTest() {
        assertFalse("validateUserNotInDatabaseTest: should fail to validate a user that is not in the database", userService.validateUser(fakeUser1.getUsername(), password1));
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    @Test
    public void validateUserNotInDatabaseTest() {
        assertFalse("validateUserNotInDatabaseTest: should fail to validate a user that is not in the database", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided an incorrect
     * username and a valid password.
     */
    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, but a valid password", userService.validateUser(fakeUser1.getUsername() + "not", password1));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided a valid
     * username and an incorrect password.
     */
    @Test
    public void validateUserValidUserInvalidPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserValidUserInvalidPasswordTest: should fail using a valid user, and an invalid password", userService.validateUser(fakeUser1.getUsername(), password1 + "not"));
    }

    /**
     * This unit test checks to see that a user cannot be successfully validated in the database provided an incorrect
     * password and an incorrect username
     */
    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(fakeUser1.getUsername() + "not", password1 + "extra"));
    }

    /**
     * This unit test checks to see that a null user cannot successfully be validated in the database provided a password that exists in the db.
     */
    @Test
    public void validateUserNullUserInvalidPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserNullUserInvalidPasswordTest: should fail using a null user, and a valid password", userService.validateUser(null, password1));
    }

    /**
     * This unit test checks to see that a valid user cannot successfully be validated in the database with a valid username and a null password.
     */
    @Test
    public void validateUserValidUserNullPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserValidUserNullPasswordTest: should fail using a valid username, and a null password", userService.validateUser(fakeUser1.getUsername(), null));
    }

    /**
     * This unit test checks to see that a null username and null password can not be validated
     */
    @Test
    public void validateUserNullUserNullPasswordTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserNullUserNullPasswordTest: should fail to validate a user with null parameters", userService.validateUser(null, null));
    }

    /**
     * This unit test checks to see that a user cannot log in with their valid username and someone elses valid password.
     */
    @Test
    public void validateUserValidUser1ValidPassword2Test() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertFalse("validateUserValidUser1ValidPassword2Test: should fail using a valid username from fakeUser1, and a valid password from fakeUser2", userService.validateUser(fakeUser1.getUsername(), password2));
    }

    /**
     * This unit test checks to see that multiple users can be validated
     */
    @Test
    public void validateMultipleUsersTest() {
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser1));
        assertTrue("validateUserSuccessTest: Add user to database failed", userService.registerUser(fakeUser2));

        assertTrue("validateMultipleUser1Test: should be able to validate the first user: ", userService.validateUser(username1, password1));
        assertTrue("validateMultipleUser2Test: should be able to validate the second user as well: ", userService.validateUser(username2, password2));
    }

    /**
     * This unit test checks to see that foreign users can be validated in the database
     */
    @Test
    public void validateUserForeignUserValidPassword() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        assertTrue("validateUserForeignUserValidPassword: Icelandic user should be added to database", userService.registerUser(icelandicUser));
        assertTrue("validateUserForeignUserValidPassword: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));

        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        assertTrue("validateUserForeignUserValidPassword: Arabic user should be added to database",userService.registerUser(arabicUser));
        assertTrue("validateUserForeignUserValidPassword: validateUserArabicUsernameValidPassword: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));

        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);
        assertTrue("validateUserForeignUserValidPassword: Mandarin user should be added to database",userService.registerUser(mandarinUser));
        assertTrue("validateUserForeignUserValidPassword: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }

    /**
     * This unit test checks to see if order of instantiation and testing will change the outcome of the method
     */
    @Test
    public void validateUserForeignUserDifferentOrderTest() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);

        assertTrue("validateUserForeignUserDifferentOrderTest: Icelandic user should be added to database",userService.registerUser(icelandicUser));
        assertTrue("validateUserForeignUserDifferentOrderTest: Arabic user should be added to database",userService.registerUser(arabicUser));
        assertTrue("validateUserForeignUserDifferentOrderTest: Mandarin user should be added to database",userService.registerUser(mandarinUser));

        assertTrue("validateUserForeignUserDifferentOrderTest: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));
        assertTrue("validateUserForeignUserDifferentOrderTest: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));
        assertTrue("validateUserForeignUserDifferentOrderTest: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }

    /**
     * This will make sure that empty username and empty password cannot be validated
     */
    @Test
    public void validateUserEmptyString() {
        assertFalse("validateUserEmptyString: Should fail to validate user with empty strings", userService.validateUser("",""));
    }

    /**
     * This unit test checks to see that a new user can be saved into the database if they do not already exist in the database.
     */
    @Test
    public void registerUserNewUserTest() {
        assertTrue("saveUserNewUserTest: should succeed using a new user", userService.registerUser(fakeUser1));
        //assertTrueGetUserByUsername
    }

    /**
     * This unit test checks and sees that user with bad information can not be registered
     */
    @Test
    public void registerUserBadInformation() {
        //Username is bad because it must be at least 6 characters, password is bad because it must be at least 8 characters,
        //First and Last name are bad because there must be at least 1 character
        User blankUsername = new User("", "", "", "", age1);
        assertFalse("registerUserBlankUsernameTest: should fail to register user with bad information", userService.registerUser(blankUsername));
        assertFalse("registerUserBlankUsernameTest: should fail to validate user with bad information", userService.validateUser("", "1234"));
    }

    /**
     * This unit test checks to make sure that a username with six characters can be registered  to the database
     */
    @Test
    public void registerUserSixCharacterUsername() {
        User sixCharacterUsername = new User("sixStr", password1, fname1, lname1, age1);
        assertTrue("registerUserSixCharacterUsername: failed to register user with username of six characters", userService.registerUser(sixCharacterUsername));
        assertTrue("registerUserSixCharacterUsername: failed to validate user with username of six characters", userService.validateUser(sixCharacterUsername.getUsername(), password1));
    }

    /**
     * This unit test checks to make sure that a username with five characters can not be registered  to the database
     */
    @Test
    public void registerUserFiveCharacterUsername() {
        User fiveCharacterUsername = new User("five", password1, fname1, lname1, age1);
        assertFalse("registerUserFiveCharacterUsername: failed to register user with username of six characters", userService.registerUser(fiveCharacterUsername));
        assertFalse("registerUserFiveCharacterUsername: failed to validate user with six username of six characters", userService.validateUser(fiveCharacterUsername.getUsername(), password1));
    }

    /**
     * This unit test checks to make sure user with 8 length password can register
     */
    @Test
    public void registerUserEightCharacterUsername() {
        User eightCharacterPassword = new User(username1, "12345678", fname1, lname1, age1);
        assertTrue("registerUserEightCharacterUsername: failed to register user with password of eight characters", userService.registerUser(eightCharacterPassword));
        assertTrue("registerUserEightCharacterUsername: failed to validate user with password of eight characters", userService.validateUser(eightCharacterPassword.getUsername(), "12345678"));
    }

    /**
     * This unit test checks to make sure user with 7 length password can not be registered
     */
    @Test
    public void registerUserSevenCharacterUsername() {
        User sevenCharacterPassword = new User(username1, "1234567", fname1, lname1, age1);
        assertFalse("registerUserEightCharacterUsername: failed to register user with password of eight characters", userService.registerUser(sevenCharacterPassword));
        assertFalse("registerUserEightCharacterUsername: failed to validate user with password of eight characters", userService.validateUser(sevenCharacterPassword.getUsername(), "1234567"));
    }

    /**
     * This unit test checks and sees that multiples foreign users can register
     */
    @Test
    public void registerMultipleUsersTest() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);

        assertTrue("registerMultipleUsersTest: Icelandic user should be added to database",userService.registerUser(icelandicUser));
        assertTrue("registerMultipleUsersTest: Arabic user should be added to database",userService.registerUser(arabicUser));
        assertTrue("registerMultipleUsersTest: Mandarin user should be added to database",userService.registerUser(mandarinUser));

        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(icelandicUser.getUsername()), icelandicUser);
        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(arabicUser.getUsername()), arabicUser);
        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(mandarinUser.getUsername()), mandarinUser);

        assertTrue("registerUserMultipleUsersTest: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));
        assertTrue("registerUserMultipleUsersTest: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));
        assertTrue("registerUserMultipleUsersTest: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }

    /**
     * This test checks to make sure order does not impair registering foreign users
     */
    @Test
    public void registerMultipleUsersDifferentOrderTest() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User mandarinUser = new User(mandarinName, password2, fname1, lname1, age1);

        assertTrue("registerMultipleUsersTest: Icelandic user should be added to database",userService.registerUser(icelandicUser));
        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(icelandicUser.getUsername()), icelandicUser);
        assertTrue("registerUserMultipleUsersTest: should be able to validate the icelandic user: ", userService.validateUser(icelandicName, password1));

        assertTrue("registerMultipleUsersTest: Arabic user should be added to database",userService.registerUser(arabicUser));
        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(arabicUser.getUsername()), arabicUser);
        assertTrue("registerUserMultipleUsersTest: should be able to validate the arabic user: ", userService.validateUser(arabicName, password1));

        assertTrue("registerMultipleUsersTest: Mandarin user should be added to database",userService.registerUser(mandarinUser));
        assertEquals("registerUserMultipleUsersTest: Did not retrieve correct User", userService.getUserByUserName(mandarinUser.getUsername()), mandarinUser);
        assertTrue("registerUserMultipleUsersTest: should be able to validate the arabic user: ", userService.validateUser(mandarinName, password2));
    }

    /**
    * This unit test checks to see that a user cannot be saved into the database if they already exist in the database.
    */
    @Test
    public void registerUserExistingUserTest() {
        assertTrue("registerUserExistingUserTest: fakeUser1 failed to register the user", userService.registerUser(fakeUser1));
        assertEquals("registerUserExistingUserTest: fakeUser1 could not be retrieved", userService.getUserByUserName(fakeUser1.getUsername()), fakeUser1);
        assertFalse("registerUserExistingUserTest: should fail using a user already in db", userService.registerUser(fakeUser1));
        //make sure user can still be retrieved after trying to register duplicate user
        assertEquals("registerUserExistingUserTest: fakeUser1 could not be retrieved", userService.getUserByUserName(fakeUser1.getUsername()), fakeUser1);
    }

    /**
     * This unit test checks to see that a null value cannot be saved to the database
     */
    @Test
    public void registerUserNullTest() {
        assertFalse("registerUserNullTest: should fail to add a null value", userService.registerUser(null));
    }

    /**
     * This unit test checks to see that a new User with blank fields cannot be saved into the database
     */
    @Test
    public void registerUserBlankUserTest() {
        User blankUser = new User();
        assertFalse("registerUserBlankUserTest: should fail to add a null user", userService.registerUser(blankUser));
        assertFalse("registerUserBlankUserTest: should fail to validate the blank user", userService.validateUser(blankUser.getUsername(), blankUser.getHashedPassword()));
    }

    /**
     * This unit test checks to see that a new User with no username cannot be saved into the database
     */
    @Test
    public void registerUserNullUsernameTest() {
        User noUsername = new User(null, password1, fname1, lname1, age1);
        assertFalse("registerUserNullUsernameTest: should fail to add a user with no username", userService.registerUser(noUsername));
        assertFalse("registerUserNullUsernameTest: should fail to validate user", userService.validateUser(noUsername.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a new User with no password cannot be saved into the database
     */
    @Test
    public void registerUserNullPasswordTest() {
        User noPassword = new User(username1, null, fname1, lname1, age1);
        assertFalse("registerUserNullPasswordTest: should fail to add a user with no password", userService.registerUser(noPassword));
        assertFalse("registerUserNullPasswordTest: should fail to validate user", userService.validateUser(noPassword.getUsername(), null));
    }

    /**
     * This unit test checks to see that a new User with no first name cannot be saved into the database
     */
    @Test
    public void registerUserNullFnameTest() {
        User noFname = new User(username1, password1, null, lname1, age1);
        assertFalse("registerUserNullFnameTest: should fail to add a user with no first name", userService.registerUser(noFname));
        assertFalse("registerUserNullFnameTest: should fail to validate user", userService.validateUser(noFname.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a new User with no last name cannot be saved into the database
     */
    @Test
    public void registerUserNullLnameTest() {
        User noLname = new User(username1, password1, fname1, null, age1);
        assertFalse("registerUserNullLnameTest: should fail to add a user with no last name", userService.registerUser(noLname));
        assertFalse("registerUserNullLnameTest: should fail to validate user", userService.validateUser(noLname.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a new User with no age cannot be added to the database
     */
    @Test
    public void registerUserNullAgeTest() {
        User noAge = new User(username1, password1, fname1, lname1, null);
        assertFalse("registerUserNullAgeTest: should fail to add a user with no age name", userService.registerUser(noAge));
        assertFalse("registerUserNullAgeTest: should fail to validate user", userService.validateUser(noAge.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a new User with a negative age cannot be added to the database
     */
    @Test
    public void registerUserNegativeAgeTest() {
        User negativeAge = new User(username1, password1, fname1, lname1, Integer.MIN_VALUE);
        assertFalse("registerUserNegativeAgeTest: should fail to add a user with negative age", userService.registerUser(negativeAge));
        assertFalse("registerUserNegativeAgeTest: should fail to validate user", userService.validateUser(negativeAge.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a new user with an age of 1 can be added to the database since 1-125 is the cutoff
     */
    @Test
    public void registerUser1AgeTest() {
        User age1 = new User(username1, password1, fname1, lname1, 1);
        assertTrue("registerUser1AgeTest: should succeed to add a user with an age of 1", userService.registerUser(age1));
        assertTrue("registerUser1AgeTest: failed to validate user in db", userService.validateUser(age1.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a new user with an age of 125 can be added to the database since 1-125 is the cutoff
     */
    @Test
    public void registerUserEdgeCaseAgeTest() {
        User edgeCaseAge = new User(username1, password1, fname1,lname1, userService.TOO_OLD_AGE - 1);
        assertTrue("registerUserEdgeCaseAgeTest: should succeed to add a user with an age of 122", userService.registerUser(edgeCaseAge));
        assertTrue("registerUserEdgeCaseAgeTest: failed to validate user in db", userService.validateUser(edgeCaseAge.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a new user with an age of 0 cannot be added to the database
     */
    @Test
    public void registerUser0AgeTest() {
        User age0 = new User(username1, password1, fname1, lname1, 0);

        assertFalse("registerUser0AgeTest: should fail to add a user with an age of 0", userService.registerUser(age0));
        assertFalse("registerUser0AgeTest: should fail to validate user in db", userService.validateUser(age0.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a new user with an age of Integer.MAX_VALUE cannot be added to the database
     */
    @Test
    public void registerUserMaxAgeTest() {
        User ageMax = new User(username1, password1, fname1, lname1, Integer.MAX_VALUE);
        assertFalse("registerUserMaxAgeTest: should fail to add a user with an age of Integer.MAX_VALUE", userService.registerUser(ageMax));
        assertFalse("registerUserMaxAgeTest: should fail to validate user in db", userService.validateUser(ageMax.getUsername(), password1));
    }

    /**
     * This unit test checks to see if a user can update their account with a new password
     */
    @Test
    public void updatePasswordValidPasswordTest() {
        String newPassword = "Password1234";
        assertTrue("updatePasswordValidPasswordTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertTrue("updatePasswordValidPasswordTest: should succeed to change the user password", userService.updatePassword(fakeUser1, newPassword, password1));
        assertTrue("updatePasswordValidPasswordTest: failed to validate user with new password", userService.validateUser(fakeUser1.getUsername(), newPassword));
    }

    /**
     * This unit test checks to see if a user can update their account with the same password
     */
    @Test
    public void updatePasswordSamePasswordTest() {
        assertTrue("updatePasswordValidPasswordTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertTrue("updatePasswordValidPasswordTest: should succeed to keep the same the user password", userService.updatePassword(fakeUser1, password1, password1));
        assertTrue("updatePasswordValidPasswordTest: failed to validate user with new password", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a password with less than 8 characters can not substitute an old password
     */
    @Test
    public void updatePasswordShortPasswordTest() {
        String shortPassword = "12345";
        assertTrue("updatePasswordShortPasswordTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertFalse("updatePasswordShortPasswordTest: should fail to update the user password", userService.updatePassword(fakeUser1, shortPassword, password1));
        //It better validate with the old password and not the new one or else we have a problem
        assertTrue("updatePasswordShortPasswordTest: failed to validate user with old password", userService.validateUser(fakeUser1.getUsername(), password1));
        //Check to make sure we can not validate with the shortPassword
        assertFalse("updatePasswordShortPasswordTest: succeeded to validate user with short password", userService.validateUser(fakeUser1.getUsername(), shortPassword));
    }

    /**
     * This unit test checks to see that a password with greater than 71 characters can not substitute an old password
     * due to BCrypt's password length limit
     */
    @Test
    public void updatePasswordLongPasswordTest() {
        String longPassword = "This is intentionally very long and greater than 72 characters so that the password will not be accepted.";
        assertTrue("updatePasswordLongPasswordTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertFalse("updatePasswordLongPasswordTest: should fail to update the user password that is greater than 71 characters", userService.updatePassword(fakeUser1, longPassword, password1));
        //It better validate with the old password and not the new one or else we have a problem
        assertTrue("updatePasswordLongPasswordTest: failed to validate user with old password", userService.validateUser(fakeUser1.getUsername(), password1));
        //Check to make sure we can not validate with the longPassword
        assertFalse("updatePasswordLongPasswordTest: succeeded to validate user with Long password", userService.validateUser(fakeUser1.getUsername(), longPassword));

    }

    /**
     * This unit test checks to see if a null password can substitute an old password
     */
    @Test
    public void updatePasswordNullPasswordTest() {
        assertTrue("updatePasswordNullPasswordTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertFalse("updatePasswordNullPasswordTest: should fail to update the user password provided a null password", userService.updatePassword(fakeUser1, null, password1));
        //It better validate with the old password and not the new one or else we have a problem
        assertTrue("updatePasswordNullPasswordTest: failed to validate user with old password", userService.validateUser(fakeUser1.getUsername(), password1));
        //Check to make sure we can not validate with the null password
        assertFalse("updatePasswordNullPasswordTest: succeeded to validate user with null password", userService.validateUser(fakeUser1.getUsername(), null));

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
        assertFalse("updatePasswordUnsavedUserTest: should fail to update a password for a user that is not in the db", userService.updatePassword(unsavedUser, newPassword, unsavedUser.getHashedPassword()));
    }

    /**
     * This unit test checks to see if passwords with foreign characters can be updated and that the same user can change his password multiple times
     */
    @Test
    public void updatePasswordForeignCharactersTest() {
        String mandarinPassword = "密码1234567";
        String arabicPassword = "كلمة المرور1234";
        String icelandicPassword = "Lykilorð1234";

        assertTrue("updatePasswordForeignCharactersTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertTrue("updatePasswordMandarinPasswordTest: should succeed to update password with mandarin characters", userService.updatePassword(fakeUser1, mandarinPassword, password1));
        assertTrue("updatePasswordArabicPasswordTest: should succeed to update password with arabic characters", userService.updatePassword(fakeUser1, arabicPassword, mandarinPassword));
        assertTrue("updatePasswordIcelandicPasswordTest: should succeed to update password with icelandic characters", userService.updatePassword(fakeUser1, icelandicPassword, arabicPassword));
    }

    /**
     * This unit test checks to see if a username associated with a user that exists can be deleted from the database
     */
    @Test
    public void deleteExistingUserTest() {
        assertTrue("deleteExistingUserTest: failed to add fakeUser1", userService.registerUser(fakeUser1));
        assertTrue("deleteExistingUserTest: should succeed to delete a user that is already in the database", userService.deleteUser(fakeUser1.getUsername()));
        assertFalse("deleteExistingUserTest: should fail to verify user because its been deleted", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a username associated with a user that does not exist in the database can not
     * be deleted and returns false.
     */
    @Test
    public void deleteNonExistingUserTest() {
        assertFalse("deleteNonExistingUserTest: should fail to verify user because it has not been added", userService.validateUser(fakeUser1.getUsername(), password1));
        assertFalse("deleteNonExistingUserTest: should fail to delete a user that does not exist", userService.deleteUser(fakeUser1.getUsername()));
    }

    /**
     * This unit test checks to see that a user with a username that has foreign characters can be successfully deleted
     */
    @Test
    public void deleteMultipleForeignUserTest() {
        User mandarinUser = new User(mandarinName, password1, fname1, lname1, age1);
        User arabicUser = new User(arabicName, password1, fname1, lname1, age1);
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, age1);

        userService.registerUser(mandarinUser);
        userService.registerUser(arabicUser);
        userService.registerUser(icelandicUser);

        assertTrue("deleteMultipleForeignUserTestMandarin: should succeed to delete user with Mandarin username", userService.deleteUser(mandarinName));
        assertTrue("deleteMultipleForeignUserTestArabic: should succeed to delete user with Arabic username", userService.deleteUser(arabicName));
        assertTrue("deleteMultipleForeignUserTestIcelandic: should succeed to delete user with Icelandic username", userService.deleteUser(icelandicName));

        assertFalse("deleteMultipleForeignUserTestMandarin: should fail to validate user because its been deleted", userService.validateUser(mandarinUser.getUsername(), password1));
        assertFalse("deleteMultipleForeignUserTestMandarin: should fail to validate user because its been deleted", userService.validateUser(arabicUser.getUsername(), password1));
        assertFalse("deleteMultipleForeignUserTestMandarin: should fail to validate user because its been deleted", userService.validateUser(icelandicUser.getUsername(), password1));

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
    public void getUserAgeExistingUserTest() {
        assertTrue("getUserAgeTestExistingUserTest: should succeed adding user to db", userService.registerUser(fakeUser1));
        assertEquals("getUserAgeTestExistingUserTest: should equal the fakeUsers age, 17", fakeUser1.getAge(), userService.getUserAge(fakeUser1.getUsername()));
    }

    /**
     * This unit test checks to see that multiple users with foreign characters can have their age retrieved
     */
    @Test
    public void getMultipleUserAgeForeignUserTest() {
        User mandarinUser = new User(mandarinName, password1, fname1, lname1, 21);
        User arabicUser = new User(arabicName, password1, fname1, lname1, 37);
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, 9);

        assertTrue("getMultipleUserAgeForeignUserTest: failed to add mandarinUser to db", userService.registerUser(mandarinUser));
        assertTrue("getMultipleUserAgeForeignUserTest: failed to add arabicUser to db", userService.registerUser(arabicUser));
        assertTrue("getMultipleUserAgeForeignUserTest: failed to add icelandicUser to db", userService.registerUser(icelandicUser));

        assertTrue("getMultipleUserAgeForeignUserTest:  failed to validate mandarinUser", userService.validateUser(mandarinUser.getUsername(), password1));
        assertTrue("getMultipleUserAgeForeignUserTest:  failed to validate arabicUser", userService.validateUser(arabicUser.getUsername(), password1));
        assertTrue("getMultipleUserAgeForeignUserTest:  failed to validate icelandicUser", userService.validateUser(icelandicUser.getUsername(), password1));

        assertEquals("getMultipleUserAgeForeignUserTest: retrieve incorrect mandarinUser age", mandarinUser.getAge(), userService.getUserAge(mandarinUser.getUsername()));
        assertEquals("getMultipleUserAgeForeignUserTest: retrieve incorrect mandarinUser age", arabicUser.getAge(), userService.getUserAge(arabicUser.getUsername()));
        assertEquals("getMultipleUserAgeForeignUserTest: retrieve incorrect mandarinUser age", icelandicUser.getAge(), userService.getUserAge(icelandicUser.getUsername()));
    }

    /**
     * This unit test checks to see that -1 is returned if there is no user in the database.
     */
    @Test
    public void getUserAgeTestNonUserTest() {
        User newUser = new User("new1" + username1, password1, fname1, lname1, age1);
        assertFalse("getUserAgeTestNonUserTest: should fail to validate user not in db", userService.validateUser(newUser.getUsername(), password1));
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
        assertTrue("getUserValidUsernameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("getUserValidUsernameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
        assertEquals("getUserValidUsernameTest: the username associated with fakeUser1 should return the fakeUser1 object", fakeUser1, userService.getUserByUserName(username1));
    }

    /**
     * This unit test checks to see that multiple users with foreign characters can have their age retrieved
     */
    @Test
    public void getMultipleUserForeignUsernameTest() {
        User mandarinUser = new User(mandarinName, password1, fname1, lname1, 21);
        User arabicUser = new User(arabicName, password1, fname1, lname1, 37);
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, 9);

        assertTrue("getMultipleUserForeignUsernameTest: failed to add mandarinUser to db", userService.registerUser(mandarinUser));
        assertTrue("getMultipleUserForeignUsernameTest: failed to add arabicUser to db", userService.registerUser(arabicUser));
        assertTrue("getMultipleUserForeignUsernameTest: failed to add icelandicUser to db", userService.registerUser(icelandicUser));

        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate mandarinUser", userService.validateUser(mandarinUser.getUsername(), password1));
        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate arabicUser", userService.validateUser(arabicUser.getUsername(), password1));
        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate icelandicUser", userService.validateUser(icelandicUser.getUsername(), password1));

        assertEquals("getMultipleUserForeignUsernameTest: retrieve incorrect user from mandarin username", mandarinUser, userService.getUserByUserName(mandarinUser.getUsername()));
        assertEquals("getMultipleUserForeignUsernameTest: retrieve incorrect user from arabic username", arabicUser, userService.getUserByUserName(arabicUser.getUsername()));
        assertEquals("getMultipleUserForeignUsernameTest: retrieve incorrect user from icelandic username", icelandicUser, userService.getUserByUserName(icelandicUser.getUsername()));
    }

    /**
     * This unit test checks to see that null is returned given a username to an invalid User object
     */
    @Test
    public void getUserInvalidUsernameTest() {
        User newUser = new User("new1" + username1, password1, fname1, lname1, age1);
        assertFalse("getUserInvalidUsernameTest: should fail to validate user in db", userService.validateUser(newUser.getUsername(), password1));
        assertEquals("getUserInvalidUsernameTest: the fake username should return null", null, userService.getUserByUserName(newUser.getUsername()));
    }

    /**
     * This unit test checks to see that the null is returned given a null username
     */
    @Test
    public void getUserNullUsernameTest() {
        assertEquals("getUserNullUsernameTest: the null username should return null", null, userService.getUserByUserName(null));
    }

    /**
     * This unit test checks to see that a user can update first name, last name, and age given valid information
     */
    @Test
    public void updateUserValidInfoTest() {
        assertTrue("updateUserValidInfoTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserValidInfoTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
        //establish new valid credentials
        String newFname = fakeUser1.getFirstName() + "extraCharacters";
        String newLname = fakeUser1.getLastName() + "extraCharacters";
        Integer newAge = fakeUser1.getAge() + 1;
        assertTrue("updateUserValidInfoTest: failed to update user info", userService.updateUser(fakeUser1, newFname, newLname, newAge));
        assertEquals("updateUserValidInfoTest: new fname does not match updated fname", newFname, fakeUser1.getFirstName());
        assertEquals("updateUserValidInfoTest: new lname does not match updated lname", newLname, fakeUser1.getLastName());
        assertEquals("updateUserValidInfoTest: new age does not match updated age", newAge, fakeUser1.getAge());
    }

    /**
     * This unit test checks to see that a user cannot update with a null fname, null lname, and null age
     */
    @Test
    public void updateUserNullInfoTest() {
        assertTrue("updateUserNullInfoTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserNullInfoTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        assertFalse("updateUserNullInfoTest: should fail to update user with null info", userService.updateUser(fakeUser1, null, null, null));

        //checks to make sure the user information was not altered
        assertEquals("updateUserNullInfoTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserNullInfoTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserNullInfoTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserNullInfoTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update with a null fname, same lname, and same age
     */
    @Test
    public void updateUserNullFnameTest() {
        assertTrue("updateUserNullFnameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserNullFnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        assertFalse("updateUserNullFnameTest: should fail to update user with null fname", userService.updateUser(fakeUser1, null, fakeUser1.getLastName(), fakeUser1.getAge()));

        //checks to make sure the user information was not altered
        assertEquals("updateUserNullFnameTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserNullFnameTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserNullFnameTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserNullFnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update with a same fname, null lname, and same age
     */
    @Test
    public void updateUserNullLnameTest() {
        assertTrue("updateUserNullLnameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserNullLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        assertFalse("updateUserNullLnameTest: should fail to update user with null lname", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), null, fakeUser1.getAge()));

        //checks to make sure the user information was not altered
        assertEquals("updateUserNullLnameTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserNullLnameTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserNullLnameTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserNullLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update with a same fname, same lname, and null age
     */
    @Test
    public void updateUserNullAgeTest() {
        assertTrue("updateUserNullLnameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserNullLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        assertFalse("updateUserNullLnameTest: should fail to update user with null lname", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), null));

        //checks to make sure the user information was not altered
        assertEquals("updateUserNullLnameTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserNullLnameTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserNullLnameTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserNullLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user can update first name, last name, and age with the same information as before
     */
    @Test
    public void updateUserSameInfoTest() {
        assertTrue("updateUserSameInfoTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserSameInfoTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        assertTrue("updateUserSameInfoTest: should succeed to update user with same info", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), fakeUser1.getAge()));

        //checks to make sure the user information was not altered
        assertEquals("updateUserSameInfoTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserSameInfoTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserSameInfoTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserSameInfoTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user can update first name, but keep last name and age the same
     */
    @Test
    public void updateUserDifferentFnameTest() {
        assertTrue("updateUserDifferentFnameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentFnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        String newFname = fakeUser1.getFirstName() + "newFname";

        assertTrue("updateUserDifferentFnameTest: should succeed to update user with new first name", userService.updateUser(fakeUser1, newFname, fakeUser1.getLastName(), fakeUser1.getAge()));

        //checks to make sure the user information was altered appropriately
        assertEquals("updateUserDifferentFnameTest: new fname does not match updated fname", newFname, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentFnameTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserDifferentFnameTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserDifferentFnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user can update last name, but keep first name and age the same
     */
    @Test
    public void updateUserDifferentLnameTest() {
        assertTrue("updateUserDifferentLnameTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        String newLname = fakeUser1.getLastName() + "newLname";

        assertTrue("updateUserDifferentLnameTest: should succeed to update user with new last name", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), newLname, fakeUser1.getAge()));

        //checks to make sure the user information was altered appropriately
        assertEquals("updateUserDifferentLnameTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentLnameTest: new lname does not match updated lname", newLname, fakeUser1.getLastName());
        assertEquals("updateUserDifferentLnameTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserDifferentLnameTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user can update age, but keep first and last name the same
     */
    @Test
    public void updateUserDifferentAgeTest() {
        assertTrue("updateUserDifferentAgeTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        Integer newAge = fakeUser1.getAge() + 1;

        assertTrue("updateUserDifferentAgeTest: should succeed to update user with new age info", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), newAge));

        //checks to make sure the user information was altered appropriately
        assertEquals("updateUserDifferentAgeTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentAgeTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserDifferentAgeTest: new age does not match updated age", newAge, fakeUser1.getAge());
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update information with a negative age
     */
    @Test
    public void updateUserNegativeAgeTest() {
        assertTrue("updateUserDifferentAgeTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        Integer negativeAge = Integer.MIN_VALUE;

        assertFalse("updateUserDifferentAgeTest: should fail to update user with negative age", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), negativeAge));

        //checks to make sure the user information was not altered
        assertEquals("updateUserDifferentAgeTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentAgeTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserDifferentAgeTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update information with a zero age
     */
    @Test
    public void updateUserZeroAgeTest() {
        assertTrue("updateUserDifferentAgeTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        Integer zeroAge = 0;

        assertFalse("updateUserDifferentAgeTest: should fail to update user with negative age", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), zeroAge));

        //checks to make sure the user information was not altered
        assertEquals("updateUserDifferentAgeTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentAgeTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserDifferentAgeTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user cannot update information with an age of above 126
     */
    @Test
    public void updateUserTooOldTest() {
        assertTrue("updateUserDifferentAgeTest: failed to add user to db", userService.registerUser(fakeUser1));
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));

        Integer tooOldAge = userService.TOO_OLD_AGE;

        assertFalse("updateUserDifferentAgeTest: should fail to update user with negative age", userService.updateUser(fakeUser1, fakeUser1.getFirstName(), fakeUser1.getLastName(), tooOldAge));

        //checks to make sure the user information was not altered
        assertEquals("updateUserDifferentAgeTest: old fname does not match current fname", fname1, fakeUser1.getFirstName());
        assertEquals("updateUserDifferentAgeTest: old lname does not match current lname", lname1, fakeUser1.getLastName());
        assertEquals("updateUserDifferentAgeTest: old age does not match current age", age1, fakeUser1.getAge());
        assertTrue("updateUserDifferentAgeTest: failed to validate user in db", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a user can update first name, last name, and age given valid information
     */
    @Test
    public void updateMultipleForeignUserValidInfoTest() {
        User icelandicUser = new User(icelandicName, password1, fname1, lname1, 9);
        User mandarinUser = new User(mandarinName, password1, fname1, lname1, 21);
        User arabicUser = new User(arabicName, password1, fname1, lname1, 37);

        assertTrue("getMultipleUserForeignUsernameTest: failed to add icelandicUser to db", userService.registerUser(icelandicUser));
        assertTrue("getMultipleUserForeignUsernameTest: failed to add mandarinUser to db", userService.registerUser(mandarinUser));
        assertTrue("getMultipleUserForeignUsernameTest: failed to add arabicUser to db", userService.registerUser(arabicUser));

        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate icelandicUser", userService.validateUser(icelandicUser.getUsername(), password1));
        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate mandarinUser", userService.validateUser(mandarinUser.getUsername(), password1));
        assertTrue("getMultipleUserForeignUsernameTest:  failed to validate arabicUser", userService.validateUser(arabicUser.getUsername(), password1));

        //establish new valid credentials
        String newIcelandicFname = icelandicUser.getFirstName() + "nýtt fornafn";
        String newMandarinFname = mandarinUser.getFirstName() + "新名字";
        String newArabicFname = arabicUser.getFirstName() + "الاسم الأول الجديد";

        String newIcelandicLname = icelandicUser.getLastName() + "nýtt eftirnafn";
        String newMandarinLname = mandarinUser.getLastName() + "新姓氏";
        String newArabicLname = arabicUser.getLastName() + "الاسم الأخير الجديد";

        Integer newIcelandicAge = icelandicUser.getAge() + 1;
        Integer newMandarinAge = mandarinUser.getAge() + 1;
        Integer newArabicAge = arabicUser.getAge() + 1;

        assertTrue("getMultipleUserForeignUsernameTest: failed to update user info", userService.updateUser(icelandicUser, newIcelandicFname, newIcelandicLname, newIcelandicAge));
        assertTrue("getMultipleUserForeignUsernameTest: failed to update user info", userService.updateUser(mandarinUser, newMandarinFname, newMandarinLname, newMandarinAge));
        assertTrue("getMultipleUserForeignUsernameTest: failed to update user info", userService.updateUser(arabicUser, newArabicFname, newArabicLname, newArabicAge));

        assertEquals("getMultipleUserForeignUsernameTest: new icelandic fname does not match updated fname", newIcelandicFname, icelandicUser.getFirstName());
        assertEquals("getMultipleUserForeignUsernameTest: new icelandic lname does not match updated lname", newIcelandicLname, icelandicUser.getLastName());
        assertEquals("getMultipleUserForeignUsernameTest: new age does not match updated age", newIcelandicAge, icelandicUser.getAge());

        assertEquals("getMultipleUserForeignUsernameTest: new mandarin fname does not match updated fname", newMandarinFname, mandarinUser.getFirstName());
        assertEquals("getMultipleUserForeignUsernameTest: new mandarin lname does not match updated lname", newMandarinLname, mandarinUser.getLastName());
        assertEquals("getMultipleUserForeignUsernameTest: new age does not match updated age", newMandarinAge, mandarinUser.getAge());

        assertEquals("getMultipleUserForeignUsernameTest: new arabic fname does not match updated fname", newArabicFname, arabicUser.getFirstName());
        assertEquals("getMultipleUserForeignUsernameTest: new arabic lname does not match updated lname", newArabicLname, arabicUser.getLastName());
        assertEquals("getMultipleUserForeignUsernameTest: new arabic age does not match updated age", newArabicAge, arabicUser.getAge());
    }

    /**
     * This unit test checks to see that the raw password associated with the hashed password of the user matches
     */
    @Test
    public void passwordMatchesValidHashedValidRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesValidHashedValidRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertTrue("passwordMatchesValidHashedValidRawTest: the hashedPassword should match the raw password provided", userService.passwordMatches(password1, fakeUser1.getHashedPassword()));
        assertTrue("passwordMatchesValidHashedValidRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a raw password associated with a null hashed password do not match
     */
    @Test
    public void passwordMatchesValidHashedInvalidRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesValidHashedInvalidRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesValidHashedInvalidRawTest: the hashedPassword should not match the raw password provided", userService.passwordMatches("wrong" + password1, fakeUser1.getHashedPassword()));
        assertTrue("passwordMatchesValidHashedInvalidRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that the user's raw password does not match an incorrect hashed password
     */
    @Test
    public void passwordMatchesInvalidHashedValidRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesInvalidHashedValidRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesInvalidHashedValidRawTest: the hashedPassword should not match the raw password provided", userService.passwordMatches(password1, "wrong" + fakeUser1.getHashedPassword()));
        assertTrue("passwordMatchesInvalidHashedValidRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that an invalid raw password does not match with the hashed password of the user
     */
    @Test
    public void passwordMatchesInvalidHashedInvalidRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesInvalidHashedInvalidRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesInvalidHashedInvalidRawTest: the hashedPassword should not match the raw password provided", userService.passwordMatches("wrong" + password1, "wrong" + fakeUser1.getHashedPassword()));
        assertTrue("passwordMatchesInvalidHashedInvalidRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a null password associated with a null hashed password do not match
     */
    @Test
    public void passwordMatchesNullHashedNullRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesNullHashedNullRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesNullHashedNullRawTest: the null hashedPassword should not match the null raw password provided", userService.passwordMatches(null, null));
        assertTrue("passwordMatchesNullHashedNullRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a null password associated with the hashed password do not match
     */
    @Test
    public void passwordMatchesValidHashedNullRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesValidHashedNullRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesValidHashedNullRawTest: the hashedPassword should not match the raw password provided", userService.passwordMatches(null, fakeUser1.getHashedPassword()));
        assertTrue("passwordMatchesValidHashedNullRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }

    /**
     * This unit test checks to see that a raw password associated with a null hashed password do not match
     */
    @Test
    public void passwordMatchesNullHashedValidRawTest() {
        //password does not get hashed until added to db
        assertTrue("passwordMatchesValidHashedNullRawTest: failed to add the user", userService.registerUser(fakeUser1));
        assertFalse("passwordMatchesValidHashedNullRawTest: the hashedPassword should not match the raw password provided", userService.passwordMatches(password1, null));
        assertTrue("passwordMatchesValidHashedNullRawTest: failed to validate the user", userService.validateUser(fakeUser1.getUsername(), password1));
    }
}
