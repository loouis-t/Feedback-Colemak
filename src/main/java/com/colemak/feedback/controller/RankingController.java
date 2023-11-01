package com.colemak.feedback.controller;

import ch.qos.logback.core.model.Model;
import com.colemak.feedback.model.User;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class RankingController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ranking")
    public String ranking(Model model, HttpSession session) {

        return "ranking";
    }
}
