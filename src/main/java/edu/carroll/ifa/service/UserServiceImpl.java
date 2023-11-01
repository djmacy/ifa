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
     * Given a username and a password, it determines if the information provided is valid, and the user exists in the system.
     *
     * @param username - Username of the person attempting to login
     * @param rawPassword - Raw password provided by the user logging in
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String rawPassword) {
        logger.debug("validateUser: user '{}' attempted login", username);
        if (rawPassword == null) {
            logger.debug("validateUser: user '{}' provided null password", username);
            return false;
        }
        if (username == null) {
            logger.debug("validateUser: User provided a null username");
            return false;
        }
        // Always do the lookup in a case-insensitive manner (lower-casing the data). -Nate
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly. -Nate
        if (users.size() != 1) {
            logger.debug("validateUser: found {} users", users.size());
            return false;
        }
        User u = users.get(0);
        // Checks to see if rawPassword matches the hashed password in the database using BCrypts matches function
        if (!passwordEncoder.matches(rawPassword, u.getHashedPassword())) {
            logger.debug("validateUser: password does not match");
            return false;
        }
        // User exists, and the provided password matches the hashed password in the database return true
        logger.info("validateUser: successful login for {}", username);
        return true;
    }

    /**
     * Given a User object, it saves the information associated with the user to the database and hashes the raw password.
     * @param user - User object that needs to be added to the database
     * @return false if user already exists in database, true otherwise
     */
    //rename to registerUser
    @Override
    public boolean saveUser(User user) {
        // Password is still not hashed until we encode it
        if (user == null || user.getHashedPassword() == null || user.getUsername() == null ||
            user.getFirstName() == null || user.getLastName() == null || user.getAge() == null ||
            user.getAge() <= 0 || user.getAge() >= 126) {
            logger.debug("saveUser: user gave bad info");
            return false;
        }

        logger.debug("saveUser: user '{}' attempted to save their information", user.getUsername());
        List<User> existingUser = userRepo.findByUsernameIgnoreCase(user.getUsername());
        //if the username list is empty then the username does not exist
        if (!existingUser.isEmpty()) {
            logger.debug("saveUser: user '{}' already exists", user.getUsername());
            return false;
        }

        // encrypts and sets the user's password
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        // saves the user to the database
        userRepo.save(user);
        logger.info("saveUser: user '{}' saved", user.getUsername());
        return true;
    }

    /**
     * Given a User object, saves the updated information associated with the user to the database without altering
     * already set password. This method is only used to update the names or age of the user.
     * @param user - User object that needs to be added to the database
     * @return false if user already exists in database, true otherwise
     */
    public boolean updateUser(User user, String updatedFName, String updatedLName, Integer updatedAge) {
        if (user == null || user.getFirstName() == null || user.getLastName() == null || user.getAge() == null ||
                user.getAge() <= 0 || user.getAge() >= 126) {
            logger.debug("saveUser: user gave bad info");
            return false;
        }
        // set the updated information for the user
        user.setFirstName(updatedFName);
        user.setLastName(updatedLName);
        user.setAge(updatedAge);
        // saves the user to the database without hashing the password again
        userRepo.save(user);
        logger.info("saveUser: user '{}' saved", user.getUsername());
        return true;
    }

    /**
     * Updating the user's information
     * Give a two User objects, the User with the old information and the User with the updated information it will
     * overwrite the old user's information in the database.
     * @param user - User that has the old information
     * @param updatedPassword - New password that will replace the old password
     * @return true when the user is saved false otherwise
     */
    @Override
    public boolean updatePassword(User user, String updatedPassword) {
        if (user == null || updatedPassword == null) {
            logger.info("updatedPassword: User gave bad information");
            return false;
        }

        //password must be 8 or more characters and less than 72 characters. BCrypt has a 72 character limit.
        if (updatedPassword.length() < 8 || updatedPassword.length() > 72) {
            logger.info("updatedPassword: user '{} provided an invalid password", user.getUsername());
            return false;
        }

        List<User> userList= userRepo.findByUsernameIgnoreCase(user.getUsername());
        if (userList.size() != 1) {
            logger.info("updatedPassword: user '{}' does not exist or is duplicated", user.getUsername());
            return false;
        }

        // sets the user's information
        user.setHashedPassword(passwordEncoder.encode(updatedPassword));
        // saves the user with the updated information to the database
        userRepo.save(user);
        // password does not match whats in the database. If this happens its bad
        if (!passwordEncoder.matches(updatedPassword, user.getHashedPassword())) {
            return false;
        }

        logger.info("Saved the new user");
        return true;
    }

    /**
     * Given a username, delete the User associated with the username provided.
     * @param username - Username provided by the user after they have already logged in
     * @return false if the user does not exist in the database, true otherwise
     */
    @Override
    public boolean deleteUser(String username) {
        List<User> userList = userRepo.findByUsernameIgnoreCase(username);
        // checks if the user with the given username exists in the database if not then return false
        if (userList.size() != 1) {
            logger.info("saveUser: user '{}' is duplicate or does not exist", username);
            return false;
        }

        logger.debug("deleteUser: user '{}'is attempting to delete their information", username);
        // gets the user with the given username
        User user = userList.get(0);
        // deletes the user from the database
        userRepo.delete(user);
        logger.info("saveUser: user '{}' deleted their information", username);
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
            logger.info("getUserAge: user '{}' successfully retrieved age", username);
            return users.get(0).getAge();
        }

        //change this later to handle not finding the username
        logger.debug("getUserAge: user '{}' has no age", username);
        return -1;
    }

    /**
     * Given a username object, it returns the User object.
     * @param username - Username associated with the User
     * @return the User object if the user is in the database, null otherwise
     */
    @Override
    public User getUserByUserName(String username){
        if (username == null) {
            return null;
        }
        // checks if the user with the given username exists in the database. If not then return null
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if(users.size() != 1){
            logger.info("getUserByUsername: user '{}' is duplicated", username);
            return null;
        }
        // returns the user with the given username
        User user = users.get(0);
        logger.info("getUserByUsername: user '{}' has been retrieved", username);
        return user;
    }
}
