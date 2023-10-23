package com.colemak.feedback.controller;

import com.colemak.feedback.model.Statistics;
import com.colemak.feedback.model.StatisticsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {
    @Autowired
    StatisticsRepository statisticsRepository;

    @GetMapping("/statistics")
    public String statistics(Model model, HttpSession session) {
        int totalSessions = -1;
        double avgWPM = -1;
        double avgAccuracy = -1;
        double topSpeed = -1;

        // Get current user's email (from current session)
        String currentUser = session.getAttribute("user").toString();

        // TO BE DELETED : TESTS
        Statistics test = new Statistics();
        test.setEmail(currentUser);
        test.setAccuracy(.5);
        test.setWordsPerMinute(50.0);
        statisticsRepository.save(test);

        test = new Statistics();
        test.setEmail(currentUser);
        test.setAccuracy(1.0);
        test.setWordsPerMinute(100.0);
        statisticsRepository.save(test);

        // Récupérer les statistiques de la base de données en fonction de l'email de l'utilisateur (check if empty)
        if (statisticsRepository.findByEmail(currentUser).isPresent()) {
            // Get total number of sessions
            totalSessions = statisticsRepository.countByEmail(currentUser);

            // Divide to get average
            avgWPM = (double) statisticsRepository.sumWordsPerMinuteByEmail(currentUser) / totalSessions;

            avgAccuracy = (double) statisticsRepository.sumAccuracyByEmail(currentUser) / totalSessions;

            // Calculate topSpeed (max words per minute)
            topSpeed = statisticsRepository.findMaxWordsPerMinuteByEmail(currentUser);
        }

        // Ajouter les attributs au modèle pour les afficher dans la page
        model.addAttribute("avgWPM", avgWPM == -1 ? "N/A" : avgWPM);
        model.addAttribute("accuracy", avgAccuracy == -1 ? "N/A" : avgAccuracy);
        model.addAttribute("totalSessions", totalSessions == -1 ? "N/A" : totalSessions);
        model.addAttribute("topSpeed", topSpeed == -1 ? "N/A" : topSpeed);

        return "statistics";
    }
}
