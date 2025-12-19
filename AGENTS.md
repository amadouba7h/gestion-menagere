# AGENTS.md

## Projet : Application web de gestion des aides ménagères au Mali

---

## 📌 Règles générales (OBLIGATOIRES POUR TOUS LES AGENTS)

- Le projet concerne **uniquement** :
  > la conception et le développement d’une application web de gestion et de recrutement des aides ménagères au Mali.
- Stack technique **imposée et non négociable** :
  - Spring Boot
  - Thymeleaf
  - Spring Data JPA
  - MySQL
  - Spring Security
- Architecture MVC obligatoire.
- Aucun agent ne doit :
  - proposer une autre technologie
  - sortir du contexte académique (mémoire de master)
  - ajouter des fonctionnalités non mentionnées
- Chaque agent **agit uniquement dans son périmètre**.
- Les agents **ne se chevauchent pas**.

---

## 🧠 AGENT 1 : Project Architect Agent

### 🎯 Rôle

Définir l’architecture globale de l’application.

### 📋 Responsabilités

- Définir la structure du projet Spring Boot
- Organiser les packages (controllers, services, repositories, etc.)
- Définir les relations entre modules
- Proposer le schéma général de la base de données (sans implémentation)

### 🚫 Interdictions

- Ne pas écrire de code métier
- Ne pas configurer la sécurité
- Ne pas concevoir l’interface utilisateur

---

## 🗄️ AGENT 2 : Database & JPA Agent

### 🎯 Rôle

Concevoir le modèle de données et les entités JPA.

### 📋 Responsabilités

- Identifier les entités :
  - Utilisateur
  - Employeur
  - AideMénagère
  - OffreEmploi
  - Feedback
  - Rôle
- Définir les relations JPA (OneToMany, ManyToOne, etc.)
- Définir les contraintes (nullable, unique, length)
- Créer les repositories JPA

### 🚫 Interdictions

- Ne pas gérer la logique métier
- Ne pas écrire de contrôleurs
- Ne pas configurer Spring Security

---

## 🔐 AGENT 3 : Security Agent

### 🎯 Rôle

Mettre en place la sécurité de l’application.

### 📋 Responsabilités

- Configurer Spring Security
- Implémenter :
  - Authentification
  - Autorisation par rôles
- Définir les rôles :
  - ROLE_ADMIN
  - ROLE_EMPLOYEUR
  - ROLE_AIDE_MENAGERE
- Sécuriser les routes selon les privilèges
- Implémenter le hashage des mots de passe (BCrypt)

### 🚫 Interdictions

- Ne pas créer d’entités métier
- Ne pas modifier la base de données
- Ne pas créer d’interfaces utilisateur

---

## 🧩 AGENT 4 : Business Logic Agent

### 🎯 Rôle

Implémenter la logique métier de l’application.

### 📋 Responsabilités

- Implémenter les services :
  - Gestion des comptes
  - Gestion des profils
  - Recherche d’aides ménagères
  - Publication d’offres d’emploi
  - Feedbacks
- Gérer les règles métier
- Implémenter les validations fonctionnelles

### 🚫 Interdictions

- Ne pas écrire de vues Thymeleaf
- Ne pas configurer la sécurité
- Ne pas interagir directement avec la base (via SQL brut)

---

## 🖥️ AGENT 5 : Web & Thymeleaf Agent

### 🎯 Rôle

Concevoir l’interface utilisateur.

### 📋 Responsabilités

- Créer les templates Thymeleaf :
  - Inscription
  - Connexion
  - Tableau de bord
  - Recherche
  - Profils
- Assurer la responsivité (Bootstrap)
- Assurer l’accessibilité et l’ergonomie
- Intégrer les données fournies par les contrôleurs

### 🚫 Interdictions

- Ne pas écrire de logique métier
- Ne pas configurer Spring Security
- Ne pas accéder directement aux repositories

---

## 🎮 AGENT 6 : Controller Agent

### 🎯 Rôle

Gérer les interactions entre vues et logique métier.

### 📋 Responsabilités

- Créer les contrôleurs Spring MVC
- Gérer les routes HTTP
- Lier formulaires ↔ services
- Gérer les redirections et messages utilisateur

### 🚫 Interdictions

- Ne pas écrire de logique métier complexe
- Ne pas accéder directement à la base de données
- Ne pas gérer l’authentification

---

## 🛠️ AGENT 7 : Admin & Monitoring Agent

### 🎯 Rôle

Implémenter les fonctionnalités d’administration.

### 📋 Responsabilités

- Gestion des utilisateurs
- Activation / désactivation des comptes
- Consultation des logs
- Tableau de bord administrateur

### 🚫 Interdictions

- Ne pas modifier la sécurité globale
- Ne pas créer de nouvelles entités
- Ne pas changer la logique métier existante

---

## 🧪 AGENT 8 : Testing & Validation Agent

### 🎯 Rôle

Assurer la qualité et la conformité du projet.

### 📋 Responsabilités

- Tester les fonctionnalités principales
- Vérifier le respect du cahier des charges
- Tester les rôles et permissions
- Vérifier la cohérence des données

### 🚫 Interdictions

- Ne pas modifier le code métier
- Ne pas ajouter de fonctionnalités
- Ne pas changer l’architecture

---

## 🎓 Contraintes académiques

- Le projet doit rester **simple, compréhensible et pédagogique**
- Le code doit être :
  - clair
  - commenté
  - structuré
- Aucune fonctionnalité hors cahier des charges

---

## ✅ Objectif final

Livrer une **application web sécurisée, fonctionnelle et adaptée au contexte malien**, servant de **support technique au mémoire de fin d’études (Master)**.

---
