package com.colemak.feedback.controller;

import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

import jakarta.servlet.http.HttpSession;

import com.colemak.feedback.FeedbackApplication;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "password", required = false) String password, Model model, HttpSession session) throws NoSuchAlgorithmException {
        // redirect to home page if user is already logged in
        if (session.getAttribute("user") != null)
            return "redirect:/";

        // TODO : Delete this block -> Create a test user (development)
        if (userRepository.findByEmail("test").isEmpty()) {
            User test = new User();
            test.setEmail("test");
            test.setName("test");
            test.setSurname("test");
            test.setPassword(FeedbackApplication.hashString("test"));
            userRepository.save(test);
        }

        // check if POST inputs are null
        if (email != null && password != null) {
            // check if user exists in DB and if password is correct
            if (userRepository.findByEmail(email).isPresent() && userRepository.findByEmail(email).get().getPassword().equals(FeedbackApplication.hashString(password))) {
                // set session and redirect to home page
                session.setAttribute("user", email.toLowerCase());
                return "redirect:/";
            } else {
                // if user does not exist or password is incorrect, display error message
                model.addAttribute("error", "block");
                return "login";
            }
        }
        // if nothing is POSTed, display login page
        model.addAttribute("error", "none");
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        // on logout invalidate session and redirect to home page
        session.invalidate();
        return "redirect:/";
    }
}