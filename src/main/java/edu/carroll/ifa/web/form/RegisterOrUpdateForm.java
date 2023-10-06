package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.*;

/**
 * This class outlines the criteria for registering with the RegisterForm. It also allows to interact with the data that
 * has been given to us by the user.
 */
public class RegisterOrUpdateForm {
    @NotNull
    @Size(min = 6, message = "Username must be at least 6 characters long")
    private String username;
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotNull(message = "Please enter a valid name")
    @NotEmpty(message = "First Name is required")
    private String firstName;
    @NotNull(message = "Please enter a valid name")
    @NotEmpty(message = "Last Name is required")
    private String lastName;
    //@NotNull(message = "Please enter a valid age")
    @NotNull(message = "Age is required")
    @Min(value = 1L, message = "The value must be positive")
    @Max(value = 126L, message = "The value must be less than 127")
    private Integer age;

    /**
     * Returns the username from the registerForm.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username from the registerForm
     * @param username - Username from the registerForm
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password from the registerForm.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password from the registerForm
     * @param password - Password from the registerForm
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the first name from the registerForm.
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name from the registerForm
     * @param firstName - First name from the registerForm
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name from the registerForm.
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name from the registerForm
     * @param lastName - Last name from the registerForm
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the age from the registerForm.
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the age from the registerForm
     * @param age - Age from the registerForm
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
