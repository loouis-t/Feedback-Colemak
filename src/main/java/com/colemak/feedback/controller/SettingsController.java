package com.colemak.feedback.controller;

import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.NoSuchAlgorithmException;

public class SettingsController {

    @GetMapping("/settings")
    public String settings(Model model, HttpSession session) throws NoSuchAlgorithmException {

        return "settings";
    }
}
