package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    @Autowired
    private UserService service;
    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        logger.info("Visited the register page");
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute RegisterForm registerForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("There were " + result.getErrorCount() + " errors");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(registerForm.getUsername());
        newUser.setHashedPassword(registerForm.getPassword());
        newUser.setFirstName(registerForm.getFirstName());
        newUser.setLastName(registerForm.getLastName());
        newUser.setAge(registerForm.getAge());

        boolean saved = service.saveUser(newUser);

        if (!saved) {
            result.addError(new ObjectError("globalError", "Username already exists"));
            logger.warn("The username " + newUser.getUsername() + " already exists");
            return "register";
        }
        logger.info("The username " + newUser.getUsername() + " completed registration");
        return "redirect:/login";
    }
}
