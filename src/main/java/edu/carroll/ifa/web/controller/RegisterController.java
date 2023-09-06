package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.web.form.LoginForm;
import edu.carroll.ifa.web.form.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }
}
