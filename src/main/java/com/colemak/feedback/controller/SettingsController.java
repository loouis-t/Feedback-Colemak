package com.colemak.feedback.controller;

import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.NoSuchAlgorithmException;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings(Model model, HttpSession session) throws NoSuchAlgorithmException {

        return "settings";
    }


    @GetMapping("setUserSettings")
    public String setUserSettings(Model model, HttpSession session) throws NoSuchAlgorithmException {

        return "settings";
    }
}
