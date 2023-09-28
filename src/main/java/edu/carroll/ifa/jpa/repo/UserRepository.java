package edu.carroll.ifa.jpa.repo;

import java.util.List;

import edu.carroll.ifa.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // JPA throws an exception if we attempt to return a single object that doesn't exist (which is way more expensive),
    // so return a list even though we only expect either an empty list of a single element. -Nate

    /**
     * Given a username, it returns the User in a list
     * @param username - Username associated with the User object
     * @return one User in a list if found, otherwise an empty list.
     */
    List<User> findByUsernameIgnoreCase(String username);

}
