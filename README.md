# Expense Tracker API

API REST de gestion de dépenses personnelles, développée avec Java et Spring Boot. Permet de suivre ses dépenses par catégorie, de définir des budgets mensuels, et d'être alerté automatiquement (via RabbitMQ) en cas de dépassement.

> Enrichi avec authentification JWT, budgets, statistiques, alertes asynchrones, tests automatisés et conteneurisation complète.

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Stack technique](#stack-technique)
- [Architecture](#architecture)
- [Lancer le projet](#lancer-le-projet)
- [Tester l'API](#tester-lapi)
- [Lancer les tests](#lancer-les-tests)
- [Variables d'environnement](#variables-denvironnement)
- [Points techniques notables](#points-techniques-notables)

## Fonctionnalités

- Authentification par JWT (inscription, connexion)
- CRUD des catégories de dépenses
- CRUD des dépenses avec pagination et filtres (date, catégorie, montant)
- CRUD des budgets mensuels par catégorie
- Statistiques mensuelles (total par catégorie, comparaison au budget)
- Alertes asynchrones de dépassement de budget via RabbitMQ
- Isolation stricte des données entre utilisateurs
- Documentation interactive via Swagger / OpenAPI

## Stack technique

| Catégorie | Technologies |
|---|---|
| Langage / Framework | Java 21, Spring Boot 3.5 |
| Sécurité | Spring Security, JWT |
| Base de données | PostgreSQL, Spring Data JPA, Flyway |
| Messagerie | RabbitMQ |
| Tests | JUnit 5, Mockito, AssertJ |
| Documentation | Swagger / OpenAPI |
| Conteneurisation | Docker, Docker Compose |

## Architecture

Architecture en couches, avec séparation stricte des responsabilités :

\`\`\`
controller/  → endpoints REST uniquement
service/     → logique métier
repository/  → accès aux données (Spring Data JPA)
entity/      → modèle de données (JPA)
dto/         → objets exposés par l'API
mapper/      → conversion entité ↔ DTO
security/    → authentification JWT
messaging/   → producteur / consommateur RabbitMQ
exception/   → gestion centralisée des erreurs
config/      → configuration Spring (sécurité, OpenAPI, RabbitMQ)
\`\`\`


## Lancer le projet

Prérequis : Docker et Docker Compose.

```bash
git clone https://github.com/Mohamedboukcim/expense-tracker.git
cd expense-tracker
docker compose up --build
```

L'API est accessible sur `http://localhost:8080`.
La documentation Swagger est sur `http://localhost:8080/swagger-ui.html`.
L'interface d'administration RabbitMQ est sur `http://localhost:15672` (guest / guest).

## Tester l'API

1. `POST /api/auth/register` pour créer un compte
2. Copier le token JWT renvoyé
3. Dans Swagger, cliquer sur **Authorize** et coller le token
4. Tester librement les endpoints protégés

## Lancer les tests

```bash
mvn test
```

## Points techniques notables

- **Protection IDOR** : chaque opération sur une ressource (catégorie, dépense, budget) vérifie son appartenance à l'utilisateur authentifié.
- **Filtres dynamiques** : les dépenses se filtrent par date, catégorie et montant grâce aux Spring Data JPA Specifications, combinables librement.
- **Alertes découplées** : la détection de dépassement de budget publie un message RabbitMQ plutôt que d'appeler directement un service de notification, pour ne pas ralentir la création d'une dépense.
- **Authentification stateless** : JWT signé en HMAC-SHA256, sans session côté serveur.