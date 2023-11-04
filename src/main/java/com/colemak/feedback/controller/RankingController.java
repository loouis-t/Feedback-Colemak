package com.colemak.feedback.controller;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import com.colemak.feedback.model.Statistics;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class RankingController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ranking")
    public String ranking(Model model) {

        // Get all users
        List<User> allUsers = userRepository.findAll();
        Map<Double, User> userAvgSpeedMap = new TreeMap<>(Collections.reverseOrder());
        int currentUserTotalSession;
        double currentUserAvgSpeed;

        // Calculate average speed for each user
        for (User user : allUsers) {
            List<Statistics> userStatistics = user.getStatistics();
            if (userStatistics != null) {
                currentUserTotalSession = 0;
                currentUserAvgSpeed = 0;

                for (Statistics stat : userStatistics) {
                    currentUserTotalSession++;
                    currentUserAvgSpeed += stat.getWordsPerMinute();
                }

                if (currentUserTotalSession != 0) {
                    currentUserAvgSpeed /= currentUserTotalSession;
                }

                // Round to 2 decimal places
                userAvgSpeedMap.put((double) Math.round(currentUserAvgSpeed * 100) / 100, user);
            }
        }

        model.addAttribute("userAvgSpeedMap", userAvgSpeedMap);
        model.addAttribute("users", allUsers);

        return "ranking";
    }
}
