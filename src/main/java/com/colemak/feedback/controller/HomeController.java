package com.colemak.feedback.controller;

import com.colemak.feedback.FeedbackApplication;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @GetMapping("/generateRandomText")
    @ResponseBody
    public List<String> generateRandomText(@RequestParam int numberOfWords) {

        String filePath = "src/main/java/com/colemak/feedback/controller/wordlist.txt";

        List<String> randomWords = new ArrayList<>();

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

        System.out.println(randomWords);

        return randomWords;
    }
}