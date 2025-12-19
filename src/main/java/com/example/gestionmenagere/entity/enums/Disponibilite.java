package com.example.gestionmenagere.entity.enums;

public enum Disponibilite {
    TEMPS_PLEIN("Disponible à temps plein"),
    TEMPS_PARTIEL("Disponible à temps partiel"),
    WEEKEND("Disponible le weekend uniquement"),
    FLEXIBLE("Horaires flexibles");

    private final String libelle;

    Disponibilite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
