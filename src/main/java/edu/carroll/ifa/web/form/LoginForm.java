package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotNull;

public class LoginForm {
    @NotNull
    private String username;
    @NotNull
    private String password;

    /**
     * Returns the username from the loginForm.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the loginForm
     * @param username - Username for the loginForm
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
     * Sets the password for the loginForm
     * @param password - Password for the loginForm
     */
    public void setPassword(String password) {
        this.password = password;
    }
}