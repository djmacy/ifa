package edu.carroll.ifa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Returns the index html page.
     * @return index
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
