package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.Feedback;
import com.example.gestionmenagere.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByAuteur(Utilisateur auteur);

    List<Feedback> findByAuteurId(Long auteurId);

    List<Feedback> findByDestinataire(Utilisateur destinataire);

    List<Feedback> findByDestinataireId(Long destinataireId);

    List<Feedback> findByDestinataireIdOrderByDateCreationDesc(Long destinataireId);

    @Query("SELECT AVG(f.note) FROM Feedback f WHERE f.destinataire.id = :destinataireId")
    Double calculerNoteMoyenne(@Param("destinataireId") Long destinataireId);

    Long countByDestinataire(Utilisateur destinataire);

    boolean existsByAuteurAndDestinataire(Utilisateur auteur, Utilisateur destinataire);
}
