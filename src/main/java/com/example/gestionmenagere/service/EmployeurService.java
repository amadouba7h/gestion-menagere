package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.Employeur;
import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.repository.EmployeurRepository;
import com.example.gestionmenagere.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeurService {

    private final EmployeurRepository employeurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeurService(EmployeurRepository employeurRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.employeurRepository = employeurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Employeur> findById(Long id) {
        return employeurRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Employeur> findByEmail(String email) {
        return employeurRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Employeur> findAll() {
        return employeurRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employeur> findActifs() {
        return employeurRepository.findByActifTrue();
    }

    @Transactional(readOnly = true)
    public List<Employeur> rechercherParEntreprise(String entreprise) {
        return employeurRepository.findByEntrepriseContainingIgnoreCase(entreprise);
    }

    @Transactional(readOnly = true)
    public List<Employeur> rechercherParSecteur(String secteur) {
        return employeurRepository.findBySecteurActiviteContainingIgnoreCase(secteur);
    }

    public Employeur inscrire(Employeur employeur) {
        Role roleEmployeur = roleRepository.findByNom(Role.ROLE_EMPLOYEUR)
                .orElseThrow(() -> new RuntimeException("Rôle EMPLOYEUR non trouvé"));

        employeur.setRole(roleEmployeur);
        employeur.setMotDePasse(passwordEncoder.encode(employeur.getMotDePasse()));
        employeur.setDateInscription(LocalDateTime.now());
        employeur.setActif(true);

        return employeurRepository.save(employeur);
    }

    public Employeur save(Employeur employeur) {
        return employeurRepository.save(employeur);
    }

    public Employeur mettreAJourProfil(Long employeurId, String entreprise,
            String secteurActivite, String description,
            String telephone, String adresse) {
        Employeur employeur = employeurRepository.findById(employeurId)
                .orElseThrow(() -> new RuntimeException("Employeur non trouvé"));

        if (entreprise != null)
            employeur.setEntreprise(entreprise);
        if (secteurActivite != null)
            employeur.setSecteurActivite(secteurActivite);
        if (description != null)
            employeur.setDescription(description);
        if (telephone != null)
            employeur.setTelephone(telephone);
        if (adresse != null)
            employeur.setAdresse(adresse);

        return employeurRepository.save(employeur);
    }

    public void deleteById(Long id) {
        employeurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return employeurRepository.count();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeurRepository.findByEmail(email).isPresent();
    }
}
