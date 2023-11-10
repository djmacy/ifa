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
        // adds the login form to the model
        model.addAttribute("loginForm", new LoginForm());
        logger.info("New user has visited the login page");
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
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session, RedirectAttributes attrs, Model model) {
        // checks if there are any errors when the user trys to log in
        if (result.hasErrors()) {
            logger.debug("There were {} errors", result.getErrorCount());
            return "login";
        }
        // checks if the user's name and password are invalid
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            // adds and error message to the result
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            logger.info("login failed username or password do not match known users");
            return "login";
        }
        // adds the username to the redirect attributes
        attrs.addAttribute("username", loginForm.getUsername());

        // setting the username to the session
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
        // gets the username from the session
       String sessionUsername = (String) session.getAttribute("username");
        // checks if there is a username in the session. Checks if user is logged in
       if (sessionUsername != null) {
           // adds the username to the model
           model.addAttribute("username", sessionUsername);
           logger.info("User '{}' successfully logged in", sessionUsername);
           return "loginSuccess";
       } else {
           logger.info("User is not logged in");
           return "redirect:/login";
       }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // removes the username from the session
       session.removeAttribute("username");
       logger.info("successfully logged out");
       return "logout";
    }

}