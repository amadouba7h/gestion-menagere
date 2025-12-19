package com.example.gestionmenagere.config;

import com.example.gestionmenagere.entity.Role;
import com.example.gestionmenagere.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            String redirectUrl = "/";

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN))) {
                redirectUrl = "/admin/dashboard";
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Role.ROLE_EMPLOYEUR))) {
                redirectUrl = "/employeur/dashboard";
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Role.ROLE_AIDE_MENAGERE))) {
                redirectUrl = "/aide/dashboard";
            }

            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Routes publiques
                        .requestMatchers("/", "/home", "/about", "/contact").permitAll()
                        .requestMatchers("/login", "/register", "/register/**").permitAll()
                        .requestMatchers("/offres", "/offres/recherche").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Routes Admin
                        .requestMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN)

                        // Routes Employeur
                        .requestMatchers("/employeur/**").hasAuthority(Role.ROLE_EMPLOYEUR)

                        // Routes Aide Ménagère
                        .requestMatchers("/aide/**").hasAuthority(Role.ROLE_AIDE_MENAGERE)

                        // Routes nécessitant une authentification (tous les rôles)
                        .requestMatchers("/offres/*/postuler").hasAuthority(Role.ROLE_AIDE_MENAGERE)
                        .requestMatchers("/offres/creer", "/offres/*/modifier", "/offres/*/supprimer")
                        .hasAuthority(Role.ROLE_EMPLOYEUR)
                        .requestMatchers("/profil/**").authenticated()
                        .requestMatchers("/feedback/**").authenticated()

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("motDePasse")
                        .successHandler(customSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403"))
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true"));

        return http.build();
    }
}
