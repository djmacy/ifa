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
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(DrillsController.class);
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
        String username = (String) session.getAttribute("username");
        int userAge = userService.getUserAge(username);
        model.addAttribute("userAge", userAge);
        logger.info(username + " successfully visited the soccer drills page");
        return "soccerDrills";
    }
}
