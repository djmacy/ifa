package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterOrUpdateForm;
import edu.carroll.ifa.web.form.UpdatePasswordForm;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

     private final UserService userService;
    private final SmartValidator validator;


    /**
     * Constructs a UserController instance with the UserService dependency.
     * @param userService - UserService implementation used in the UserController
     */
    public UserController(UserService userService, SmartValidator validator) {
        this.userService = userService;
        this.validator = validator;

    }

    /**
     * Handles the GET request for the /deleteAccount page and displays the delete page
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

    /**
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/updatePassword")
    public String updatePassword(Model model, HttpSession session){
        // get the username from session
        String sessionUsername = (String) session.getAttribute("username");
        // gets the user given the username
        User user = userService.getUserByUserName(sessionUsername);

        UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
        updatePasswordForm.setUsername(user.getUsername());

        model.addAttribute("updatePasswordForm", updatePasswordForm);
        logger.info("User '{}' visited update password page", sessionUsername);
        return "updatePassword";
    }

    /**
     * Handles the POST request for the "/updateAccount" page which updates the user's age and names.
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
        if (sessionUsername == null) {
            return "redirect:/login";
        }

        // get the user given the username
        User user = userService.getUserByUserName(sessionUsername);
        // If the user you're logged in as doesn't exist in the database, redirect
        if (user == null) {
            return "redirect:/login";
        }
        
        // checks for errors and adds to result
        if (result.hasErrors()) {
            logger.debug("There were {} errors", result.getErrorCount());
            return "updateAccount";
        }

        // save the user with new information
        if (userService.updateUser(user, updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getAge())) {
            logger.info("The user '{}' updated account information", user.getUsername());
        }

        // get the username in the session
        session.setAttribute("username", user.getUsername());

        return "redirect:/loginSuccess";
    }

    /**
     *
     * @param updatedPassword
     * @param session
     * @param result
     * @return
     */
    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute UpdatePasswordForm updatedPassword,
                                HttpSession session,
                                BindingResult result) {


        // get the username from the session
        String sessionUsername = (String) session.getAttribute("username");

        // If you're not logged in, redirect to login
        if (sessionUsername == null) {
            return "redirect:/login";
        }

        // get the user given the username
        User user = userService.getUserByUserName(sessionUsername);

        // If the user you're logged in as doesn't exist in the database, redirect
        if (user == null) {
            return "redirect:/login";
        }

        // checks for errors and adds to result
        if (result.hasErrors()) {
            logger.debug("There were {} errors", result.getErrorCount());
            return "updatePassword";
        }

        // checking if updated user is meeting all required validations
        validator.validate(updatedPassword, result);

        if(!userService.passwordMatches(updatedPassword.getCurrentPassword(), user.getHashedPassword())){
            result.addError(new ObjectError("currentPassword", "Current password does not match"));
            return "updatePassword";
        }

        if (!updatedPassword.getNewPassword().equals(updatedPassword.getConfirmNewPassword())) {
            result.addError(new ObjectError("newPassword", "New passwords do not match"));
            return "updatePassword";
        }

        // save the user with new password
        userService.updatePassword(user, updatedPassword.getNewPassword(), updatedPassword.getCurrentPassword());
        logger.info("The user '{}' updated their password", user.getUsername());

        return "redirect:/loginSuccess";
    }
}
