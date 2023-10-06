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



/**
 * Controller class for the login page. It will check to see if the user is valid and then redirect them to the loginSuccess page.
 * It will also make sure that someone in the loginSuccess page is a valid user.
 */

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    /**
     * Constructs a LoginController instance with the UserService dependency.
     * @param userService - UserService implementation used in the LoginController
     */
   public LoginController(UserService userService) {
       this.userService = userService;
   }

    /**
     * Handles the GET request for the /login page. It also initializes the login form model and displays the page.
     * @param model - Model object for storing attributes associated with logging in
     * @return login page
     */
    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        logger.info("Visited the login page");
        return "login";
    }

    /**
     * Handles the POST request for the /login page. It also authenticates the user to make they have permission to be on the page.
     * @param loginForm - LoginForm information that is submitted by the user
     * @param result - BindingResult for validating the form information
     * @param session - HttpSession for managing session data
     * @param attrs - RedirectAttributes for passing attributes to the /loginSuccess page
     * @return login page if user has incorrect information, otherwise the loginSuccess page
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
     * Handles the GET request for the /loginSuccess page as well as display the login success page or redirecting them to
     * the login page if they have not logged in yet.
     * @param session - HttpSession for managing session data
     * @param model - Model for storing the attributes of the user
     * @return loginSuccess page if the user has been authenticated, otherwise login page
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
       session.removeAttribute("username");
       logger.info("successfully logged out");
       return "logout";
    }

}