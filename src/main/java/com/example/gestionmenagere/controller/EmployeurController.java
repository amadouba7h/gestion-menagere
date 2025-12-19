package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.security.CustomUserDetails;
import com.example.gestionmenagere.service.CandidatureService;
import com.example.gestionmenagere.service.EmployeurService;
import com.example.gestionmenagere.service.OffreEmploiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employeur")
public class EmployeurController {

    private final EmployeurService employeurService;
    private final OffreEmploiService offreEmploiService;
    private final CandidatureService candidatureService;

    public EmployeurController(EmployeurService employeurService,
            OffreEmploiService offreEmploiService,
            CandidatureService candidatureService) {
        this.employeurService = employeurService;
        this.offreEmploiService = offreEmploiService;
        this.candidatureService = candidatureService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

        List<OffreEmploi> offres = offreEmploiService.findByEmployeur(employeur);

        model.addAttribute("employeur", employeur);
        model.addAttribute("offres", offres.stream().limit(5).toList());
        model.addAttribute("nombreOffresActives", offres.stream()
                .filter(o -> o.getStatut() == StatutOffre.ACTIVE).count());
        model.addAttribute("nombrePostesPourvus", offres.stream()
                .filter(o -> o.getStatut() == StatutOffre.POURVUE).count());
        model.addAttribute("candidatures",
                candidatureService.findByEmployeurId(employeur.getId()).stream().limit(5).toList());
        model.addAttribute("nombreCandidatures", candidatureService.findByEmployeurId(employeur.getId()).size());

        return "employeur/dashboard";
    }

    @GetMapping("/offres")
    public String mesOffres(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

        model.addAttribute("offres", offreEmploiService.findByEmployeur(employeur));
        return "employeur/offres";
    }

    @GetMapping("/offres/creer")
    public String creerOffreForm(Model model) {
        model.addAttribute("offre", new OffreEmploi());
        return "offre/form";
    }

    @PostMapping("/offres/creer")
    public String creerOffre(@AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute OffreEmploi offre,
            RedirectAttributes redirectAttributes) {
        try {
            Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

            offreEmploiService.creer(employeur, offre.getTitre(), offre.getDescription(),
                    offre.getTypeContrat(), offre.getSalaire(), offre.getLieu(), offre.getDateExpiration());
            redirectAttributes.addFlashAttribute("success", "Offre publiée avec succès");
            return "redirect:/employeur/offres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employeur/offres/creer";
        }
    }

    @GetMapping("/offres/{id}/modifier")
    public String modifierOffreForm(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        OffreEmploi offre = offreEmploiService.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

        if (!offre.getEmployeur().getId().equals(employeur.getId())) {
            throw new RuntimeException("Accès non autorisé");
        }

        model.addAttribute("offre", offre);
        return "offre/form";
    }

    @PostMapping("/offres/{id}/modifier")
    public String modifierOffre(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute OffreEmploi offreModifiee,
            RedirectAttributes redirectAttributes) {
        try {
            OffreEmploi offre = offreEmploiService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

            Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

            if (!offre.getEmployeur().getId().equals(employeur.getId())) {
                throw new RuntimeException("Accès non autorisé");
            }

            offre.setTitre(offreModifiee.getTitre());
            offre.setDescription(offreModifiee.getDescription());
            offre.setLieu(offreModifiee.getLieu());
            offre.setSalaire(offreModifiee.getSalaire());
            offre.setTypeContrat(offreModifiee.getTypeContrat());
            offre.setDateExpiration(offreModifiee.getDateExpiration());

            offreEmploiService.mettreAJour(id, offre.getTitre(), offre.getDescription(),
                    offre.getTypeContrat(), offre.getSalaire(), offre.getLieu(), offre.getDateExpiration());
            redirectAttributes.addFlashAttribute("success", "Offre modifiée avec succès");
            return "redirect:/employeur/offres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employeur/offres/" + id + "/modifier";
        }
    }

    @PostMapping("/offres/{id}/annuler")
    public String annulerOffre(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            OffreEmploi offre = offreEmploiService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

            Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

            if (!offre.getEmployeur().getId().equals(employeur.getId())) {
                throw new RuntimeException("Accès non autorisé");
            }

            offreEmploiService.annuler(id);
            redirectAttributes.addFlashAttribute("success", "Offre annulée");
            return "redirect:/employeur/offres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employeur/offres";
        }
    }

    @GetMapping("/candidatures")
    public String mesCandidatures(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Employeur employeur = employeurService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

        model.addAttribute("candidatures", candidatureService.findByEmployeurId(employeur.getId()));
        return "employeur/candidatures";
    }

    @PostMapping("/candidatures/{id}/accepter")
    public String accepterCandidature(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            employeurService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

            candidatureService.accepter(id);
            redirectAttributes.addFlashAttribute("success", "Candidature acceptée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employeur/candidatures";
    }

    @PostMapping("/candidatures/{id}/refuser")
    public String refuserCandidature(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            employeurService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

            candidatureService.refuser(id);
            redirectAttributes.addFlashAttribute("success", "Candidature refusée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employeur/candidatures";
    }
}
