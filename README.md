# Gestion Ménagère Mali

Application web de gestion et de recrutement des aides ménagères au Mali.

> **Projet académique** - Mémoire de fin d'études (Master)

## 📋 Description

Cette application permet de mettre en relation les employeurs et les aides ménagères au Mali. Elle offre les fonctionnalités suivantes :

- **Employeurs** : Publication d'offres d'emploi, gestion des candidatures, évaluation des aides ménagères
- **Aides ménagères** : Création de profil, recherche et candidature aux offres, suivi des candidatures
- **Administrateur** : Gestion des utilisateurs, modération, statistiques globales

## 🛠️ Stack Technique

- **Backend** : Spring Boot 4.x
- **Frontend** : Thymeleaf + Bootstrap 5
- **Base de données** : MySQL 8.x
- **Sécurité** : Spring Security
- **ORM** : Spring Data JPA / Hibernate

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

| Outil | Version minimale | Vérification |
|-------|------------------|--------------|
| Java JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| MySQL | 8.0+ | `mysql --version` |
| Git | 2.x | `git --version` |

## 🚀 Installation et Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/amadouba7h/gestion-menagere.git
cd gestion-menagere
```

### 2. Configurer la base de données MySQL

Connectez-vous à MySQL et créez la base de données :

```sql
CREATE DATABASE gestion_menagere_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> **Note** : La base de données sera créée automatiquement si elle n'existe pas, grâce à l'option `createDatabaseIfNotExist=true` dans la configuration.

### 3. Configurer les paramètres de connexion

Modifiez le fichier `src/main/resources/application.properties` selon votre environnement :

```properties
# Configuration MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_menagere_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

### 4. Lancer l'application

#### Option A : Avec Maven Wrapper (recommandé)

```bash
# Sur macOS/Linux
./mvnw spring-boot:run

# Sur Windows
mvnw.cmd spring-boot:run
```

#### Option B : Avec Maven installé

```bash
mvn spring-boot:run
```

#### Option C : Générer et exécuter le JAR

```bash
# Compiler le projet
./mvnw clean package -DskipTests

# Lancer l'application
java -jar target/gestionmenagere-0.0.1-SNAPSHOT.jar
```

### 5. Accéder à l'application

Ouvrez votre navigateur et accédez à :

```text
http://localhost:8080
```

## 👤 Comptes par défaut

Au premier lancement, un compte administrateur est créé automatiquement :

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Admin | `admin@gestionmenagere.ml` | `admin123` |

> ⚠️ **Important** : Changez le mot de passe administrateur en production !

## 📁 Structure du Projet

```text
gestionmenagere/
├── src/
│   ├── main/
│   │   ├── java/com/example/gestionmenagere/
│   │   │   ├── config/          # Configuration (Security, DataInitializer)
│   │   │   ├── controller/      # Contrôleurs MVC
│   │   │   ├── entity/          # Entités JPA
│   │   │   ├── repository/      # Repositories Spring Data
│   │   │   ├── security/        # Classes de sécurité
│   │   │   └── service/         # Services métier
│   │   └── resources/
│   │       ├── static/          # CSS, JS, images
│   │       ├── templates/       # Templates Thymeleaf
│   │       └── application.properties
│   └── test/                    # Tests unitaires
├── docs/                        # Documentation
├── pom.xml                      # Dépendances Maven
└── README.md
```

## 🔐 Rôles et Permissions

| Rôle | Accès |
|------|-------|
| `ROLE_ADMIN` | Tableau de bord admin, gestion des utilisateurs, statistiques |
| `ROLE_EMPLOYEUR` | Publication d'offres, gestion des candidatures |
| `ROLE_AIDE_MENAGERE` | Recherche d'offres, candidatures, gestion du profil |

## 🐛 Dépannage

### Erreur de connexion à MySQL

```text
Communications link failure
```

**Solution** : Vérifiez que MySQL est démarré et accessible sur le port 3306.

```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql

# Windows
net start mysql
```

### Erreur de permission MySQL

```text
Access denied for user 'root'@'localhost'
```

**Solution** : Vérifiez le mot de passe dans `application.properties`.

### Port 8080 déjà utilisé

```text
Web server failed to start. Port 8080 was already in use.
```

**Solution** : Arrêtez le processus utilisant le port ou changez le port dans `application.properties` :

```properties
server.port=8081
```

## 📝 Licence

Projet académique - Tous droits réservés.

## 👨‍💻 Auteur

Développé dans le cadre d'un mémoire de Master.
