package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.AideMenagere;
import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.entity.enums.Disponibilite;
import com.example.gestionmenagere.repository.AideMenagereRepository;
import com.example.gestionmenagere.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AideMenagereService {

    private final AideMenagereRepository aideMenagereRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AideMenagereService(AideMenagereRepository aideMenagereRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.aideMenagereRepository = aideMenagereRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<AideMenagere> findById(Long id) {
        return aideMenagereRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AideMenagere> findByEmail(String email) {
        return aideMenagereRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> findAll() {
        return aideMenagereRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> findActives() {
        return aideMenagereRepository.findByActifTrue();
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> findByDisponibilite(Disponibilite disponibilite) {
        return aideMenagereRepository.findByDisponibilite(disponibilite);
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> findByExperienceMinimum(Integer annees) {
        return aideMenagereRepository.findByExperienceAnneesGreaterThanEqual(annees);
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> rechercherParCompetence(String competence) {
        return aideMenagereRepository.findByCompetenceContaining(competence);
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> rechercher(String terme) {
        return aideMenagereRepository.rechercherAidesMenageres(terme);
    }

    @Transactional(readOnly = true)
    public List<AideMenagere> rechercherParLocalisation(String adresse) {
        return aideMenagereRepository.findByAdresseContainingIgnoreCase(adresse);
    }

    public AideMenagere inscrire(AideMenagere aideMenagere) {
        Role roleAide = roleRepository.findByNom(Role.ROLE_AIDE_MENAGERE)
                .orElseThrow(() -> new RuntimeException("Rôle AIDE_MENAGERE non trouvé"));

        aideMenagere.setRole(roleAide);
        aideMenagere.setMotDePasse(passwordEncoder.encode(aideMenagere.getMotDePasse()));
        aideMenagere.setDateInscription(LocalDateTime.now());
        aideMenagere.setActif(true);

        return aideMenagereRepository.save(aideMenagere);
    }

    public AideMenagere save(AideMenagere aideMenagere) {
        return aideMenagereRepository.save(aideMenagere);
    }

    public AideMenagere mettreAJourProfil(Long aideId, Disponibilite disponibilite,
            Integer experienceAnnees, String competences,
            String description, String telephone, String adresse) {
        AideMenagere aide = aideMenagereRepository.findById(aideId)
                .orElseThrow(() -> new RuntimeException("Aide ménagère non trouvée"));

        if (disponibilite != null)
            aide.setDisponibilite(disponibilite);
        if (experienceAnnees != null)
            aide.setExperienceAnnees(experienceAnnees);
        if (competences != null)
            aide.setCompetences(competences);
        if (description != null)
            aide.setDescription(description);
        if (telephone != null)
            aide.setTelephone(telephone);
        if (adresse != null)
            aide.setAdresse(adresse);

        return aideMenagereRepository.save(aide);
    }

    public void mettreAJourPhoto(Long aideId, String photoPath) {
        aideMenagereRepository.findById(aideId).ifPresent(aide -> {
            aide.setPhoto(photoPath);
            aideMenagereRepository.save(aide);
        });
    }

    public void deleteById(Long id) {
        aideMenagereRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return aideMenagereRepository.count();
    }

    @Transactional(readOnly = true)
    public long countActives() {
        return aideMenagereRepository.findByActifTrue().size();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return aideMenagereRepository.findByEmail(email).isPresent();
    }
}
