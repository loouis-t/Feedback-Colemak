package com.colemak.feedback.controller;

import com.colemak.feedback.FeedbackApplication;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;

@Controller
public class RegisterController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String register(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "surname", required = false) String surname, @RequestParam(value = "password", required = false) String password, Model model) throws NoSuchAlgorithmException {
        if (email != null && name != null && surname != null && password != null) {
            if (userRepository.findByEmail(email).isEmpty()) {
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setSurname(surname);
                user.setPassword(FeedbackApplication.hashString(password));
                userRepository.save(user);
                return "redirect:/login";
            } else {
                model.addAttribute("display", "block");
                return "register";
            }
        }
        model.addAttribute("display", "none");
        return "register";
    }
}