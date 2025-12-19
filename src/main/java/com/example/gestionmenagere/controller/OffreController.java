package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.TypeContrat;
import com.example.gestionmenagere.security.CustomUserDetails;
import com.example.gestionmenagere.service.AideMenagereService;
import com.example.gestionmenagere.service.CandidatureService;
import com.example.gestionmenagere.service.OffreEmploiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/offres")
public class OffreController {

    private final OffreEmploiService offreEmploiService;
    private final CandidatureService candidatureService;
    private final AideMenagereService aideMenagereService;

    public OffreController(OffreEmploiService offreEmploiService,
            CandidatureService candidatureService,
            AideMenagereService aideMenagereService) {
        this.offreEmploiService = offreEmploiService;
        this.candidatureService = candidatureService;
        this.aideMenagereService = aideMenagereService;
    }

    @GetMapping
    public String listeOffres(Model model) {
        model.addAttribute("offres", offreEmploiService.findOffresActives());
        return "offre/liste";
    }

    @GetMapping("/recherche")
    public String rechercherOffres(@RequestParam(required = false) String q,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String typeContrat,
            Model model) {
        List<OffreEmploi> offres;

        if (q != null && !q.isEmpty()) {
            offres = offreEmploiService.rechercher(q);
        } else if (lieu != null && !lieu.isEmpty()) {
            offres = offreEmploiService.findByLieu(lieu);
        } else if (typeContrat != null && !typeContrat.isEmpty()) {
            offres = offreEmploiService.findByTypeContrat(TypeContrat.valueOf(typeContrat));
        } else {
            offres = offreEmploiService.findOffresActives();
        }

        model.addAttribute("offres", offres);
        model.addAttribute("recherche", q);
        model.addAttribute("lieu", lieu);
        model.addAttribute("typeContrat", typeContrat);

        return "offre/liste";
    }

    @GetMapping("/{id}")
    public String detailOffre(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        OffreEmploi offre = offreEmploiService.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        model.addAttribute("offre", offre);

        if (userDetails != null) {
            aideMenagereService.findByEmail(userDetails.getUsername()).ifPresent(aide -> {
                model.addAttribute("aDejaPostule", candidatureService.aDejaPostule(offre, aide));
            });
        }

        return "offre/detail";
    }

    @GetMapping("/{id}/postuler")
    public String formulairePostuler(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        OffreEmploi offre = offreEmploiService.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

        if (candidatureService.aDejaPostule(offre, aide)) {
            return "redirect:/offres/" + id + "?dejaPostule=true";
        }

        model.addAttribute("offre", offre);
        return "offre/postuler";
    }

    @PostMapping("/{id}/postuler")
    public String postuler(@PathVariable Long id,
            @RequestParam(required = false) String message,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            OffreEmploi offre = offreEmploiService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

            AideMenagere aide = aideMenagereService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

            candidatureService.postuler(offre, aide, message);
            redirectAttributes.addFlashAttribute("success", "Votre candidature a été envoyée avec succès");
            return "redirect:/aide/candidatures";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/offres/" + id;
        }
    }
}
