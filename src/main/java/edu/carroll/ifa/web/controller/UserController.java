package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterOrUpdateForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import edu.carroll.ifa.jpa.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private SmartValidator validator;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     *
     * @return
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(){
        logger.info("visited delete account page ");
        return "deleteAccount";
    }

    /**
     *
     * @param session
     * @return
     */
    @GetMapping("/deleteAccountConfirmed")
    public String deleteAccountConfirmed(HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        boolean deleteStatus = service.deleteUser(sessionUsername);
        if(deleteStatus){
            session.removeAttribute("username");
            logger.info(sessionUsername + " successfully deleted their account ");
            return "redirect:/";
        }else{
            return "redirect:/loginSuccess";
        }
    }

    /**
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/updateAccount")
    public String updateAccount(Model model, HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        User user = service.getUserByUserName(sessionUsername);

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
     *
     * @param updatedUser
     * @param session
     * @return
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

        User user = service.getUserByUserName(sessionUsername);

        // If the user you're logged in as doesn't exist in the database, redirect
        if(user == null) {
            return "redirect:/login";
        }

        user.setUsername(updatedUser.getUsername());
        user.setHashedPassword(updatedUser.getPassword());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setAge(updatedUser.getAge());

        boolean saved = service.saveUser(user);

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
