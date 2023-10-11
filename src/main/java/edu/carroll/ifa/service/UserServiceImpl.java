package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class will allows us to interact with the database by making changes to the users information in the database
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepo;


    /**
     * Constructs a new UserServiceImpl instance with the UserRepository
     * @param userRepo - UserRepository that is used in UserServiceImpl
     */

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username - Username of the person attempting to login
     * @param password - Raw password provided by the user logging in
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        // if there is no password provided, return false
        if (password == null) {
            return false;
        }
        // Always do the lookup in a case-insensitive manner (lower-casing the data). -Nate
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly. -Nate
        if (users.size() != 1)
            return false;
        User u = users.get(0);
        // returns false if the provided plain text password, after its encrypted, doesnt match the encrypted password in the database
        if (!passwordEncoder.matches(password, u.getHashedPassword()))
            return false;

        // User exists, and the provided password matches the hashed password in the database. -Nate
        logger.info("There is a user in the database and password matches hashed password");
        return true;
    }

    /**
     * Given a User object, it saves the information associated with the user to the database and hash the raw password.
     * @param user - User object that needs to be added to the database
     * @return false if user already exists in database, true otherwise
     */
    @Override

    public boolean saveUser(User user) {
        //password is still not hashed until we encode it
        if (user == null || user.getHashedPassword() == null || user.getUsername() == null ||
            user.getFirstName() == null || user.getLastName() == null || user.getAge() == null ||
            user.getAge() <= 0 || user.getAge() >= 126) {
            return false;
        }

        // returns false if the user already exists in the database
        List<User> existingUser = userRepo.findByUsernameIgnoreCase(user.getUsername());
        //if the username list is empty then the username does not exist
        if (!existingUser.isEmpty()) {
             return false;
         }
        // encrypts and sets the user's password
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        // saves the user to the database
        userRepo.save(user);
        logger.info("Saved the user: " + user.getUsername());
        return true;

    }

    /**

     */
    @Override
    public boolean saveUser(User user, User updatedUser){
        // sets the user's information
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setHashedPassword(passwordEncoder.encode(updatedUser.getHashedPassword()));
        user.setAge(updatedUser.getAge());
        user.setUsername(updatedUser.getUsername());
        // saves the user with the updated information to the database
        userRepo.save(user);
        logger.info("Saved the new user");
        return true;
    }

    /**
     * Given a username, delete the User associated with the username provided.
     * @param username - Username provided by the user after they have already logged in
     * @return false if the user does not exist in the database, true otherwise
     */
    @Override
    public boolean deleteUser(String username){
        // checks if the user with the given username exists in the database if not then return false
        List<User> userList = userRepo.findByUsernameIgnoreCase(username);
        if (userList.size() != 1) {
            return false;
        }
        // gets the user with the given username
        User user = userList.get(0);
        // deletes the user from the database
        userRepo.delete(user);
        logger.info("Delete the user");
        return true;
    }

    /**
     * Given a username, it returns the user's age.
     * @param username - Username associated with the User
     * @return the age of the user if the list is not empty, -1 otherwise
     */
    @Override
    public int getUserAge(String username) {
        // checks if the user with the given username exists in the database. If it does then return that user's age
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (!users.isEmpty()) {
            //There should only be one so get first index
            logger.info("Get the user's age");
            return users.get(0).getAge();
        }
        //change this later to handle not finding the username later
        return -1;
    }

    /**
     * Given a username object, it returns the User object.
     * @param username - Username associated with the User
     * @return the User object if the user is in the database, null otherwise
     */
    @Override
    public User getUserByUserName(String username){
        // checks if the user with the given username exists in the database. If not then return null
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if(users.size() != 1){
            return null;
        }
        // returns the user with the given username
        User user = users.get(0);
        logger.info("Get the user by username");
        return user;
    }
}
