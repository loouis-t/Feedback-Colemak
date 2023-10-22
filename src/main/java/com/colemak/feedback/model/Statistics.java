package com.colemak.feedback.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Statistics {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Date TotalTime;

    @Column
    private Integer TotalLessons;

    @Column
    private Integer TopSpeed;

    @Column
    private Integer AverageSpeed;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Date getTotalTime() {
        return TotalTime;
    }

    public Integer getTotalLessons() {
        return TotalLessons;
    }

    public Integer getTopSpeed() {
        return TopSpeed;
    }

    public Integer getAverageSpeed() {
        return AverageSpeed;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTotalTime(Date TotalTime) {
        this.TotalTime = TotalTime;
    }

    public void setTotalLessons(Integer TotalLessons) {
        this.TotalLessons = TotalLessons;
    }

    public void setTopSpeed(Integer TopSpeed) {
        this.TopSpeed = TopSpeed;
    }

    public void setAverageSpeed(Integer AverageSpeed) {
        this.AverageSpeed = AverageSpeed;
    }
}
