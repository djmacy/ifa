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

    public User() {
    }

    /**
     *
     * @param username
     * @param hashedPassword
     * @param firstName
     * @param lastName
     * @param age
     */
    public User(String username, String hashedPassword, String firstName, String lastName, Integer age) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    /**
     *
     * @return
     */
    public Integer getId() {

        return id;
    }

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

    /**
     *
     * @return
     */
    public String getHashedPassword() {

        return hashedPassword;
    }

    /**
     *
     * @param hashedPassword
     */
    public void setHashedPassword(String hashedPassword) {

        this.hashedPassword = hashedPassword;
    }

    /**
     *
     * @param o
     * @return
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
     *
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(username, hashedPassword);
    }

}
