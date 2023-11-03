package com.colemak.feedback.controller;

import com.colemak.feedback.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) throws NoSuchAlgorithmException {
        // handle session : display login or logout button
        Object currentUserSessionEmail = session.getAttribute("user");


        boolean isLoggedIn = currentUserSessionEmail != null;
        model.addAttribute("activeSession", isLoggedIn ? "flex" : "none");
        model.addAttribute("noSession", isLoggedIn ? "none" : "flex");

        if (isLoggedIn) {
            // Set letters statistics attributes
            User currentUser = null;
            if (userRepository.findByEmail(currentUserSessionEmail.toString()).isPresent())
                currentUser = userRepository.findByEmail(currentUserSessionEmail.toString()).get();

            // Get user's letters statistics from its settings (check if settings exist)
            if (currentUser != null)
                model.addAttribute("byLetterStatistics", currentUser.getByLetterStatistics());
        }


        return "home";
    }

    @GetMapping("/generateRandomText")
    @ResponseBody
    public List<String> generateRandomText(HttpSession session) {

        String filePath = "src/main/resources/wordlist.txt";
        List<String> randomWords = new ArrayList<>();

        // Set default number of words
        int numberOfWords = 20;

        // Get current sessions's email or null
        Object sessionEmail = session.getAttribute("user");

        // If user is logged in, get its text length preference from its settings
        if (sessionEmail != null && userRepository.findByEmail(sessionEmail.toString()).isPresent()) {
            // Get corresponding user from DB
            User currentUser = userRepository.findByEmail(sessionEmail.toString()).get();

            // Get user's text length preference from its settings (check if settings exist)
            if (currentUser.getSettings() != null)
                numberOfWords = currentUser.getSettings().getTextLength();
        }

        try {
            // Lire le contenu du fichier wordlist.txt
            Path file = Paths.get(filePath);
            List<String> lines = Files.readAllLines(file);

            Random random = new Random();

            // Sélectionner des mots aléatoires
            while (randomWords.size() < numberOfWords) {
                int randomIndex = random.nextInt(lines.size());
                randomWords.add(lines.get(randomIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return randomWords;
    }
}