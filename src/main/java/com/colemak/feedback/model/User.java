package com.colemak.feedback.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    //@JoinColumn(name = "email")
    private Settings settings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statistics> statistics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ByLetterStatistics> byLetterStatistics;

    public List<ByLetterStatistics> getByLetterStatistics() {
        return byLetterStatistics;
    }

    public void setByLetterStatistics(List<ByLetterStatistics> byLetterStatistics) {
        this.byLetterStatistics = byLetterStatistics;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistics> statistics) {
        this.statistics = statistics;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
