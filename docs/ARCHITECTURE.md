# Architecture Technique

## Application de Gestion des Aides Ménagères au Mali

---

## 🏗️ Vue d'Ensemble

Cette application suit une **architecture MVC (Model-View-Controller)** avec Spring Boot, conforme aux bonnes pratiques de développement Java Enterprise.

---

## 📁 Structure des Packages

```text
com.example.gestionmenagere/
│
├── GestionmenagereApplication.java    # Point d'entrée de l'application
│
├── config/                            # Configuration Spring
│   ├── SecurityConfig.java            # Configuration Spring Security
│   └── WebMvcConfig.java              # Configuration MVC
│
├── controller/                        # Contrôleurs MVC
│   ├── HomeController.java            # Pages publiques
│   ├── AuthController.java            # Authentification
│   ├── EmployeurController.java       # Espace employeur
│   ├── AideMenagereController.java    # Espace aide ménagère
│   ├── OffreEmploiController.java     # Gestion des offres
│   ├── CandidatureController.java     # Gestion des candidatures
│   ├── FeedbackController.java        # Gestion des évaluations
│   └── AdminController.java           # Administration
│
├── dto/                               # Data Transfer Objects
│   ├── InscriptionDTO.java            # Formulaire d'inscription
│   ├── ProfilDTO.java                 # Données de profil
│   ├── OffreEmploiDTO.java            # Données d'offre
│   ├── RechercheDTO.java              # Critères de recherche
│   └── FeedbackDTO.java               # Données d'évaluation
│
├── entity/                            # Entités JPA
│   ├── Utilisateur.java               # Classe de base
│   ├── Employeur.java                 # Entité employeur
│   ├── AideMenagere.java              # Entité aide ménagère
│   ├── OffreEmploi.java               # Entité offre d'emploi
│   ├── Candidature.java               # Entité candidature
│   ├── Feedback.java                  # Entité feedback
│   └── Role.java                      # Entité rôle
│
├── exception/                         # Exceptions personnalisées
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   ├── ValidationException.java
│   └── DuplicateResourceException.java
│
├── repository/                        # Repositories JPA
│   ├── UtilisateurRepository.java
│   ├── EmployeurRepository.java
│   ├── AideMenagereRepository.java
│   ├── OffreEmploiRepository.java
│   ├── CandidatureRepository.java
│   ├── FeedbackRepository.java
│   └── RoleRepository.java
│
├── security/                          # Sécurité
│   ├── CustomUserDetails.java         # Implémentation UserDetails
│   └── CustomUserDetailsService.java  # Service de chargement
│
├── service/                           # Services métier
│   ├── UtilisateurService.java
│   ├── EmployeurService.java
│   ├── AideMenagereService.java
│   ├── OffreEmploiService.java
│   ├── CandidatureService.java
│   └── FeedbackService.java
│
└── util/                              # Utilitaires
    ├── DateUtils.java
    └── ValidationUtils.java
```

---

## 🔄 Flux de Données (Architecture MVC)

```text
┌─────────────────────────────────────────────────────────────────────┐
│                           NAVIGATEUR                                │
│                    (Requêtes HTTP / Réponses HTML)                  │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      SPRING SECURITY FILTER                         │
│              (Authentification / Autorisation)                      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         CONTROLLER                                  │
│         (Réception requêtes, validation, appel services)            │
│                                                                     │
│   @GetMapping, @PostMapping, @RequestMapping                        │
│   Retourne : nom de vue Thymeleaf + Model                           │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          SERVICE                                    │
│              (Logique métier, règles de gestion)                    │
│                                                                     │
│   @Service, @Transactional                                          │
│   Orchestration des opérations métier                               │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        REPOSITORY                                   │
│              (Accès aux données via JPA)                            │
│                                                                     │
│   @Repository, extends JpaRepository                                │
│   Requêtes CRUD et personnalisées                                   │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     BASE DE DONNÉES MySQL                           │
│                   (Persistance des données)                         │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🛡️ Architecture de Sécurité

### Rôles et Permissions

| Rôle | Permissions |
|------|-------------|
| `ROLE_ADMIN` | Accès complet, gestion des utilisateurs, consultation des logs |
| `ROLE_EMPLOYEUR` | Publier des offres, consulter les candidatures, évaluer les aides |
| `ROLE_AIDE_MENAGERE` | Consulter les offres, postuler, gérer son profil |

### Routes Sécurisées

```text
/                           → PUBLIC
/login, /register           → PUBLIC
/offres                     → PUBLIC (consultation)

/employeur/**               → ROLE_EMPLOYEUR
/aide/**                    → ROLE_AIDE_MENAGERE
/admin/**                   → ROLE_ADMIN

/api/**                     → Authentifié (tous rôles)
```

---

## 📂 Structure des Ressources

```text
src/main/resources/
│
├── application.properties         # Configuration principale
│
├── static/                        # Ressources statiques
│   ├── css/                       # Feuilles de style
│   │   └── style.css
│   ├── js/                        # Scripts JavaScript
│   │   └── main.js
│   └── images/                    # Images
│       └── logo.png
│
└── templates/                     # Templates Thymeleaf
    ├── layout/                    # Layouts réutilisables
    │   ├── base.html              # Template de base
    │   ├── header.html            # En-tête
    │   └── footer.html            # Pied de page
    │
    ├── auth/                      # Pages d'authentification
    │   ├── login.html
    │   └── register.html
    │
    ├── home/                      # Pages publiques
    │   ├── index.html
    │   └── about.html
    │
    ├── employeur/                 # Espace employeur
    │   ├── dashboard.html
    │   ├── profil.html
    │   └── mes-offres.html
    │
    ├── aide/                      # Espace aide ménagère
    │   ├── dashboard.html
    │   ├── profil.html
    │   └── mes-candidatures.html
    │
    ├── offre/                     # Pages des offres
    │   ├── liste.html
    │   ├── detail.html
    │   └── form.html
    │
    ├── admin/                     # Administration
    │   ├── dashboard.html
    │   └── utilisateurs.html
    │
    └── error/                     # Pages d'erreur
        ├── 403.html
        ├── 404.html
        └── 500.html
```

---

## ⚙️ Stack Technique

| Composant | Technologie | Version |
|-----------|-------------|---------|
| **Framework** | Spring Boot | 4.0.1 |
| **Langage** | Java | 17 |
| **Template Engine** | Thymeleaf | (inclus) |
| **ORM** | Spring Data JPA / Hibernate | (inclus) |
| **Sécurité** | Spring Security | (inclus) |
| **Base de données** | MySQL | 8.x |
| **Build Tool** | Maven | 3.x |
| **Utilitaires** | Lombok | (inclus) |

---

## 🔗 Relations entre Modules

```text
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ Controller  │────▶│   Service   │────▶│ Repository  │
└─────────────┘     └─────────────┘     └─────────────┘
       │                   │                   │
       │                   │                   │
       ▼                   ▼                   ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    DTO      │     │   Entity    │     │   Entity    │
└─────────────┘     └─────────────┘     └─────────────┘
       │
       ▼
┌─────────────┐
│  Thymeleaf  │
│   (View)    │
└─────────────┘
```

### Règles de Dépendance

- **Controller** → Service, DTO
- **Service** → Repository, Entity, DTO
- **Repository** → Entity
- **Entity** → Aucune dépendance applicative

---

## 📋 Conventions de Nommage

| Élément | Convention | Exemple |
|---------|------------|---------|
| Package | minuscules | `com.example.gestionmenagere.service` |
| Classe | PascalCase | `OffreEmploiService` |
| Interface | PascalCase | `UtilisateurRepository` |
| Méthode | camelCase | `findByEmail()` |
| Variable | camelCase | `dateInscription` |
| Constante | SCREAMING_SNAKE | `MAX_RESULTS` |
| Table BD | snake_case | `offre_emploi` |
| Colonne BD | snake_case | `date_publication` |

---

## 🚀 Prochaines Étapes

1. **Agent 2** : Implémenter les entités JPA
2. **Agent 3** : Configurer Spring Security
3. **Agent 4** : Implémenter les services métier
4. **Agent 5** : Créer les templates Thymeleaf
5. **Agent 6** : Créer les contrôleurs
6. **Agent 7** : Implémenter l'administration
7. **Agent 8** : Tests et validation

---

*Document généré par l'Agent 1 - Project Architect Agent*
