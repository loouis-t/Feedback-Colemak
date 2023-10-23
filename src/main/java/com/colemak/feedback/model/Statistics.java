package com.colemak.feedback.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Statistics {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String email;

    @Column
    private Date time;

    @Column
    private Double wordsPerMinute;

    @Column
    private Double accuracy;

    @Column
    private Double clicksPerMinute;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
