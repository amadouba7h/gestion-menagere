package com.example.gestionmenagere.entity.enums;

public enum StatutOffre {
    ACTIVE("Offre active"),
    POURVUE("Poste pourvu"),
    EXPIREE("Offre expirée"),
    ANNULEE("Offre annulée");

    private final String libelle;

    StatutOffre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
