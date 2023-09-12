package edu.carroll.ifa.web.controller;

import edu.carroll.ifa.jpa.model.User;
import edu.carroll.ifa.jpa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DrillsController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/soccer_drills")
    public String soccerDrills(Model model) {
        User user = repository.getReferenceById(2);
        model.addAttribute("user", user);
        return "soccer_drills";
    }
}
