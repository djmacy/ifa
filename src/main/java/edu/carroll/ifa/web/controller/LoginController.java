package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.LoginService;
import edu.carroll.ifa.web.form.LoginForm;
import jakarta.servlet.http.HttpSession;
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
public class LoginController {
   private final LoginService loginService;

   public LoginController(LoginService loginService) {
       this.loginService = loginService;
   }
    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "login";
        }
        if (!loginService.validateUser(loginForm)) {
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            return "login";
        }
        attrs.addAttribute("username", loginForm.getUsername());
        session.setAttribute("username", loginForm.getUsername());

        return "redirect:/loginSuccess";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model, @ModelAttribute LoginForm loginForm) {
       String sessionUsername = (String) session.getAttribute("username");

       if (sessionUsername != null && sessionUsername.equals(loginForm.getUsername())) {
           model.addAttribute("username", sessionUsername);
           return "loginSuccess";
       } else {
           return "redirect:/login";
       }
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}