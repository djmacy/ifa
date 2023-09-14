package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.util.LambdaSafe;

public class RegisterForm {
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
