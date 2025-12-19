package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Candidature;
import com.example.gestionmenagere.entity.enums.StatutCandidature;
import com.example.gestionmenagere.security.CustomUserDetails;
import com.example.gestionmenagere.service.AideMenagereService;
import com.example.gestionmenagere.service.CandidatureService;
import com.example.gestionmenagere.service.FeedbackService;
import com.example.gestionmenagere.service.OffreEmploiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/aide")
public class AideMenagereController {

    private final AideMenagereService aideMenagereService;
    private final CandidatureService candidatureService;
    private final FeedbackService feedbackService;
    private final OffreEmploiService offreEmploiService;

    public AideMenagereController(AideMenagereService aideMenagereService,
            CandidatureService candidatureService,
            FeedbackService feedbackService,
            OffreEmploiService offreEmploiService) {
        this.aideMenagereService = aideMenagereService;
        this.candidatureService = candidatureService;
        this.feedbackService = feedbackService;
        this.offreEmploiService = offreEmploiService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

        List<Candidature> candidatures = candidatureService.findByAideMenagere(aide);

        model.addAttribute("aide", aide);
        model.addAttribute("candidatures", candidatures.stream().limit(5).toList());
        model.addAttribute("nombreCandidatures", candidatures.size());
        model.addAttribute("nombreEnAttente", candidatures.stream()
                .filter(c -> c.getStatut() == StatutCandidature.EN_ATTENTE).count());
        model.addAttribute("nombreAcceptees", candidatures.stream()
                .filter(c -> c.getStatut() == StatutCandidature.ACCEPTEE).count());
        model.addAttribute("feedbacks", feedbackService.findByDestinataire(aide).stream().limit(3).toList());
        model.addAttribute("noteMoyenne", feedbackService.calculerNoteMoyenne(aide.getId()));
        model.addAttribute("offresRecentes", offreEmploiService.findOffresActives().stream().limit(3).toList());

        return "aide/dashboard";
    }

    @GetMapping("/candidatures")
    public String mesCandidatures(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

        model.addAttribute("candidatures", candidatureService.findByAideMenagere(aide));
        return "aide/candidatures";
    }

    @PostMapping("/candidatures/{id}/retirer")
    public String retirerCandidature(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

            if (!candidatureService.isProprietaireCandidature(id, aide.getId())) {
                throw new RuntimeException("Accès non autorisé");
            }

            candidatureService.retirer(id);
            redirectAttributes.addFlashAttribute("success", "Candidature retirée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/aide/candidatures";
    }

    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

        model.addAttribute("aide", aide);
        model.addAttribute("feedbacks", feedbackService.findByDestinataire(aide));
        model.addAttribute("noteMoyenne", feedbackService.calculerNoteMoyenne(aide.getId()));
        return "aide/profil";
    }

    @PostMapping("/profil")
    public String mettreAJourProfil(@AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute AideMenagere aideModifiee,
            RedirectAttributes redirectAttributes) {
        try {
            AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

            aideMenagereService.mettreAJourProfil(
                    aide.getId(),
                    aideModifiee.getDisponibilite(),
                    aideModifiee.getExperienceAnnees(),
                    aideModifiee.getCompetences(),
                    aideModifiee.getDescription(),
                    aideModifiee.getTelephone(),
                    aideModifiee.getAdresse());

            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/aide/profil";
    }
}
