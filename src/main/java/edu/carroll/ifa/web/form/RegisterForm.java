package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.util.LambdaSafe;

public class RegisterForm {
    @NotNull
    @Size(min = 6, message = "Username must be at least 6 characters long")
    private String username;
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotNull(message = "Please enter a valid name")
    private String firstName;
    @NotNull(message = "Please enter a valid name")
    private String lastName;
    @NotNull(message = "Please enter a valid name")
    private int age;


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

    public void setFirstName() {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName() {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge() {
        this.age = age;
    }
}
