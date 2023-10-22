package com.colemak.feedback.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        // handle session : display login or logout button
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("activeSession", isLoggedIn ? "flex" : "none");
        model.addAttribute("noSession", isLoggedIn ? "none" : "flex");
        return "home";
    }
}