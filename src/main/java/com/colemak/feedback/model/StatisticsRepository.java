package com.colemak.feedback.model;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Long> {
    @Query("SELECT id FROM Statistics WHERE email = ?1")
    Optional<List<Integer>> findByEmail(String email);

    // Méthode pour compter le nombre total de sessions en fonction de l'email de l'utilisateur
    int countByEmail(String email);

    // Méthode pour calculer la somme des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(wordsPerMinute) FROM Statistics WHERE email = ?1")
    double sumWordsPerMinuteByEmail(String email);

    // Méthode pour récupérer le maximum des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT MAX(wordsPerMinute) FROM Statistics WHERE email = ?1")
    double findMaxWordsPerMinuteByEmail(String email);

    // Méthode pour calculer la somme des 'accuracy' en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(accuracy) FROM Statistics WHERE email = ?1")
    double sumAccuracyByEmail(String email);
}
