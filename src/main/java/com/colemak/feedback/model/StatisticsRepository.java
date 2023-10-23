package com.colemak.feedback.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Long> {
    Optional<Statistics> findByEmail(String email);

    // Méthode pour compter le nombre total de sessions en fonction de l'email de l'utilisateur
    int countByEmail(String email);

    // Méthode pour calculer la somme des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT SUM(s.wordsPerMinute) FROM Statistics s WHERE s.email = ?1")
    int sumWordsPerMinuteByEmail(String email);

    // Méthode pour récupérer le maximum des mots par minute en fonction de l'email de l'utilisateur
    @Query("SELECT MAX(s.wordsPerMinute) FROM Statistics s WHERE s.email = ?1")
    int findMaxWordsPerMinuteByEmail(String email);
}
