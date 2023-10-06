package edu.carroll.ifa.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * This is the User entity class that helps persist data related to a user into a MySQL database.
 */
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
     * @param username - Username associated with the user
     * @param hashedPassword - Hashed password associated with the user
     * @param firstName - First name associated with the user
     * @param lastName - Last name associated with the user
     * @param age - Age associated with the user
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
     * @param username - Username associated with the user
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
     * @param firstName - First name associated with the user
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
     * @param lastName - Last name associated with the user
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
     * @param age - Age associated with the user
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
     * @param hashedPassword - Hashed password associated with the user
     */
    public void setHashedPassword(String hashedPassword) {

        this.hashedPassword = hashedPassword;
    }

    /**
     * Compare two User objects for equality.
     * @param o - Object that gets compared with the user
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
     * Returns a hash code value for the User object given a username and hashed password.
     * @return a hash code value for the User
     */
    @Override
    public int hashCode() {

        return Objects.hash(username, hashedPassword);
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    /**
     * Nate's toString method for User object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Login @ ").append(super.toString()).append("[").append(EOL);
        builder.append(TAB).append("username=").append(username).append(EOL);
        builder.append(TAB).append("hashedPassword=").append("****").append(EOL);
        builder.append("]").append(EOL);
        return builder.toString();
    }
}
