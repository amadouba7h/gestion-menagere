package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.entity.enums.TypeContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {

    List<OffreEmploi> findByEmployeur(Employeur employeur);

    List<OffreEmploi> findByEmployeurId(Long employeurId);

    List<OffreEmploi> findByStatut(StatutOffre statut);

    List<OffreEmploi> findByStatutOrderByDatePublicationDesc(StatutOffre statut);

    List<OffreEmploi> findByTypeContrat(TypeContrat typeContrat);

    List<OffreEmploi> findByLieuContainingIgnoreCase(String lieu);

    List<OffreEmploi> findByDateExpirationBefore(LocalDate date);

    @Query("SELECT o FROM OffreEmploi o WHERE o.statut = :statut AND " +
            "(o.titre LIKE %:recherche% OR o.description LIKE %:recherche% OR o.lieu LIKE %:recherche%)")
    List<OffreEmploi> rechercherOffres(@Param("statut") StatutOffre statut, @Param("recherche") String recherche);

    @Query("SELECT o FROM OffreEmploi o WHERE o.statut = 'ACTIVE' ORDER BY o.datePublication DESC")
    List<OffreEmploi> findOffresActives();

    Long countByEmployeur(Employeur employeur);

    Long countByStatut(StatutOffre statut);
}
