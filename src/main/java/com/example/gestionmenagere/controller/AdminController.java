package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.entity.OffreEmploi;
import com.example.gestionmenagere.entity.Utilisateur;
import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final EmployeurService employeurService;
    private final AideMenagereService aideMenagereService;
    private final OffreEmploiService offreEmploiService;
    private final CandidatureService candidatureService;
    private final FeedbackService feedbackService;

    public AdminController(UtilisateurService utilisateurService,
            EmployeurService employeurService,
            AideMenagereService aideMenagereService,
            OffreEmploiService offreEmploiService,
            CandidatureService candidatureService,
            FeedbackService feedbackService) {
        this.utilisateurService = utilisateurService;
        this.employeurService = employeurService;
        this.aideMenagereService = aideMenagereService;
        this.offreEmploiService = offreEmploiService;
        this.candidatureService = candidatureService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("nombreUtilisateurs", utilisateurService.countAll());
        model.addAttribute("nombreEmployeurs", employeurService.countAll());
        model.addAttribute("nombreAides", aideMenagereService.countAll());
        model.addAttribute("nombreOffres", offreEmploiService.countAll());
        model.addAttribute("nombreOffresActives", offreEmploiService.countActives());
        model.addAttribute("nombreCandidatures", candidatureService.findAll().size());
        model.addAttribute("nombreFeedbacks", feedbackService.countAll());

        model.addAttribute("derniersEmployeurs", employeurService.findAll().stream().limit(5).toList());
        model.addAttribute("dernieresAides", aideMenagereService.findAll().stream().limit(5).toList());
        model.addAttribute("dernieresOffres", offreEmploiService.findAll().stream().limit(5).toList());

        return "admin/dashboard";
    }

    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.findAll());
        return "admin/utilisateurs";
    }

    @GetMapping("/employeurs")
    public String listeEmployeurs(Model model) {
        model.addAttribute("employeurs", employeurService.findAll());
        return "admin/employeurs";
    }

    @GetMapping("/aides")
    public String listeAides(Model model) {
        model.addAttribute("aides", aideMenagereService.findAll());
        return "admin/aides";
    }

    @GetMapping("/offres")
    public String listeOffres(Model model) {
        model.addAttribute("offres", offreEmploiService.findAll());
        return "admin/offres";
    }

    @PostMapping("/utilisateurs/{id}/activer")
    public String activerUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.activerCompte(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur activé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/utilisateurs/{id}/desactiver")
    public String desactiverUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.desactiverCompte(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur désactivé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/employeurs/{id}/activer")
    public String activerEmployeur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Employeur employeur = employeurService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));
            employeur.setActif(true);
            employeurService.save(employeur);
            redirectAttributes.addFlashAttribute("success", "Employeur activé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/employeurs";
    }

    @PostMapping("/employeurs/{id}/desactiver")
    public String desactiverEmployeur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Employeur employeur = employeurService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));
            employeur.setActif(false);
            employeurService.save(employeur);
            redirectAttributes.addFlashAttribute("success", "Employeur désactivé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/employeurs";
    }

    @PostMapping("/aides/{id}/activer")
    public String activerAide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            AideMenagere aide = aideMenagereService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));
            aide.setActif(true);
            aideMenagereService.save(aide);
            redirectAttributes.addFlashAttribute("success", "Aide ménagère activée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/aides";
    }

    @PostMapping("/aides/{id}/desactiver")
    public String desactiverAide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            AideMenagere aide = aideMenagereService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));
            aide.setActif(false);
            aideMenagereService.save(aide);
            redirectAttributes.addFlashAttribute("success", "Aide ménagère désactivée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/aides";
    }

    @PostMapping("/offres/{id}/annuler")
    public String annulerOffre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            offreEmploiService.annuler(id);
            redirectAttributes.addFlashAttribute("success", "Offre annulée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/offres";
    }

    @PostMapping("/offres/{id}/reactiver")
    public String reactiverOffre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            offreEmploiService.changerStatut(id, StatutOffre.ACTIVE);
            redirectAttributes.addFlashAttribute("success", "Offre réactivée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/offres";
    }

    @GetMapping("/statistiques")
    public String statistiques(Model model) {
        model.addAttribute("totalUtilisateurs", utilisateurService.countAll());
        model.addAttribute("totalEmployeurs", employeurService.countAll());
        model.addAttribute("totalAides", aideMenagereService.countAll());
        model.addAttribute("aidesActives", aideMenagereService.countActives());
        model.addAttribute("totalOffres", offreEmploiService.countAll());
        model.addAttribute("offresActives", offreEmploiService.countActives());
        model.addAttribute("totalCandidatures", candidatureService.findAll().size());
        model.addAttribute("totalFeedbacks", feedbackService.countAll());

        return "admin/statistiques";
    }
}
