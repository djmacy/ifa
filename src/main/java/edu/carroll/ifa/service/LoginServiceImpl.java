package edu.carroll.ifa.service;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.LoginRepository;
import edu.carroll.ifa.web.form.LoginForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepo;

    public LoginServiceImpl(LoginRepository loginRepo) {
        this.loginRepo = loginRepo;
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
        List<User> users = loginRepo.findByUsernameIgnoreCase(loginForm.getUsername());

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly. -Nate
        if (users.size() != 1)
            return false;
        User u = users.get(0);
        // XXX - Using Java's hashCode is wrong on SO many levels, but is good enough for demonstration purposes.
        // NEVER EVER do this in production code! -Nate
        final String userProvidedHash = Integer.toString(loginForm.getPassword().hashCode());
        if (!u.getHashedPassword().equals(userProvidedHash))
            return false;

        // User exists, and the provided password matches the hashed password in the database. -Nate
        return true;
    }
}
