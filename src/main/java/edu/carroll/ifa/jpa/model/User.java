package edu.carroll.ifa.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "fname", nullable = false)
    private String firstName;
    @Column(name = "lname", nullable = false)
    private String lastName;
    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "password", nullable = false)
    private String hashedPassword;

    /**
     * No-argument constructor for Spring Dat JPA.
     */
    public User() {
    }

    /**
     * Constructor for the user.
     * @param username associated username of the user
     * @param hashedPassword associated hashed password of the user
     * @param firstName associated first name of the user
     * @param lastName associated last name of the user
     * @param age associated age of the user
     */
    public User(String username, String hashedPassword, String firstName, String lastName, Integer age) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    /**
     * Returns the user's id.
     * @return user id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the user's username.
     * @return user username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     * @param username associated username for the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the user's first name.
     * @return user first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * @param firstName associated first name for the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's last name.
     * @return user last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * @param lastName associated last name for the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the user's age.
     * @return user age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the user's age.
     * @param age associated age for the user
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Returns the user's hashed password.
     * @return associated hashed password for the user
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the user's hashed password
     * @param hashedPassword associated hashed password for the user
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Compare two User objects for equality.
     * @param o the object that gets compared with the user
     * @return true if the objects are equal based on username and hashed password. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User user = (User)o;
        return username.equals(user.username) && hashedPassword.equals(user.hashedPassword);
    }

    /**
     * Returns a hash code value for the User object.
     * @return a hash code value for the User
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword);
    }

}
