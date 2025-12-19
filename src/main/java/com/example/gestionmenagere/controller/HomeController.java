package com.example.gestionmenagere.controller;

import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.repository.AideMenagereRepository;
import com.example.gestionmenagere.repository.EmployeurRepository;
import com.example.gestionmenagere.repository.OffreEmploiRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AideMenagereRepository aideMenagereRepository;
    private final EmployeurRepository employeurRepository;
    private final OffreEmploiRepository offreEmploiRepository;

    public HomeController(AideMenagereRepository aideMenagereRepository,
            EmployeurRepository employeurRepository,
            OffreEmploiRepository offreEmploiRepository) {
        this.aideMenagereRepository = aideMenagereRepository;
        this.employeurRepository = employeurRepository;
        this.offreEmploiRepository = offreEmploiRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nombreAides", aideMenagereRepository.count());
        model.addAttribute("nombreEmployeurs", employeurRepository.count());
        model.addAttribute("nombreOffres", offreEmploiRepository.countByStatut(StatutOffre.ACTIVE));
        return "home/index";
    }

    @GetMapping("/about")
    public String about() {
        return "home/about";
    }
}
