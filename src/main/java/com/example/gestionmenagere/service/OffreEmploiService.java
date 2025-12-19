package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.entity.enums.TypeContrat;
import com.example.gestionmenagere.repository.OffreEmploiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OffreEmploiService {

    private final OffreEmploiRepository offreEmploiRepository;

    public OffreEmploiService(OffreEmploiRepository offreEmploiRepository) {
        this.offreEmploiRepository = offreEmploiRepository;
    }

    @Transactional(readOnly = true)
    public Optional<OffreEmploi> findById(Long id) {
        return offreEmploiRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findAll() {
        return offreEmploiRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findOffresActives() {
        return offreEmploiRepository.findOffresActives();
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findByStatut(StatutOffre statut) {
        return offreEmploiRepository.findByStatutOrderByDatePublicationDesc(statut);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findByEmployeur(Employeur employeur) {
        return offreEmploiRepository.findByEmployeur(employeur);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findByEmployeurId(Long employeurId) {
        return offreEmploiRepository.findByEmployeurId(employeurId);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findByTypeContrat(TypeContrat typeContrat) {
        return offreEmploiRepository.findByTypeContrat(typeContrat);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> findByLieu(String lieu) {
        return offreEmploiRepository.findByLieuContainingIgnoreCase(lieu);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploi> rechercher(String terme) {
        return offreEmploiRepository.rechercherOffres(StatutOffre.ACTIVE, terme);
    }

    public OffreEmploi creer(Employeur employeur, String titre, String description,
            TypeContrat typeContrat, BigDecimal salaire, String lieu,
            LocalDate dateExpiration) {
        OffreEmploi offre = new OffreEmploi();
        offre.setEmployeur(employeur);
        offre.setTitre(titre);
        offre.setDescription(description);
        offre.setTypeContrat(typeContrat);
        offre.setSalaire(salaire);
        offre.setLieu(lieu);
        offre.setDatePublication(LocalDateTime.now());
        offre.setDateExpiration(dateExpiration);
        offre.setStatut(StatutOffre.ACTIVE);

        return offreEmploiRepository.save(offre);
    }

    public OffreEmploi save(OffreEmploi offre) {
        return offreEmploiRepository.save(offre);
    }

    public OffreEmploi mettreAJour(Long offreId, String titre, String description,
            TypeContrat typeContrat, BigDecimal salaire,
            String lieu, LocalDate dateExpiration) {
        OffreEmploi offre = offreEmploiRepository.findById(offreId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        if (titre != null)
            offre.setTitre(titre);
        if (description != null)
            offre.setDescription(description);
        if (typeContrat != null)
            offre.setTypeContrat(typeContrat);
        if (salaire != null)
            offre.setSalaire(salaire);
        if (lieu != null)
            offre.setLieu(lieu);
        if (dateExpiration != null)
            offre.setDateExpiration(dateExpiration);

        return offreEmploiRepository.save(offre);
    }

    public void changerStatut(Long offreId, StatutOffre nouveauStatut) {
        offreEmploiRepository.findById(offreId).ifPresent(offre -> {
            offre.setStatut(nouveauStatut);
            offreEmploiRepository.save(offre);
        });
    }

    public void marquerCommePourvue(Long offreId) {
        changerStatut(offreId, StatutOffre.POURVUE);
    }

    public void annuler(Long offreId) {
        changerStatut(offreId, StatutOffre.ANNULEE);
    }

    public void verifierEtExpirerOffres() {
        List<OffreEmploi> offresExpirees = offreEmploiRepository
                .findByDateExpirationBefore(LocalDate.now());

        offresExpirees.stream()
                .filter(o -> o.getStatut() == StatutOffre.ACTIVE)
                .forEach(offre -> {
                    offre.setStatut(StatutOffre.EXPIREE);
                    offreEmploiRepository.save(offre);
                });
    }

    public void deleteById(Long id) {
        offreEmploiRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return offreEmploiRepository.count();
    }

    @Transactional(readOnly = true)
    public long countActives() {
        return offreEmploiRepository.countByStatut(StatutOffre.ACTIVE);
    }

    @Transactional(readOnly = true)
    public long countByEmployeur(Employeur employeur) {
        return offreEmploiRepository.countByEmployeur(employeur);
    }

    public boolean isProprietaire(Long offreId, Long employeurId) {
        return offreEmploiRepository.findById(offreId)
                .map(offre -> offre.getEmployeur().getId().equals(employeurId))
                .orElse(false);
    }
}
