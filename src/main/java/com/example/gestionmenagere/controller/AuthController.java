package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.service.AideMenagereService;
import com.example.gestionmenagere.service.EmployeurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final EmployeurService employeurService;
    private final AideMenagereService aideMenagereService;

    public AuthController(EmployeurService employeurService, AideMenagereService aideMenagereService) {
        this.employeurService = employeurService;
        this.aideMenagereService = aideMenagereService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("employeur", new Employeur());
        model.addAttribute("aideMenagere", new AideMenagere());
        return "auth/register";
    }

    @PostMapping("/register/employeur")
    public String registerEmployeur(@ModelAttribute Employeur employeur,
            RedirectAttributes redirectAttributes) {
        try {
            employeurService.inscrire(employeur);
            redirectAttributes.addFlashAttribute("success",
                    "Votre compte employeur a été créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/register/aide")
    public String registerAideMenagere(@ModelAttribute AideMenagere aideMenagere,
            RedirectAttributes redirectAttributes) {
        try {
            aideMenagereService.inscrire(aideMenagere);
            redirectAttributes.addFlashAttribute("success",
                    "Votre compte aide ménagère a été créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
