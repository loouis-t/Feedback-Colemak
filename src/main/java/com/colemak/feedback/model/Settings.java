package com.colemak.feedback.model;

import jakarta.persistence.*;

@Entity
public class Settings {
    @Id
    @Column
    private String email;

    @Column
    private int textLength;

    @Column
    boolean darkMode;

    @Column
    boolean emulateColemak;

    @OneToOne(mappedBy = "settings", cascade = CascadeType.ALL)
    private User user;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isEmulateColemak() {
        return emulateColemak;
    }

    public void setEmulateColemak(boolean emulateColemak) {
        this.emulateColemak = emulateColemak;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}