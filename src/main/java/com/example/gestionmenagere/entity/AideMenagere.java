package com.example.gestionmenagere.entity;

import com.example.gestionmenagere.entity.enums.Disponibilite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aide_menagere")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AideMenagere extends Utilisateur {

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "experience_annees")
    private Integer experienceAnnees = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilite", length = 50)
    private Disponibilite disponibilite;

    @Column(name = "competences", columnDefinition = "TEXT")
    private String competences;

    @Column(name = "photo", length = 255)
    private String photo;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "aideMenagere", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
}
