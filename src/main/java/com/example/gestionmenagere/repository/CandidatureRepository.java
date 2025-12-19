package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Candidature;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.StatutCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByAideMenagere(AideMenagere aideMenagere);

    List<Candidature> findByAideMenagereId(Long aideMenagereId);

    List<Candidature> findByOffreEmploi(OffreEmploi offreEmploi);

    List<Candidature> findByOffreEmploiId(Long offreEmploiId);

    List<Candidature> findByStatut(StatutCandidature statut);

    Optional<Candidature> findByOffreEmploiAndAideMenagere(OffreEmploi offreEmploi, AideMenagere aideMenagere);

    boolean existsByOffreEmploiAndAideMenagere(OffreEmploi offreEmploi, AideMenagere aideMenagere);

    @Query("SELECT c FROM Candidature c WHERE c.offreEmploi.employeur.id = :employeurId")
    List<Candidature> findByEmployeurId(@Param("employeurId") Long employeurId);

    Long countByOffreEmploi(OffreEmploi offreEmploi);

    Long countByAideMenagereAndStatut(AideMenagere aideMenagere, StatutCandidature statut);
}
