package com.colemak.feedback.controller;

import com.colemak.feedback.model.Statistics;
import com.colemak.feedback.model.StatisticsRepository;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StatisticsController {
    @Autowired
    StatisticsRepository statisticsRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/statistics")
    public String statistics(Model model, HttpSession session) {

        Double totalTime = 0.0;
        int totalSessions = -1;
        double avgWPM = -1;
        double avgAccuracy = -1;
        double topSpeed = -1;

        Double dayTotalTime = 0.0;
        int dayTotalSessions = -1;
        double dayAvgWPM = -1;
        double dayAvgAccuracy = -1;
        double dayTopSpeed = -1;

        // Créer des listes pour les données du graphique
        List<LocalDate> dates = new ArrayList<>();
        List<Double> speeds = new ArrayList<>();
        List<Double> accuracies = new ArrayList<>();

        // redirect to login page if user is not logged in
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        // Get current user's email (from current session)
        String currentUser = session.getAttribute("user").toString();

        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Récupérer toutes les statistiques de la base de données en fonction de l'email de l'utilisateur (check if empty)
        Optional<List<Statistics>> stats = statisticsRepository.findByEmail(currentUser);
        if (stats.isPresent() && !stats.get().isEmpty()) {
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


            for (Statistics stat : stats.get()) {
                speeds.add(stat.getWordsPerMinute());
                accuracies.add(stat.getAccuracy());
            }
        }

        // Ajouter les attributs au modèle pour les afficher dans la page
        model.addAttribute("totalTime", totalTime == null ? "N/A" : ((double) Math.round(totalTime * 10)) / 10);
        model.addAttribute("totalSessions", totalSessions == -1 ? "N/A" : totalSessions);
        model.addAttribute("topSpeed", topSpeed == -1 ? "N/A" : ((double) Math.round(topSpeed * 10)) / 10);
        model.addAttribute("avgWPM", avgWPM == -1 ? "N/A" : ((double) Math.round(avgWPM * 10)) / 10);
        model.addAttribute("avgAccuracy", avgAccuracy == -1 ? "N/A" : ((double) Math.round(avgAccuracy * 10)) / 10);

        model.addAttribute("dayTotalTime", dayTotalTime == null ? "N/A" : ((double) Math.round(dayTotalTime * 10)) / 10);
        model.addAttribute("dayTotalSessions", dayTotalSessions == -1 ? "N/A" : dayTotalSessions);
        model.addAttribute("dayTopSpeed", dayTopSpeed == -1 ? "N/A" : ((double) Math.round(dayTopSpeed * 10)) / 10);
        model.addAttribute("dayAvgWPM", dayAvgWPM == -1 ? "N/A" : ((double) Math.round(dayAvgWPM * 10)) / 10);
        model.addAttribute("dayAvgAccuracy", dayAvgAccuracy == -1 ? "N/A" : ((double) Math.round(dayAvgAccuracy * 10)) / 10);

        // Ajouter les listes au modèle pour les utiliser dans la vue
        model.addAttribute("speeds", speeds);
        model.addAttribute("accuracies", accuracies);

        return "statistics";
    }

    @RequestMapping(value = "/add-stats", method = {RequestMethod.POST})
    public ResponseEntity<String> addStats(Model model, HttpSession session, @RequestParam(value = "wpm", required = false) Double wpm, @RequestParam(value = "accuracy", required = false) Double accuracy, @RequestParam(value = "cpm", required = false) Double cpm, @RequestParam(value = "elapsedTime", required = false) Double time) {
        // Check if user is logged in
        if (session.getAttribute("user") == null)
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);

        // Check if POST inputs are null
        if (wpm != null && accuracy != null && cpm != null && time != null) {
            // Get current user's email (from current session)
            String currentUser = session.getAttribute("user").toString();
            User user = userRepository.findByEmail(currentUser).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            // Créer un nouvel objet statistique
            Statistics statistics = new Statistics();

            // Définir les attributs de l'objet statistique
            statistics.setEmail(currentUser);
            statistics.setWordsPerMinute(wpm);
            statistics.setAccuracy(accuracy);
            statistics.setClicksPerMinute(cpm);
            statistics.setTime(time);
            statistics.setDay(LocalDate.now());

            // Associer l'objet statistique à l'utilisateur
            statistics.setUser(user);

            // Ajouter l'objet statistique à la liste des statistiques de l'utilisateur
            user.getStatistics().add(statistics);

            // Enregistrer l'objet statistique dans la base de données
            statisticsRepository.save(statistics);

            // Enregistrer l'utilisateur dans la base de données
            userRepository.save(user);

            return new ResponseEntity<>("Data saved successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
    }
}


