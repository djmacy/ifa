package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import edu.carroll.ifa.jpa.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class for the user options after logging in. Once logged in the user can change their age, delete their account,
 * or logout.
 */
@Controller
public class UserController {
    private final UserService userService;

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
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(){
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
        model.addAttribute("user", user);
        return "updateAccount";
    }

    /**
     * Handles the POST request for the "/updateAccount" page which updates the user's age.
     * @param updatedUser - User object associated with the user that's logged in
     * @param session - HttpSession for managing session information
     * @return loginSuccess page after updating the age
     */

    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute("user") User updatedUser, HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        User user = userService.getUserByUserName(sessionUsername);
        userService.saveUserAge(user, updatedUser.getAge());
        //service.saveUser(user, updatedUser);
        return "redirect:/loginSuccess";
    }

}
