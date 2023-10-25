package com.colemak.feedback.model;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Long> {
    //@Query("SELECT email FROM Statistics WHERE email = ?1")
    Optional<List<Statistics>> findByEmail(String email);

    // Méthode pour compter le nombre total de sessions en fonction de l'email de l'utilisateur
    @Query("SELECT COUNT(id) FROM Statistics WHERE email = ?1 AND (?2 is null or day = ?2)")
    int countByEmail(String email, LocalDate day);

    // Méthode pour calculer la somme des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(wordsPerMinute) FROM Statistics WHERE email = ?1 AND (?2 is null or day = ?2)")
    double sumWordsPerMinuteByEmail(String email, LocalDate day);

    // Méthode pour récupérer le maximum des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT MAX(wordsPerMinute) FROM Statistics WHERE email = ?1 AND (?2 is null or day = ?2)")
    double findMaxWordsPerMinuteByEmail(String email, LocalDate day);

    // Méthode pour calculer la somme des 'accuracy' en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(accuracy) FROM Statistics WHERE email = ?1 AND (?2 is null or day = ?2)")
    double sumAccuracyByEmail(String email, LocalDate day);

    // Méthode pour récupérer le temps de chaque session en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(time) FROM Statistics WHERE email = ?1 AND (?2 is null or day = ?2)")
    Double sumTimeByEmail(String email, LocalDate day);
}
