package com.example.gestionmenagere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employeur")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Employeur extends Utilisateur {

    @Column(name = "entreprise", length = 150)
    private String entreprise;

    @Column(name = "secteur_activite", length = 100)
    private String secteurActivite;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "employeur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OffreEmploi> offres = new ArrayList<>();
}
