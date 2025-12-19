package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Candidature;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.StatutCandidature;
import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.repository.CandidatureRepository;
import com.example.gestionmenagere.repository.OffreEmploiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final OffreEmploiRepository offreEmploiRepository;

    public CandidatureService(CandidatureRepository candidatureRepository,
            OffreEmploiRepository offreEmploiRepository) {
        this.candidatureRepository = candidatureRepository;
        this.offreEmploiRepository = offreEmploiRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Candidature> findById(Long id) {
        return candidatureRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findAll() {
        return candidatureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByAideMenagere(AideMenagere aideMenagere) {
        return candidatureRepository.findByAideMenagere(aideMenagere);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByAideMenagereId(Long aideMenagereId) {
        return candidatureRepository.findByAideMenagereId(aideMenagereId);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByOffreEmploi(OffreEmploi offreEmploi) {
        return candidatureRepository.findByOffreEmploi(offreEmploi);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByOffreEmploiId(Long offreEmploiId) {
        return candidatureRepository.findByOffreEmploiId(offreEmploiId);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByEmployeurId(Long employeurId) {
        return candidatureRepository.findByEmployeurId(employeurId);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByStatut(StatutCandidature statut) {
        return candidatureRepository.findByStatut(statut);
    }

    @Transactional(readOnly = true)
    public boolean aDejaPostule(OffreEmploi offre, AideMenagere aide) {
        return candidatureRepository.existsByOffreEmploiAndAideMenagere(offre, aide);
    }

    public Candidature postuler(OffreEmploi offre, AideMenagere aide, String message) {
        if (offre.getStatut() != StatutOffre.ACTIVE) {
            throw new RuntimeException("Cette offre n'est plus active");
        }

        if (aDejaPostule(offre, aide)) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre");
        }

        Candidature candidature = new Candidature();
        candidature.setOffreEmploi(offre);
        candidature.setAideMenagere(aide);
        candidature.setMessage(message);
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        return candidatureRepository.save(candidature);
    }

    public Candidature save(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    public void changerStatut(Long candidatureId, StatutCandidature nouveauStatut) {
        candidatureRepository.findById(candidatureId).ifPresent(candidature -> {
            candidature.setStatut(nouveauStatut);
            candidatureRepository.save(candidature);
        });
    }

    public void accepter(Long candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        candidature.setStatut(StatutCandidature.ACCEPTEE);
        candidatureRepository.save(candidature);

        OffreEmploi offre = candidature.getOffreEmploi();
        offre.setStatut(StatutOffre.POURVUE);
        offreEmploiRepository.save(offre);

        candidatureRepository.findByOffreEmploi(offre).stream()
                .filter(c -> !c.getId().equals(candidatureId))
                .filter(c -> c.getStatut() == StatutCandidature.EN_ATTENTE)
                .forEach(c -> {
                    c.setStatut(StatutCandidature.REFUSEE);
                    candidatureRepository.save(c);
                });
    }

    public void refuser(Long candidatureId) {
        changerStatut(candidatureId, StatutCandidature.REFUSEE);
    }

    public void retirer(Long candidatureId) {
        changerStatut(candidatureId, StatutCandidature.RETIREE);
    }

    public void deleteById(Long id) {
        candidatureRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countByOffre(OffreEmploi offre) {
        return candidatureRepository.countByOffreEmploi(offre);
    }

    @Transactional(readOnly = true)
    public long countAccepteesParAide(AideMenagere aide) {
        return candidatureRepository.countByAideMenagereAndStatut(aide, StatutCandidature.ACCEPTEE);
    }

    public boolean isProprietaireCandidature(Long candidatureId, Long aideId) {
        return candidatureRepository.findById(candidatureId)
                .map(c -> c.getAideMenagere().getId().equals(aideId))
                .orElse(false);
    }

    public boolean isProprietaireOffre(Long candidatureId, Long employeurId) {
        return candidatureRepository.findById(candidatureId)
                .map(c -> c.getOffreEmploi().getEmployeur().getId().equals(employeurId))
                .orElse(false);
    }
}
