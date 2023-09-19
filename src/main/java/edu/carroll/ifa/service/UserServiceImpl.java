package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import edu.carroll.ifa.web.form.LoginForm;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username - Username of the person attempting to login
     *                      * @param password - Raw password provided by the user logging in
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        // Always do the lookup in a case-insensitive manner (lower-casing the data). -Nate
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly. -Nate
        if (users.size() != 1)
            return false;
        User u = users.get(0);

        if (!passwordEncoder.matches(password, u.getHashedPassword()))
            return false;

        // User exists, and the provided password matches the hashed password in the database. -Nate
        return true;
    }

    @Override
    public boolean saveUser(User user) {
        List<User> existingUser = userRepo.findByUsernameIgnoreCase(user.getUsername());
        //if the username list is empty then the username exists
        if (!existingUser.isEmpty()) {
             return false;
         }

        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        userRepo.save(user);
        return true;
    }

//    @Override
//    public boolean saveUser(User user, User updatedUser){
//        user.setFirstName(updatedUser.getFirstName());
//        user.setLastName(updatedUser.getLastName());
//        user.setHashedPassword(updatedUser.getHashedPassword());
//        user.setAge(updatedUser.getAge());
//        user.setUsername(updatedUser.getUsername());
//        userRepo.save(user, updatedUser);
//        return true;
//    }

    @Override
    public boolean saveUserAge(User user, Integer age){
        user.setAge(age);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean deleteUser(String username){
        List<User> userList = userRepo.findByUsernameIgnoreCase(username);
        if(userList.size() != 1){
            return false;
        }
        User user = userList.get(0);
        userRepo.delete(user);
        return true;
    }

    @Override
    public int getUserAge(String username) {
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (!users.isEmpty()) {
            //There should only be one so get first index
            return users.get(0).getAge();
        }
        //change this later to handle not finding the username later
        return -1;
    }

    @Override
    public User getUserByUserName(String username){
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if(users.size() != 1){
            return null;
        }
        User user = users.get(0);
        return user;
    }

}
