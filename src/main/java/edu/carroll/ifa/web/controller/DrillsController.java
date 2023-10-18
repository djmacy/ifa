package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for the page that displays the drills for an associated user based on their age.
 */
@Controller
public class DrillsController {
    private static final Logger logger = LoggerFactory.getLogger(DrillsController.class);
    private final UserService userService;

    /**
     * Constructs a DrillsController instance with the UserService dependency.
     * @param userService - UserService implementation used in the DrillsController
     */
    public DrillsController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles a GET request for the "/soccerDrills" page. It retreives the user's age based on the session username
     * and adds it to the model.
     * @param model - Model object used for storing attributes
     * @param session - HttpSession object for managing session information
     * @return the soccerDrills html page
     */
    @GetMapping("/soccerDrills")
    public String soccerDrills(Model model, HttpSession session) {
        // get the username saved in the session
        String username = (String) session.getAttribute("username");

        if (username == null) {
            logger.info("User is not logged in");
            return "redirect:/login";
        }

        // get the age of the user with the given username
        int userAge = userService.getUserAge(username);
        // add the age to the model
        model.addAttribute("userAge", userAge);
        logger.info("/soccerDrills: user '{}' accessed their drills", username);
        return "soccerDrills";
    }
}
