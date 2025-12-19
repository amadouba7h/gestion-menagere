package com.example.gestionmenagere.security;

import com.example.gestionmenagere.entity.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Utilisateur utilisateur;

    public CustomUserDetails(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(utilisateur.getRole().getNom()));
    }

    @Override
    public String getPassword() {
        return utilisateur.getMotDePasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return utilisateur.getActif();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return utilisateur.getActif();
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Long getId() {
        return utilisateur.getId();
    }

    public String getNom() {
        return utilisateur.getNom();
    }

    public String getPrenom() {
        return utilisateur.getPrenom();
    }

    public String getNomComplet() {
        return utilisateur.getPrenom() + " " + utilisateur.getNom();
    }
}
