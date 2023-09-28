package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.web.form.LoginForm;


public interface UserService {
    /**
     * Given a username and a raw password, determine if the information provided is valid, and the user exists in the system.
     * @param username - Username provided by the user logging in
     * @param password - Raw password provided by the user logging in
     * @return true if data exists and matches what's on record, false otherwise
     * - Nate
     */
    boolean validateUser(String username, String password);

    /**
     * Given a User object, save the information associated with the user to the database and hash the raw password.
     * @param user - User object that needs to be added to the database
     * @return false if user already exists in database, true otherwise
     */
    boolean saveUser(User user);

    /**
     * Given a username, delete the User associated with the username provided.
     * @param username - Username provided by the user after they have already logged in
     * @return false if the user does not exist in the database, true otherwise
     */
    boolean deleteUser(String username);

    /**
     * Given a username, it returns the user's age.
     * @param username - Username associated with the User
     * @return the age of the user if the list is not empty, -1 otherwise
     */
    int getUserAge(String username);

    /**
     * Given a username object, it returns the User object.
     * @param username - Username associated with the User
     * @return the User object if the user is in the database, null otherwise
     */
    User getUserByUserName(String username);

    /**
     * Given the User object and the desired age change, it will update the user's age.
     * @param user - User object associated with the user that is updating its age
     * @param age - Age that the user would like to update to
     * @return true if the user updates its age
     */
    boolean saveUserAge(User user, Integer age);

    //boolean saveUser(User user, User updatedUser);
}
