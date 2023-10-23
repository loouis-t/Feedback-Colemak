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
    private Integer wordsPerMinute;

    @Column
    private Integer accuracy;

    @Column
    private Integer clicksPerMinute;

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

    public Integer getWordsPerMinute() {
        return wordsPerMinute;
    }

    public void setWordsPerMinute(Integer wordsPerMinute) {
        this.wordsPerMinute = wordsPerMinute;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getClicksPerMinute() {
        return clicksPerMinute;
    }

    public void setClicksPerMinute(Integer clicksPerMinute) {
        this.clicksPerMinute = clicksPerMinute;
    }
}
