package com.example.gestionmenagere.service;

import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.entity.Utilisateur;
import com.example.gestionmenagere.repository.RoleRepository;
import com.example.gestionmenagere.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findByActif(Boolean actif) {
        return utilisateurRepository.findByActif(actif);
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findByRole(String roleName) {
        Optional<Role> role = roleRepository.findByNom(roleName);
        return role.map(utilisateurRepository::findByRole).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        if (utilisateur.getId() == null && utilisateur.getMotDePasse() != null) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    public void updatePassword(Long utilisateurId, String newPassword) {
        utilisateurRepository.findById(utilisateurId).ifPresent(utilisateur -> {
            utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
            utilisateurRepository.save(utilisateur);
        });
    }

    public boolean checkPassword(Utilisateur utilisateur, String rawPassword) {
        return passwordEncoder.matches(rawPassword, utilisateur.getMotDePasse());
    }

    public void activerCompte(Long utilisateurId) {
        utilisateurRepository.findById(utilisateurId).ifPresent(utilisateur -> {
            utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        });
    }

    public void desactiverCompte(Long utilisateurId) {
        utilisateurRepository.findById(utilisateurId).ifPresent(utilisateur -> {
            utilisateur.setActif(false);
            utilisateurRepository.save(utilisateur);
        });
    }

    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> rechercher(String terme) {
        return utilisateurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(terme, terme);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return utilisateurRepository.count();
    }

    @Transactional(readOnly = true)
    public long countActifs() {
        return utilisateurRepository.findByActif(true).size();
    }
}
