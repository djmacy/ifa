package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.web.form.LoginForm;


public interface UserService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * @param username - Username of the person attempting to login
     *      * @param password - Raw password provided by the user logging in
     * @return true if data exists and matches what's on record, false otherwise
     * - Nate
     */
    boolean validateUser(String username, String password);

    boolean saveUser(User user);

    boolean deleteUser(String username);

    int getUserAge(String username);

    User getUserByUserName(String username);

    boolean saveUser(User user, User updatedUser);
}
