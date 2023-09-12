package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import edu.carroll.ifa.web.form.LoginForm;
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
     * @param loginForm - Data containing user login information, such as username and password.
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(LoginForm loginForm) {
        // Always do the lookup in a case-insensitive manner (lower-casing the data). -Nate
        List<User> users = userRepo.findByUsernameIgnoreCase(loginForm.getUsername());

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly. -Nate
        if (users.size() != 1)
            return false;
        User u = users.get(0);

        if (!passwordEncoder.matches(loginForm.getPassword(), u.getHashedPassword()))
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
        return false;
    }
}
