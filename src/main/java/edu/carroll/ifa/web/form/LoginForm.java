package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotNull;

public class LoginForm {
    @NotNull
    private String username;
    @NotNull
    private String password;

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}