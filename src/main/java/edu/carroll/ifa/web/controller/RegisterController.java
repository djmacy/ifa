package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.service.UserService;
import edu.carroll.ifa.web.form.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private final UserService userService;

    /**
     * Constructs a LoginController instance with the UserService dependency.
     * @param userService - UserService implementation used in the LoginController
     */
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the GET request for the /register page. It also initializes the register form model and displays the page.
     * @param model - Model object for storing attributes associated with logging in
     * @return register page
     */
    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    /**
     * Handles the POST request for the /register page. It also processes user registration.
     * @param registerForm - RegisterForm contains the information submitted by the user
     * @param result - BindingResult validates form information
     * @param model - Model object for storing attributes
     * @return register page if user gives invalid or incomplete information, otherwise login page
     */
    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute RegisterForm registerForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(registerForm.getUsername());
        newUser.setHashedPassword(registerForm.getPassword());
        newUser.setFirstName(registerForm.getFirstName());
        newUser.setLastName(registerForm.getLastName());
        newUser.setAge(registerForm.getAge());

        boolean saved = userService.saveUser(newUser);

        if (!saved) {
            result.addError(new ObjectError("globalError", "Username already exists"));
            return "register";
        }
        return "redirect:/login";
    }
}
