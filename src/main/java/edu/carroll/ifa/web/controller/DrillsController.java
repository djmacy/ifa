package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import edu.carroll.ifa.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DrillsController {

    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(DrillsController.class);

    @Autowired
    public DrillsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/soccerDrills")
    public String soccerDrills(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        int userAge = userService.getUserAge(username);
        model.addAttribute("userAge", userAge);
        logger.info(username + " successfully visited the soccer drills page");
        return "soccerDrills";
    }
}
