package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private Integer age;

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

    /**
     *
     * @return
     */
    public String getFirstName() {

        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {

        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public Integer getAge() {

        return age;
    }

    /**
     *
     * @param age
     */
    public void setAge(Integer age) {

        this.age = age;
    }
}
