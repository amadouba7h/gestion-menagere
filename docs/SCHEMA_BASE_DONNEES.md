# Schéma de la Base de Données

## Application de Gestion des Aides Ménagères au Mali

---

## 📊 Diagramme Entité-Relation (Conceptuel)

```
┌─────────────────┐       ┌─────────────────┐
│      ROLE       │       │   UTILISATEUR   │
├─────────────────┤       ├─────────────────┤
│ id (PK)         │◄──────│ id (PK)         │
│ nom             │       │ email           │
└─────────────────┘       │ mot_de_passe    │
                          │ nom             │
                          │ prenom          │
                          │ telephone       │
                          │ adresse         │
                          │ date_inscription│
                          │ actif           │
                          │ role_id (FK)    │
                          └────────┬────────┘
                                   │
                    ┌──────────────┴──────────────┐
                    │                             │
                    ▼                             ▼
          ┌─────────────────┐           ┌─────────────────┐
          │    EMPLOYEUR    │           │  AIDE_MENAGERE  │
          ├─────────────────┤           ├─────────────────┤
          │ id (PK, FK)     │           │ id (PK, FK)     │
          │ entreprise      │           │ date_naissance  │
          │ secteur_activite│           │ experience_annees│
          │ description     │           │ disponibilite   │
          └────────┬────────┘           │ competences     │
                   │                    │ photo           │
                   │                    │ description     │
                   │                    └────────┬────────┘
                   │                             │
                   ▼                             │
          ┌─────────────────┐                    │
          │  OFFRE_EMPLOI   │                    │
          ├─────────────────┤                    │
          │ id (PK)         │                    │
          │ titre           │                    │
          │ description     │                    │
          │ type_contrat    │                    │
          │ salaire         │                    │
          │ lieu            │                    │
          │ date_publication│                    │
          │ date_expiration │                    │
          │ statut          │                    │
          │ employeur_id(FK)│                    │
          └────────┬────────┘                    │
                   │                             │
                   └──────────────┬──────────────┘
                                  │
                                  ▼
                        ┌─────────────────┐
                        │   CANDIDATURE   │
                        ├─────────────────┤
                        │ id (PK)         │
                        │ date_candidature│
                        │ statut          │
                        │ message         │
                        │ offre_id (FK)   │
                        │ aide_id (FK)    │
                        └─────────────────┘

          ┌─────────────────┐
          │    FEEDBACK     │
          ├─────────────────┤
          │ id (PK)         │
          │ note            │
          │ commentaire     │
          │ date_creation   │
          │ auteur_id (FK)  │
          │ destinataire_id │
          └─────────────────┘
```

---

## 📋 Description des Entités

### 1. ROLE
Définit les rôles de sécurité du système.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| nom | VARCHAR(50) | NOT NULL, UNIQUE | Nom du rôle (ROLE_ADMIN, ROLE_EMPLOYEUR, ROLE_AIDE_MENAGERE) |

---

### 2. UTILISATEUR
Classe de base pour tous les utilisateurs du système.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Adresse email (login) |
| mot_de_passe | VARCHAR(255) | NOT NULL | Mot de passe hashé (BCrypt) |
| nom | VARCHAR(100) | NOT NULL | Nom de famille |
| prenom | VARCHAR(100) | NOT NULL | Prénom |
| telephone | VARCHAR(20) | NOT NULL | Numéro de téléphone |
| adresse | VARCHAR(255) | | Adresse postale |
| date_inscription | DATETIME | NOT NULL | Date de création du compte |
| actif | BOOLEAN | NOT NULL, DEFAULT TRUE | Compte actif/désactivé |
| role_id | BIGINT | FK → ROLE(id) | Rôle de l'utilisateur |

---

### 3. EMPLOYEUR
Utilisateur qui recherche des aides ménagères.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, FK → UTILISATEUR(id) | Identifiant (héritage) |
| entreprise | VARCHAR(150) | | Nom de l'entreprise (optionnel) |
| secteur_activite | VARCHAR(100) | | Secteur d'activité |
| description | TEXT | | Description du profil |

---

### 4. AIDE_MENAGERE
Utilisateur offrant ses services d'aide ménagère.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, FK → UTILISATEUR(id) | Identifiant (héritage) |
| date_naissance | DATE | NOT NULL | Date de naissance |
| experience_annees | INT | DEFAULT 0 | Années d'expérience |
| disponibilite | VARCHAR(50) | | Disponibilité (TEMPS_PLEIN, TEMPS_PARTIEL, WEEKEND) |
| competences | TEXT | | Liste des compétences |
| photo | VARCHAR(255) | | Chemin vers la photo de profil |
| description | TEXT | | Description personnelle |

---

### 5. OFFRE_EMPLOI
Offre d'emploi publiée par un employeur.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| titre | VARCHAR(200) | NOT NULL | Titre de l'offre |
| description | TEXT | NOT NULL | Description détaillée |
| type_contrat | VARCHAR(50) | NOT NULL | Type (CDI, CDD, TEMPS_PARTIEL, PONCTUEL) |
| salaire | DECIMAL(10,2) | | Salaire proposé |
| lieu | VARCHAR(150) | NOT NULL | Lieu de travail |
| date_publication | DATETIME | NOT NULL | Date de publication |
| date_expiration | DATE | | Date d'expiration |
| statut | VARCHAR(30) | NOT NULL | Statut (ACTIVE, POURVUE, EXPIREE, ANNULEE) |
| employeur_id | BIGINT | FK → EMPLOYEUR(id), NOT NULL | Employeur auteur |

---

### 6. CANDIDATURE
Candidature d'une aide ménagère à une offre d'emploi.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| date_candidature | DATETIME | NOT NULL | Date de candidature |
| statut | VARCHAR(30) | NOT NULL | Statut (EN_ATTENTE, ACCEPTEE, REFUSEE, RETIREE) |
| message | TEXT | | Message de motivation |
| offre_id | BIGINT | FK → OFFRE_EMPLOI(id), NOT NULL | Offre concernée |
| aide_id | BIGINT | FK → AIDE_MENAGERE(id), NOT NULL | Candidate |

**Contrainte unique** : (offre_id, aide_id) - Une aide ne peut candidater qu'une fois par offre.

---

### 7. FEEDBACK
Évaluation entre employeur et aide ménagère.

| Colonne | Type | Contraintes | Description |
|---------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| note | INT | NOT NULL, CHECK (1-5) | Note de 1 à 5 |
| commentaire | TEXT | | Commentaire détaillé |
| date_creation | DATETIME | NOT NULL | Date de création |
| auteur_id | BIGINT | FK → UTILISATEUR(id), NOT NULL | Auteur du feedback |
| destinataire_id | BIGINT | FK → UTILISATEUR(id), NOT NULL | Destinataire |

---

## 🔗 Relations

| Relation | Type | Description |
|----------|------|-------------|
| UTILISATEUR → ROLE | ManyToOne | Un utilisateur a un rôle |
| EMPLOYEUR → UTILISATEUR | OneToOne (héritage) | Héritage par table jointe |
| AIDE_MENAGERE → UTILISATEUR | OneToOne (héritage) | Héritage par table jointe |
| OFFRE_EMPLOI → EMPLOYEUR | ManyToOne | Un employeur publie plusieurs offres |
| CANDIDATURE → OFFRE_EMPLOI | ManyToOne | Une offre reçoit plusieurs candidatures |
| CANDIDATURE → AIDE_MENAGERE | ManyToOne | Une aide peut candidater à plusieurs offres |
| FEEDBACK → UTILISATEUR (auteur) | ManyToOne | Un utilisateur peut écrire plusieurs feedbacks |
| FEEDBACK → UTILISATEUR (destinataire) | ManyToOne | Un utilisateur peut recevoir plusieurs feedbacks |

---

## 📝 Valeurs Énumérées

### Rôles
- `ROLE_ADMIN` : Administrateur système
- `ROLE_EMPLOYEUR` : Employeur/Recruteur
- `ROLE_AIDE_MENAGERE` : Aide ménagère

### Types de Contrat
- `CDI` : Contrat à durée indéterminée
- `CDD` : Contrat à durée déterminée
- `TEMPS_PARTIEL` : Temps partiel
- `PONCTUEL` : Mission ponctuelle

### Disponibilité
- `TEMPS_PLEIN` : Disponible à temps plein
- `TEMPS_PARTIEL` : Disponible à temps partiel
- `WEEKEND` : Disponible le weekend uniquement
- `FLEXIBLE` : Horaires flexibles

### Statut Offre
- `ACTIVE` : Offre active
- `POURVUE` : Poste pourvu
- `EXPIREE` : Offre expirée
- `ANNULEE` : Offre annulée

### Statut Candidature
- `EN_ATTENTE` : En attente de réponse
- `ACCEPTEE` : Candidature acceptée
- `REFUSEE` : Candidature refusée
- `RETIREE` : Candidature retirée par l'aide

---

## 🗄️ Script SQL de Création (Référence)

```sql
-- Base de données
CREATE DATABASE IF NOT EXISTS gestion_menagere_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gestion_menagere_db;

-- Note : Les tables seront générées automatiquement par JPA/Hibernate
-- Ce script est fourni à titre de référence uniquement
```

---

*Document généré par l'Agent 1 - Project Architect Agent*
