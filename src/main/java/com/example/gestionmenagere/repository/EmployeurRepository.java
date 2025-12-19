package com.example.gestionmenagere.repository;

import com.example.gestionmenagere.entity.Employeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeurRepository extends JpaRepository<Employeur, Long> {

    Optional<Employeur> findByEmail(String email);

    List<Employeur> findByActifTrue();

    List<Employeur> findByEntrepriseContainingIgnoreCase(String entreprise);

    List<Employeur> findBySecteurActiviteContainingIgnoreCase(String secteurActivite);
}
