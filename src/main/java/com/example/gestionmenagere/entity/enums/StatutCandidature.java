package com.example.gestionmenagere.entity.enums;

public enum StatutCandidature {
    EN_ATTENTE("En attente de réponse"),
    ACCEPTEE("Candidature acceptée"),
    REFUSEE("Candidature refusée"),
    RETIREE("Candidature retirée");

    private final String libelle;

    StatutCandidature(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
