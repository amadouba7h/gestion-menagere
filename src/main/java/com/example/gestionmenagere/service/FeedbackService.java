package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.Feedback;
import com.example.gestionmenagere.entity.Utilisateur;
import com.example.gestionmenagere.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Feedback> findByAuteur(Utilisateur auteur) {
        return feedbackRepository.findByAuteur(auteur);
    }

    @Transactional(readOnly = true)
    public List<Feedback> findByAuteurId(Long auteurId) {
        return feedbackRepository.findByAuteurId(auteurId);
    }

    @Transactional(readOnly = true)
    public List<Feedback> findByDestinataire(Utilisateur destinataire) {
        return feedbackRepository.findByDestinataire(destinataire);
    }

    @Transactional(readOnly = true)
    public List<Feedback> findByDestinataireId(Long destinataireId) {
        return feedbackRepository.findByDestinataireIdOrderByDateCreationDesc(destinataireId);
    }

    @Transactional(readOnly = true)
    public Double calculerNoteMoyenne(Long destinataireId) {
        Double moyenne = feedbackRepository.calculerNoteMoyenne(destinataireId);
        return moyenne != null ? Math.round(moyenne * 10.0) / 10.0 : null;
    }

    @Transactional(readOnly = true)
    public boolean aDejaEvalue(Utilisateur auteur, Utilisateur destinataire) {
        return feedbackRepository.existsByAuteurAndDestinataire(auteur, destinataire);
    }

    public Feedback creer(Utilisateur auteur, Utilisateur destinataire, Integer note, String commentaire) {
        if (auteur.getId().equals(destinataire.getId())) {
            throw new RuntimeException("Vous ne pouvez pas vous évaluer vous-même");
        }

        if (note < 1 || note > 5) {
            throw new RuntimeException("La note doit être comprise entre 1 et 5");
        }

        Feedback feedback = new Feedback();
        feedback.setAuteur(auteur);
        feedback.setDestinataire(destinataire);
        feedback.setNote(note);
        feedback.setCommentaire(commentaire);
        feedback.setDateCreation(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback mettreAJour(Long feedbackId, Integer note, String commentaire) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé"));

        if (note != null) {
            if (note < 1 || note > 5) {
                throw new RuntimeException("La note doit être comprise entre 1 et 5");
            }
            feedback.setNote(note);
        }
        if (commentaire != null) {
            feedback.setCommentaire(commentaire);
        }

        return feedbackRepository.save(feedback);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countByDestinataire(Utilisateur destinataire) {
        return feedbackRepository.countByDestinataire(destinataire);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return feedbackRepository.count();
    }

    public boolean isAuteur(Long feedbackId, Long utilisateurId) {
        return feedbackRepository.findById(feedbackId)
                .map(f -> f.getAuteur().getId().equals(utilisateurId))
                .orElse(false);
    }
}
