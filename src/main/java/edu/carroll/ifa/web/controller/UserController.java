package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterOrUpdateForm;
import edu.carroll.ifa.web.form.UpdatePasswordForm;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Validation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import edu.carroll.ifa.jpa.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller class for the user options after logging in. Once logged in the user can change their age, delete their account,
 * or logout.
 */
@Controller
public class UserController {
     private SmartValidator validator;

     private static final Logger logger = LoggerFactory.getLogger(UserController.class);

     private final UserService userService;

    /**
     * Constructs a UserController instance with the UserService dependency.
     * @param userService - UserService implementation used in the UserController
     */
    public UserController(UserService userService, SmartValidator validator) {
        this.userService = userService;

        try {
            this.validator = (SmartValidator) Validation.buildDefaultValidatorFactory().getValidator();
        } catch (Exception ex) {

        }

        this.validator = validator;

    }

    /**
     * Handles the GET request for the /deleteAccount page and displays the page
     * @return deleteAccount page
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(HttpSession session){
        String username = (String) session.getAttribute("username");
        logger.info("User '{}' visited delete account page", username);
        return "deleteAccount";
    }

    /**
     * Handles the GET request for the /deleteAccountConfirmed which is used to delete the user account.
     * @param session - HttpSession managing user session information
     * @return home page if successfully deleted, otherwise /loginSuccess page
     */
    @GetMapping("/deleteAccountConfirmed")
    public String deleteAccountConfirmed(HttpSession session){
        // get username from the session
        String sessionUsername = (String) session.getAttribute("username");
        // deletes the user given the username
        boolean deleteStatus = userService.deleteUser(sessionUsername);
        // checking if the deletion was successful or not
        if(deleteStatus){
            logger.info("User '{}' successfully deleted their account", sessionUsername);
            // removes username from the session
            session.removeAttribute("username");
            return "redirect:/";
        }else{
            return "redirect:/loginSuccess";
        }
    }

    /**
     * Handles the GET request for the /updateAccount page, by displaying the updateAccount page
     * @param model - Model for storing attributes
     * @param session - HttpSession for managing session information
     * @return updateAccount page relevant to the user that's logged in
     */
    @GetMapping("/updateAccount")
    public String updateAccount(Model model, HttpSession session){

        // get the username from session
        String sessionUsername = (String) session.getAttribute("username");

        // If you're not logged in, redirect to login
        if(sessionUsername == null) {
            return "redirect:/login";
        }

        // gets the user given the username
        User user = userService.getUserByUserName(sessionUsername);
        // pre populates the form with the user's information
        RegisterOrUpdateForm registerOrUpdateForm = new RegisterOrUpdateForm();
        registerOrUpdateForm.setUsername(user.getUsername());
        registerOrUpdateForm.setPassword("PASSWORD");
        registerOrUpdateForm.setFirstName(user.getFirstName());
        registerOrUpdateForm.setLastName(user.getLastName());
        registerOrUpdateForm.setAge(user.getAge());
        // adds the registration form to the model
        model.addAttribute("registerOrUpdateForm", registerOrUpdateForm);
        logger.info("User '{}' visited update account page", sessionUsername);
        return "updateAccount";
    }

    @GetMapping("/updatePassword")
    public String updatePassword(Model model, HttpSession session){
        // get the username from session
        String sessionUsername = (String) session.getAttribute("username");
        // gets the user given the username
        User user = userService.getUserByUserName(sessionUsername);

        UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
        updatePasswordForm.setUsername(user.getUsername());
        updatePasswordForm.setCurrentPassword(user.getHashedPassword());
        updatePasswordForm.setNewPassword(user.getHashedPassword());
        updatePasswordForm.setConfirmNewPassword(user.getHashedPassword());


        model.addAttribute("updatePasswordForm", updatePasswordForm);
        logger.info("User '{}' visited update password page", sessionUsername);
        return "updatePassword";
    }

    /**
     * Handles the POST request for the "/updateAccount" page which updates the user's age.
     * @param updatedUser - User object associated with the user that's logged in
     * @param session - HttpSession for managing session information
     * @return loginSuccess page after updating the age
     */
    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute RegisterOrUpdateForm updatedUser,
                                HttpSession session,
                                BindingResult result) {

        // checking if updated user is meeting all required validations
        validator.validate(updatedUser, result);
        // get the username from the session
        String sessionUsername = (String) session.getAttribute("username");

        // If you're not logged in, redirect to login
        if(sessionUsername == null) {
            return "redirect:/login";
        }

        // get the user
        User preExistingUserCheckUser = userService.getUserByUserName(updatedUser.getUsername());

        //Check if the user is trying to set their username to the username of a different user who's already in the database
        if (preExistingUserCheckUser != null && !sessionUsername.equals(preExistingUserCheckUser.getUsername())) {
            String userNameAlreadyTakenErrorMessage = String.format("The username you provided (%s) is already in use, please try another", updatedUser.getUsername());
            result.rejectValue("username", "not.mapped.error.message", userNameAlreadyTakenErrorMessage);
        }
        // checks for errors and adds to result
        if (result.hasErrors()) {
            logger.debug("There were {} errors", result.getErrorCount());
            return "updateAccount";
        }

        // get the user given the username
        User user = userService.getUserByUserName(sessionUsername);

        // If the user you're logged in as doesn't exist in the database, redirect
        if(user == null) {
            return "redirect:/login";
        }
        // set the updated information for the user
        user.setUsername(updatedUser.getUsername());

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setAge(updatedUser.getAge());
        // save the user
        userService.saveUpdated(user);

        logger.info("The username {} completed registration", user.getUsername());

        logger.info("The user '{}' updated their information", user.getUsername());
        // get the username in the session
        session.setAttribute("username", user.getUsername());

        return "redirect:/loginSuccess";
    }


    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute UpdatePasswordForm updatedPassword,
                                HttpSession session,
                                BindingResult result) {
        // checking if updated user is meeting all required validations
        validator.validate(updatedPassword, result);
        // get the username from the session
        String sessionUsername = (String) session.getAttribute("username");


        // checks for errors and adds to result
        if (result.hasErrors()) {
            logger.debug("There were {} errors", result.getErrorCount());
            return "updatePassword";
        }

        // If you're not logged in, redirect to login
        if(sessionUsername == null) {
            return "redirect:/login";
        }

        // get the user given the username
        User user = userService.getUserByUserName(sessionUsername);

        // If the user you're logged in as doesn't exist in the database, redirect
        if(user == null) {
            return "redirect:/login";
        }

        if (!updatedPassword.getNewPassword().equals(updatedPassword.getConfirmNewPassword())) {
            result.addError(new ObjectError("newPassword", "Passwords don't match"));
            return "updatePassword";
        }

        // set the updated information for the user
        user.setHashedPassword(updatedPassword.getNewPassword());
        // save the user
        userService.saveUser(user, user);

        logger.info("The user '{}' updated their password", user.getUsername());


        return "redirect:/loginSuccess";
    }

}
