package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DrillsController {

    @Autowired
    private UserService userService;

    /**
     * Constructs a DrillsController instance with the UserService dependency.
     * @param userService - UserService implementation used in the DrillsController
     */
    @Autowired
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
        return "soccerDrills";
    }
}
