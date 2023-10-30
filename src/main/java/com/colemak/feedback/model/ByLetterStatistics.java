package com.colemak.feedback.model;

import jakarta.persistence.*;

@Entity
public class ByLetterStatistics {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String email;

    @Column
    private Character letter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private User user;

    @Column
    private Double letterTopSpeed;

    @Column
    private Double letterAvgSpeed;

    @Column
    private Integer numberOfSessions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getLetterTopSpeed() {
        return letterTopSpeed;
    }

    public void setLetterTopSpeed(Double letterTopSpeed) {
        this.letterTopSpeed = letterTopSpeed;
    }

    public Double getLetterAvgSpeed() { return letterAvgSpeed; }

    // function to return the color to display for the letter background in home page
    public String getMappedSpeed() {
        if (getLetterAvgSpeed() == -1)
            return "#cecece";
        else {
            int palierDeMaitrise = 50;
            int mappedSpeed = (int) Math.min(Math.round(getLetterAvgSpeed() * 255 / 50), 255);
            return "rgb(" + (255 - mappedSpeed) + "," + mappedSpeed + ",0)";
        }
    }

    public void setLetterAvgSpeed(Double letterAvgSpeed) {
        this.letterAvgSpeed = letterAvgSpeed;
    }

    public Integer getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(Integer numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }
}