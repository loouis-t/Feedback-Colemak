package com.colemak.feedback.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Statistics {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String email;

    @Column
    private LocalDate day;

    @Column
    private Double time;

    @Column
    private Double wordsPerMinute;

    @Column
    private Double accuracy;

    @Column
    private Double clicksPerMinute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Double getWordsPerMinute() {
        return wordsPerMinute;
    }

    public void setWordsPerMinute(Double wordsPerMinute) {
        this.wordsPerMinute = wordsPerMinute;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getClicksPerMinute() {
        return clicksPerMinute;
    }

    public void setClicksPerMinute(Double clicksPerMinute) {
        this.clicksPerMinute = clicksPerMinute;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
