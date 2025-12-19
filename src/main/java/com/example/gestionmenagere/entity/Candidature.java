package com.example.gestionmenagere.entity;

import com.example.gestionmenagere.entity.enums.StatutCandidature;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidature", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "offre_id", "aide_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_candidature", nullable = false)
    private LocalDateTime dateCandidature;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offre_id", nullable = false)
    private OffreEmploi offreEmploi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aide_id", nullable = false)
    private AideMenagere aideMenagere;

    @PrePersist
    protected void onCreate() {
        if (dateCandidature == null) {
            dateCandidature = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutCandidature.EN_ATTENTE;
        }
    }
}
