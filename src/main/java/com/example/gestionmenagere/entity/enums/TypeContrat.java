package com.example.gestionmenagere.entity.enums;

public enum TypeContrat {
    CDI("Contrat à durée indéterminée"),
    CDD("Contrat à durée déterminée"),
    TEMPS_PARTIEL("Temps partiel"),
    PONCTUEL("Mission ponctuelle");

    private final String libelle;

    TypeContrat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
