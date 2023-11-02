package com.colemak.feedback.controller;

import org.springframework.ui.Model;
import com.colemak.feedback.model.Statistics;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class RankingController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ranking")
    public String ranking(Model model, HttpSession session) {

        List<User> users = userRepository.findAll();
        List<Integer> avgSpeedList = new ArrayList<>();
        int iterStat = 0;
        int totalSessions;
        double avgWPM = 0;

        for (User user : users) {
            List<Statistics> stats = user.getStatistics();
            if (stats != null) {
                totalSessions = 0;
                avgWPM = 0;

                for (Statistics stat : stats) {
                    totalSessions++;
                    avgWPM += stat.getWordsPerMinute();
                }

                if (totalSessions != 0) {
                    avgWPM /= totalSessions;
                }

                avgSpeedList.add((int) avgWPM);
            }
        }

        avgSpeedList.sort(Comparator.reverseOrder());

        model.addAttribute("iterStat", iterStat);
        model.addAttribute("avgSpeedList", avgSpeedList);
        model.addAttribute("users", users);

        return "ranking";
    }
}
