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
        Statistics test = new Statistics();

        test.setEmail(session.getAttribute("user").toString());
        statisticsRepository.save(test);

        // Récupérer les statistiques de la base de données en fonction de l'email de l'utilisateur
        Statistics statistics = statisticsRepository.findByEmail(session.getAttribute("user").toString()).get();

        // Calculer la moyenne des mots par minute
        int totalSessions = statisticsRepository.countByEmail(session.getAttribute("user").toString());
        int totalWPM = statisticsRepository.sumWordsPerMinuteByEmail(session.getAttribute("user").toString());

        int avgWPM = totalWPM / totalSessions;

        // Calculer la vitesse maximale (topSpeed) en récupérant le maximum des mots par minute
        int topSpeed = statisticsRepository.findMaxWordsPerMinuteByEmail(session.getAttribute("user").toString());

        // Mettre à jour l'entité Statistics avec la moyenne des mots par minute
        statistics.setWordsPerMinute(avgWPM);
        statisticsRepository.save(statistics);

        // Ajouter les attributs au modèle pour les afficher dans la page
        model.addAttribute("avgWPM", avgWPM);
        model.addAttribute("accuracy", statistics.getAccuracy());
        model.addAttribute("totalSessions", totalSessions);
        model.addAttribute("topSpeed", topSpeed);

        return "statistics";
    }
}
