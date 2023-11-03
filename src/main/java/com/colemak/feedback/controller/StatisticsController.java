package com.colemak.feedback.controller;

import com.colemak.feedback.model.*;
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
        String currentUserEmail = session.getAttribute("user").toString();

        // Get corresponding user from DB
        User currentUser;
        if (userRepository.findByEmail(currentUserEmail).isPresent())
            currentUser = userRepository.findByEmail(currentUserEmail).get();
        else
            throw new RuntimeException("Utilisateur non trouvé");

        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Récupérer toutes les statistiques de la base de données en fonction de l'email de l'utilisateur (check if empty)
        List<Statistics> currentUserStatistics = currentUser.getStatistics();
        if (currentUserStatistics != null) {

            // set to O : at this point of the code, we are sure that currentUserStatistics is not null
            totalSessions = 0;
            avgWPM = 0;
            avgAccuracy = 0;

            dayTotalSessions = 0;
            dayAvgWPM = 0;
            dayAvgAccuracy = 0;

            // Get precise statistics iterating on stats
            for (Statistics stat : currentUserStatistics) {
                // GLOBAL STATS
                // sum up all time per session
                totalTime += stat.getTime();
                // increment number of sessions for each session found
                totalSessions++;
                // sum up all words per minute
                avgWPM += stat.getWordsPerMinute();
                // sum up all accuracies
                avgAccuracy += stat.getAccuracy();

                // check if current session's words per minute is greater than top speed
                if (stat.getWordsPerMinute() > topSpeed)
                    topSpeed = stat.getWordsPerMinute();

                // DAILY STATS
                if (stat.getDay().equals(currentDate)) {
                    // sum up all time per session
                    dayTotalTime += stat.getTime();
                    // increment number of sessions for each session found
                    dayTotalSessions++;
                    // sum up all words per minute
                    dayAvgWPM += stat.getWordsPerMinute();
                    // sum up all accuracies
                    dayAvgAccuracy += stat.getAccuracy();
                    // check if current session's words per minute is greater than top speed
                    if (stat.getWordsPerMinute() > dayTopSpeed)
                        dayTopSpeed = stat.getWordsPerMinute();
                }

                speeds.add(stat.getWordsPerMinute());
                accuracies.add(stat.getAccuracy());
            }

            // Divide avg vars by number of sessions to actually get an average
            // Check if totalSessions is not 0 to avoid division by 0
            if (totalSessions != 0 && dayTotalSessions != 0) {
                avgWPM /= totalSessions;
                avgAccuracy /= totalSessions;
                dayAvgWPM /= dayTotalSessions;
                dayAvgAccuracy /= dayTotalSessions;
            }
        }

        // Ajouter les attributs au modèle pour les afficher dans la page
        model.addAttribute("totalTime", totalTime == 0.0 ? "N/A" : ((double) Math.round(totalTime * 10)) / 10);
        model.addAttribute("totalSessions", totalSessions == -1 ? "N/A" : totalSessions);
        model.addAttribute("topSpeed", topSpeed == -1 ? "N/A" : ((double) Math.round(topSpeed * 10)) / 10);
        model.addAttribute("avgWPM", avgWPM == -1 ? "N/A" : ((double) Math.round(avgWPM * 10)) / 10);
        model.addAttribute("avgAccuracy", avgAccuracy == -1 ? "N/A" : ((double) Math.round(avgAccuracy * 10)) / 10);

        model.addAttribute("dayTotalTime", dayTotalTime == 0.0 ? "N/A" : ((double) Math.round(dayTotalTime * 10)) / 10);
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
    public ResponseEntity<String> addStats(Model model, HttpSession session, @RequestParam(value = "wpm", required = false) Double wpm, @RequestParam(value = "accuracy", required = false) Double accuracy, @RequestParam(value = "cpm", required = false) Double cpm, @RequestParam(value = "elapsedTime", required = false) Double time, @RequestParam(value = "perKeyWpm", required = false) Double[] perKeyWpm) {
        // Check if user is logged in
        if (session.getAttribute("user") == null)
            return new ResponseEntity<>("User not logged in", HttpStatus.UNAUTHORIZED);

        // Check if POST inputs are null
        if (wpm != null && accuracy != null && cpm != null && time != null && perKeyWpm != null) {
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


            // update user's byLetterStatistics average speeds
            List<ByLetterStatistics> byLetterStatistics = user.getByLetterStatistics();
            for (ByLetterStatistics letterStatistics : byLetterStatistics) {
                int letterIndex = (int) letterStatistics.getLetter() - 'A' + 1;
                System.out.println(perKeyWpm[letterIndex]);
                // if letter has been typed at least once
                if (perKeyWpm[letterIndex] != -1) {
                    int numberOfSessions = letterStatistics.getNumberOfSessions() + 1;
                    double oldLetterAvgSpeed = letterStatistics.getLetterAvgSpeed();
                    double newLetterAvgSpeed = oldLetterAvgSpeed + (perKeyWpm[(int) letterStatistics.getLetter() - 'A' + 1] - oldLetterAvgSpeed) / numberOfSessions;

                    letterStatistics.setNumberOfSessions(numberOfSessions);
                    letterStatistics.setLetterAvgSpeed(newLetterAvgSpeed);

                    // get old topSpeed then update if current is greater
                    double topSpeed = letterStatistics.getLetterTopSpeed();
                    if (perKeyWpm[letterIndex] > topSpeed)
                        letterStatistics.setLetterTopSpeed(perKeyWpm[letterIndex]);
                }
            }
            user.setByLetterStatistics(byLetterStatistics);

            // Enregistrer l'utilisateur dans la base de données
            userRepository.save(user);

            return new ResponseEntity<>("Data saved successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
    }

    // Endpoint to get letter color to display without reloading home page
    @RequestMapping(value = "/get-letter-stats", method = {RequestMethod.GET})
    public ResponseEntity<String> getLetterStats(@RequestParam(value = "letter", required = true) Character letter, HttpSession session) {
        // get current user's email Object from session
        Object currentUserEmailObject = session.getAttribute("user");

        System.out.println("letter : " + letter);

        // Check if user is logged in
        if (currentUserEmailObject == null)
            return new ResponseEntity<>("{\"background\": \"var(--keys)\"}", HttpStatus.UNAUTHORIZED);

        String currentUserEmail = currentUserEmailObject.toString();

        if (letter != null && userRepository.findByEmail(currentUserEmail).isPresent()) {
            List<ByLetterStatistics> letterStatistics = userRepository.findByEmail(currentUserEmail).get().getByLetterStatistics();

            // return letter color of concerned letter
            for (ByLetterStatistics stat : letterStatistics) {
                if (stat.getLetter() == letter)
                    return new ResponseEntity<>("{\"background\": \"" + stat.getMappedSpeed() + "\"}", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("{\"background\": \"var(--keys)\"}", HttpStatus.BAD_REQUEST);
    }
}
