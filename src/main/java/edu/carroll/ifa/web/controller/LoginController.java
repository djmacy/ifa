package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
   private final UserService userService;
   private Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     *
     * @param userService
     */
   public LoginController(UserService userService) {
       this.userService = userService;
   }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        logger.info("Visited the login page");
        return "login";
    }

    /**
     *
     * @param loginForm
     * @param result
     * @param session
     * @param attrs
     * @return
     */
    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            logger.warn("There were " + result.getErrorCount() + " errors");
            return "login";
        }
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            logger.warn("login failed username or password do not match known users");
            return "login";
        }
        attrs.addAttribute("username", loginForm.getUsername());
        session.setAttribute("username", loginForm.getUsername());
        return "redirect:/loginSuccess";
    }

    /**
     *
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model) {
       String sessionUsername = (String) session.getAttribute("username");

       if (sessionUsername != null) {
           model.addAttribute("username", sessionUsername);
           logger.info(sessionUsername + " successfully logged in");
           return "loginSuccess";
       } else {
           return "redirect:/login";
       }
    }

    /**
     *
     * @return
     */
    @GetMapping("/loginFailure")
    public String loginFailure() {
       logger.warn("failed to log in");
       return "loginFailure";
    }

    /**
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
       session.removeAttribute("username");
       logger.info("successfully logged out");
       return "logout";
    }
}