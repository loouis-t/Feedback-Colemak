package com.colemak.feedback.controller;

import com.colemak.feedback.model.Statistics;
import com.colemak.feedback.model.StatisticsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StatisticsController {
    @Autowired
    StatisticsRepository statisticsRepository;

    @GetMapping("/statistics")
    public String statistics(Model model, HttpSession session) {
        Integer totalTime = 0;
        int totalSessions = -1;
        double avgWPM = -1;
        double avgAccuracy = -1;
        double topSpeed = -1;

        Integer dayTotalTime = 0;
        int dayTotalSessions = -1;
        double dayAvgWPM = -1;
        double dayAvgAccuracy = -1;
        double dayTopSpeed = -1;

        // Get current user's email (from current session)
        String currentUser = session.getAttribute("user").toString();

        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // TO BE DELETED : TESTS
        Statistics test = new Statistics();
        test.setEmail(currentUser);
        test.setAccuracy(.5);
        test.setWordsPerMinute(50.0);
        test.setDay(LocalDate.now());
        statisticsRepository.save(test);

        test = new Statistics();
        test.setEmail(currentUser);
        test.setAccuracy(1.0);
        test.setWordsPerMinute(100.0);
        test.setDay(LocalDate.now());
        statisticsRepository.save(test);

        test = new Statistics();
        test.setEmail(currentUser);
        test.setAccuracy(1.0);
        test.setWordsPerMinute(100.0);
        test.setDay(LocalDate.now().minusDays(1));
        statisticsRepository.save(test);

        // Récupérer toutes les statistiques de la base de données en fonction de l'email de l'utilisateur (check if empty)
        if (statisticsRepository.findByEmail(currentUser).isPresent()) {
            // Get total time
            totalTime = statisticsRepository.sumTimeByEmail(currentUser, null);

            // Get total number of sessions
            totalSessions = statisticsRepository.countByEmail(currentUser, null);

            // Divide to get average
            avgWPM = (double) statisticsRepository.sumWordsPerMinuteByEmail(currentUser, null) / totalSessions;

            avgAccuracy = (double) statisticsRepository.sumAccuracyByEmail(currentUser, null) / totalSessions;

            // Calculate topSpeed (max words per minute)
            topSpeed = statisticsRepository.findMaxWordsPerMinuteByEmail(currentUser, null);

            // Get total time of the day
            dayTotalTime = statisticsRepository.sumTimeByEmail(currentUser, currentDate);

            // Get total number of sessions of the day
            dayTotalSessions = statisticsRepository.countByEmail(currentUser, currentDate);

            // Divide to get average of the day
            dayAvgWPM = (double) statisticsRepository.sumWordsPerMinuteByEmail(currentUser, currentDate) / totalSessions;

            dayAvgAccuracy = (double) statisticsRepository.sumAccuracyByEmail(currentUser, currentDate) / totalSessions;

            // Calculate topSpeed of the day (max words per minute)
            dayTopSpeed = statisticsRepository.findMaxWordsPerMinuteByEmail(currentUser, currentDate);
        }

        // Ajouter les attributs au modèle pour les afficher dans la page
        if (totalTime != null) {
            model.addAttribute("totalTime", totalTime);
        } else {
            model.addAttribute("totalTime", "N/A");
        }
        model.addAttribute("totalSessions", totalSessions == -1 ? "N/A" : totalSessions);
        model.addAttribute("topSpeed", topSpeed == -1 ? "N/A" : topSpeed);
        model.addAttribute("avgWPM", avgWPM == -1 ? "N/A" : ((double) Math.round(avgWPM * 10))/10);
        model.addAttribute("avgAccuracy", avgAccuracy == -1 ? "N/A" : ((double) Math.round(avgAccuracy * 10))/10);

        if (dayTotalTime != null) {
            model.addAttribute("dayTotalTime", dayTotalTime);
        } else {
            model.addAttribute("dayTotalTime", "N/A");
        }
        model.addAttribute("dayTotalSessions", dayTotalSessions == -1 ? "N/A" : dayTotalSessions);
        model.addAttribute("dayTopSpeed", dayTopSpeed == -1 ? "N/A" : dayTopSpeed);
        model.addAttribute("dayAvgWPM", dayAvgWPM == -1 ? "N/A" : ((double) Math.round(dayAvgWPM * 10))/10);
        model.addAttribute("dayAvgAccuracy", dayAvgAccuracy == -1 ? "N/A" : ((double) Math.round(dayAvgAccuracy * 10))/10);

        return "statistics";
    }
}
