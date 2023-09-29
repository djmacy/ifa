package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterOrUpdateForm;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
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
     @Autowired
     private SmartValidator validator;

     private final UserService userService;
     private Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * Constructs a UserController instance with the UserService dependency.
     * @param userService - UserService implementation used in the UserController
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the GET request for the /deleteAccount page and displays the page
     * @return deleteAccount page
>>>>>>> main
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(){
        logger.info("visited delete account page ");
        return "deleteAccount";
    }

    /**
     * Handles the GET request for the /deleteAccountConfirmed which is used to delete the user account.
     * @param session - HttpSession managing user session information
     * @return home page if successfully deleted, otherwise /loginSuccess page
     */
    @GetMapping("/deleteAccountConfirmed")
    public String deleteAccountConfirmed(HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        boolean deleteStatus = userService.deleteUser(sessionUsername);
        if(deleteStatus){
            session.removeAttribute("username");
            logger.info(sessionUsername + " successfully deleted their account ");
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
        String sessionUsername = (String) session.getAttribute("username");

        User user = userService.getUserByUserName(sessionUsername);

        RegisterOrUpdateForm registerOrUpdateForm = new RegisterOrUpdateForm();
        registerOrUpdateForm.setUsername(user.getUsername());
        registerOrUpdateForm.setPassword(user.getHashedPassword());
        registerOrUpdateForm.setFirstName(user.getFirstName());
        registerOrUpdateForm.setLastName(user.getLastName());
        registerOrUpdateForm.setAge(user.getAge());

        model.addAttribute("registerOrUpdateForm", registerOrUpdateForm);
        logger.info("visited update account page ");
        return "updateAccount";
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
        validator.validate(updatedUser, result);
        if (result.hasErrors()) {
            logger.warn("There were " + result.getErrorCount() + " errors");
            return "updateAccount";
        }

        String sessionUsername = (String) session.getAttribute("username");
        // If you're not logged in, redirect to login
        if(sessionUsername == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByUserName(sessionUsername);

        // If the user you're logged in as doesn't exist in the database, redirect
        if(user == null) {
            return "redirect:/login";
        }

        user.setUsername(updatedUser.getUsername());
        user.setHashedPassword(updatedUser.getPassword());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setAge(updatedUser.getAge());

        boolean saved = userService.saveUser(user);

        if (!saved) {
            result.addError(new ObjectError("globalError", "Username already exists"));
            logger.warn("The username " + user.getUsername() + " already exists");
            return "register";
        }
        session.setAttribute("username", user.getUsername());
        logger.info("The username " + user.getUsername() + " completed registration");


        session.setAttribute("username", updatedUser.getUsername());

        logger.info(sessionUsername + " successfully updated their account");

        return "redirect:/loginSuccess";
    }
}
