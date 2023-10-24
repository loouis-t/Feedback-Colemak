package com.colemak.feedback.controller;

import com.colemak.feedback.FeedbackApplication;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.NoSuchAlgorithmException;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) throws NoSuchAlgorithmException {

        // Create a test user (development) : TO DELETE
        if (userRepository.findByEmail("test").isEmpty()) {
            User test = new User();
            test.setEmail("test");
            test.setName("test");
            test.setSurname("test");
            test.setPassword(FeedbackApplication.hashString("test"));
            userRepository.save(test);

        }

        // handle session : display login or logout button
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("activeSession", isLoggedIn ? "flex" : "none");
        model.addAttribute("noSession", isLoggedIn ? "none" : "flex");
        return "home";
    }
}