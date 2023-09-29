package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * This class outlines the loginForm criteria. Here we only check to make sure that username and password is not empty
 * when logging in.
 */

public class LoginForm {
    @NotNull(message = "Please insert a valid username")
    private String username;
    @NotNull(message = "Please insert valid password")
    private String password;

    /**
     * Returns the username from the loginForm.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username from the loginForm
     * @param username - Username from the loginForm
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password from the loginForm.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password from the loginForm
     * @param password - Password from the loginForm
     */
    public void setPassword(String password) {
        this.password = password;
    }

}