package com.colemak.feedback.controller;

import com.colemak.feedback.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.colemak.feedback.FeedbackApplication;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "password", required = false) String password, Model model) throws NoSuchAlgorithmException {
        if (email != null || password != null) {
            if (userRepository.findByEmail(email).isPresent() && userRepository.findByEmail(email).get().getPassword().equals(FeedbackApplication.hashString(password))) {
                return "redirect:/";
            } else {
                model.addAttribute("display", "block");
                return "login";
            }
        }
        model.addAttribute("display", "none");
        return "login";
    }
}