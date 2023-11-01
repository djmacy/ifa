package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;

/**
 * Interface which has methods that are used in the UserServiceImpl class to help manage the information in the database
 */

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
     * Given a user object with updated information, update the current user and save it
     * @param user
     * @param updatedPassword
     * @return
     */
    boolean updatePassword(User user, String updatedPassword);

    /**
     * Given a User object, save the updated information associated with the user to the database without hashing the raw password.
     * @param user - User object that needs to be added to the database
     * @return false if user already exists in database, true otherwise
     */
    boolean updateUser(User user, String updatedFName, String updatedLName, Integer updatedAge);

}
