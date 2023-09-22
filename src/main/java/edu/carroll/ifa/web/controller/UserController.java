package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import edu.carroll.ifa.jpa.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    @Autowired
    private UserService service;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/deleteAccount")
    public String deleteAccount(){
        logger.info("visited delete account page ");
        return "deleteAccount";
    }

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

    @GetMapping("/updateAccount")
    public String updateAccount(Model model, HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        User user = service.getUserByUserName(sessionUsername);
        model.addAttribute("user", user);
        logger.info("visited update account page ");
        return "updateAccount";
    }

    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute("user") User updatedUser, HttpSession session){
        String sessionUsername = (String) session.getAttribute("username");
        User user = service.getUserByUserName(sessionUsername);
        service.saveUser(user, updatedUser);
        logger.info(sessionUsername + " successfully updated their account");
        return "redirect:/loginSuccess";
    }

}
