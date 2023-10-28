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

    public Double getLetterAvgSpeed() {
        return letterAvgSpeed;
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