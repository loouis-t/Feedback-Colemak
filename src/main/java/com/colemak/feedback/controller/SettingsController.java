package com.colemak.feedback.controller;

import com.colemak.feedback.model.Settings;
import com.colemak.feedback.model.SettingsRepository;
import com.colemak.feedback.model.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@Controller
public class SettingsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SettingsRepository settingsRepository;

    @GetMapping("/settings")
    public String settings(Model model, HttpSession session) {

        Object currentSessionObject = session.getAttribute("user");
        if (currentSessionObject == null)
            return "redirect:/";

        String currentUserEmail = currentSessionObject.toString();
        userRepository.findByEmail(currentUserEmail).ifPresent(user -> {
            model.addAttribute("isInDarkMode", user.getSettings().isDarkMode());
            model.addAttribute("textLength", user.getSettings().getTextLength());
            model.addAttribute("isEmulatingColemak", user.getSettings().isEmulateColemak());
        });
        
        return "settings";
    }


    @RequestMapping(value = "/update-settings", method = {RequestMethod.POST})
    public ResponseEntity<String> setUserSettings(Model model, HttpSession session, @RequestParam(value = "darkMode", required = false) String darkMode, @RequestParam(value = "textLength", required = false) String textLength, @RequestParam(value = "emulateColemak", required = false) String emulateColemak) {
        Object currentSessionObject = session.getAttribute("user");

        // return not allowed if not logged in
        if (currentSessionObject == null)
            return new ResponseEntity<>("Not allowed", HttpStatus.UNAUTHORIZED);

        // Get current user's email (from current session)
        String currentUserEmail = currentSessionObject.toString();
        userRepository.findByEmail(currentUserEmail).ifPresent(user -> {
            Settings s = user.getSettings();
            if (darkMode != null)
                s.setDarkMode(darkMode.equals("true"));
            if (textLength != null)
                s.setTextLength(Integer.parseInt(textLength));
            if (emulateColemak != null)
                s.setEmulateColemak(emulateColemak.equals("true"));
            settingsRepository.save(s);
            userRepository.save(user);
        });

        return new ResponseEntity<>("Data saved successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/emulate-colemak", method = {RequestMethod.GET})
    @ResponseBody
    public Boolean emulateColemak(HttpSession session) {
        Object currentSessionObject = session.getAttribute("user");

        // return true to emulate by default if not logged in
        if (currentSessionObject == null)
            return true;

        // Get current user's email (from current session)
        String currentUserEmail = currentSessionObject.toString();
        // get user's settings or true, to emulate by default
        return userRepository.findByEmail(currentUserEmail).map(user -> user.getSettings().isEmulateColemak()).orElse(true);
    }
}
