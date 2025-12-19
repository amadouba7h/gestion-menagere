package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Utilisateur> findByActif(Boolean actif);

    List<Utilisateur> findByRole(Role role);

    List<Utilisateur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
