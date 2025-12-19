package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.enums.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AideMenagereRepository extends JpaRepository<AideMenagere, Long> {

    Optional<AideMenagere> findByEmail(String email);

    List<AideMenagere> findByActifTrue();

    List<AideMenagere> findByDisponibilite(Disponibilite disponibilite);

    List<AideMenagere> findByExperienceAnneesGreaterThanEqual(Integer annees);

    @Query("SELECT a FROM AideMenagere a WHERE a.actif = true AND a.competences LIKE %:competence%")
    List<AideMenagere> findByCompetenceContaining(@Param("competence") String competence);

    @Query("SELECT a FROM AideMenagere a WHERE a.actif = true AND " +
            "(a.nom LIKE %:recherche% OR a.prenom LIKE %:recherche% OR a.competences LIKE %:recherche%)")
    List<AideMenagere> rechercherAidesMenageres(@Param("recherche") String recherche);

    List<AideMenagere> findByAdresseContainingIgnoreCase(String adresse);
}
