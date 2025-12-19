package com.example.gestionmenagere.config;

import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.entity.Utilisateur;
import com.example.gestionmenagere.repository.RoleRepository;
import com.example.gestionmenagere.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists(Role.ROLE_ADMIN);
        createRoleIfNotExists(Role.ROLE_EMPLOYEUR);
        createRoleIfNotExists(Role.ROLE_AIDE_MENAGERE);

        createAdminIfNotExists();
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByNom(roleName)) {
            Role role = new Role();
            role.setNom(roleName);
            roleRepository.save(role);
        }
    }

    private void createAdminIfNotExists() {
        String adminEmail = "admin@gestionmenagere.ml";
        if (!utilisateurRepository.existsByEmail(adminEmail)) {
            Role roleAdmin = roleRepository.findByNom(Role.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Rôle ADMIN non trouvé"));

            Utilisateur admin = new Utilisateur();
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setEmail(adminEmail);
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setTelephone("+223 00 00 00 00");
            admin.setRole(roleAdmin);
            admin.setActif(true);
            admin.setDateInscription(LocalDateTime.now());

            utilisateurRepository.save(admin);
            System.out.println("==> Compte admin créé: " + adminEmail + " / admin123");
        }
    }
}
