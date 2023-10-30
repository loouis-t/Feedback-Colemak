package com.colemak.feedback.controller;

import com.colemak.feedback.FeedbackApplication;
import com.colemak.feedback.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SettingsRepository settingsRepository;

    @Autowired
    ByLetterStatisticsRepository byLetterStatisticsRepository;

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String register(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "surname", required = false) String surname, @RequestParam(value = "password", required = false) String password, Model model, HttpSession session) throws NoSuchAlgorithmException {
        // redirect to home page if user is already logged in
        if (session.getAttribute("user") != null)
            return "redirect:/";

        // check if POST inputs are null
        if (email != null && name != null && surname != null && password != null) {
            // check if email is already in use
            if (userRepository.findByEmail(email).isEmpty()) {
                // create new user and save to database
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setSurname(surname);
                user.setPassword(FeedbackApplication.hashString(password));

                // save user to database
                userRepository.save(user);

                // Handle errors while saving settings to DB
                try {
                    // Create default settings for user
                    Settings userSettings = new Settings();
                    userSettings.setEmail(email);
                    userSettings.setTextLength(20);
                    userSettings.setDarkMode(false);
                    userSettings.setEmulateColemak(true);
                    userSettings.setUser(user); // TODO : useless ?
                    settingsRepository.save(userSettings);

                    // Add these default settings to user
                    user.setSettings(userSettings);
                } catch (Exception e) {
                    System.out.println(e);
                    model.addAttribute("typeError", "Error while trying to save user settings in db.");
                    return "register";
                }

                // Handle errors while saving ByLetterStatistics to DB
                try {
                    createDefaultByLetterStatistics(user);
                } catch (Exception e) {
                    System.out.println(e);
                    model.addAttribute("typeError", "Error while trying to save default ByLetterStatistics in db.");
                    return "register";
                }


                return "redirect:/login";
            } else {
                // display error message if email is already in use
                model.addAttribute("error", "block");
                return "register";
            }
        }
        // if nothing is POSTed, display the register page
        model.addAttribute("error", "none");
        return "register";
    }

    // Create default ByLetterStatistics for a user (one for each letter, corresponding to the user's email)
    public void createDefaultByLetterStatistics(User user) {
        Character[] letters = {'E', 'N', 'I', 'T', 'R', 'L', 'S', 'A', 'U', 'O', 'D', 'Y', 'C', 'H', 'G', 'M', 'P', 'B', 'K', 'V', 'W', 'F', 'Z', 'X', 'Q', 'J'};
        // Loop through all letters
        for (int i = 0; i < 26; i++) {
            // Create a new ByLetterStatistics object
            ByLetterStatistics specificLetterStatistic = new ByLetterStatistics();


            // Set its attributes
            specificLetterStatistic.setEmail(user.getEmail());
            specificLetterStatistic.setLetter(letters[i]); // get letter in keybr order
            specificLetterStatistic.setLetterTopSpeed(-1.0);
            specificLetterStatistic.setLetterAvgSpeed(-1.0);
            specificLetterStatistic.setNumberOfSessions(0);
            specificLetterStatistic.setUser(user);

            // Save it to DB
            byLetterStatisticsRepository.save(specificLetterStatistic);
        }
    }
}