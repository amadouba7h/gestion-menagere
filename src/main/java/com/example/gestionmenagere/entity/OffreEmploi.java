package com.example.gestionmenagere.entity;

import com.example.gestionmenagere.entity.enums.StatutOffre;
import com.example.gestionmenagere.entity.enums.TypeContrat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offre_emploi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreEmploi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_contrat", nullable = false, length = 50)
    private TypeContrat typeContrat;

    @Column(name = "salaire", precision = 10, scale = 2)
    private BigDecimal salaire;

    @Column(name = "lieu", nullable = false, length = 150)
    private String lieu;

    @Column(name = "date_publication", nullable = false)
    private LocalDateTime datePublication;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutOffre statut = StatutOffre.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeur_id", nullable = false)
    private Employeur employeur;

    @OneToMany(mappedBy = "offreEmploi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (datePublication == null) {
            datePublication = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutOffre.ACTIVE;
        }
    }
}
