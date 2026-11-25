# R4.02 — Qualité de développement (BUT 2 Informatique, IUT de Vannes)

Ce dépôt contient les **supports du module** (diapositives, sujets de TP, plan) et le **projet fil rouge** utilisé dans les six TP.

## Supports

| Semaine | Cours | TP |
|---|---|---|
| 1 | [Qualité logicielle et panorama des tests](cours/cm1_panorama_tests/cm1_panorama_tests.pdf) | [Projet fil rouge et JUnit avancé](tp/tp1_junit/tp1_junit.pdf) |
| 2 | [User stories, tests d'acceptation et faisabilité](cours/cm2_stories_faisabilite/cm2_stories_faisabilite.pdf) | [User stories, faisabilité et Cucumber](tp/tp2_bdd_cucumber/tp2_bdd_cucumber.pdf) |
| 3 | [Testabilité et doublures de test](cours/cm3_doublures/cm3_doublures.pdf) | [Tests unitaires isolés avec Mockito](tp/tp3_mockito/tp3_mockito.pdf) |
| 4 | [Tests d'intégration avec une base de données](cours/cm4_integration/cm4_integration.pdf) | [Tests d'intégration avec une base de données](tp/tp4_integration_bd/tp4_integration_bd.pdf) |
| 5 | [Tests de bout en bout et tests d'interface](cours/cm5_e2e/cm5_e2e.pdf) | [Tests de bout en bout avec Playwright](tp/tp5_e2e/tp5_e2e.pdf) |
| 6 | [Environnements, intégration continue et couverture](cours/cm6_ci_qualite/cm6_ci_qualite.pdf) | [Pipeline GitLab CI, couverture et mutation](tp/tp6_ci_qualite/tp6_ci_qualite.pdf) |

Plan du module : [`plan_cours.md`](plan_cours.md). Exemple de pipeline vu au CM6 : [`cours/cm6_ci_qualite/gitlab-ci-exemple.yml`](cours/cm6_ci_qualite/gitlab-ci-exemple.yml).

Les sources LaTeX sont à côté de chaque PDF (préambules et logos communs dans `latex/common/`). Chaque document se compile depuis son dossier, par exemple `cd tp/tp1_junit && latexmk -pdf tp1_junit.tex && latexmk -c` (TeX Live récent, `beamer` et thème `metropolis` pour les diapositives).

# Le projet fil rouge : application bancaire

Application bancaire (clients, comptes, opérations, virements) utilisée dans les six TP du module.
Elle fonctionne… mais elle est peu testée, et elle contient des bugs. À vous de les trouver.

## Prérequis

- Java 21
- Docker (base de développement, et base de test des TP4 à TP6)
- IntelliJ IDEA (ou un autre IDE Java)

Maven n'a pas besoin d'être installé : utilisez le script `./mvnw` (`mvnw.cmd` sous Windows).

## Lancer les tests

```
./mvnw test
```

## Lancer l'application

```
docker compose up -d                                    # base PostgreSQL de développement
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev   # le profil dev ajoute des comptes de démonstration
```

Puis ouvrir http://localhost:8080 et se connecter avec l'un des utilisateurs de démonstration :

| Identifiant | Mot de passe | Comptes |
|---|---|---|
| `alice` | `alice123` | `FR76-001` (courant), `FR76-002` (épargne) |
| `bruno` | `bruno123` | `FR76-003` (courant), `US-042` (courant en dollars) |

## Organisation du code

```
src/main/java/fr/iutvannes/banque/
  domaine/          Compte, Operation et leurs règles métier (sans Spring)
  service/          VirementService, ReleveService, interfaces des services externes
  persistance/      repositories Spring Data
  infrastructure/   implémentations des services externes (taux de change, SMS), horloge
  web/              API REST
  securite/         connexion des utilisateurs
  job/              traitements lancés par un ordonnanceur
src/main/resources/
  db/migration/     schéma de la base (Flyway)
  static/           front : page de connexion et application Vue 3 (sans étape de construction)
src/test/java/fr/iutvannes/banque/
  TestUtils         eur("12.50")
  doublures/        CompteRepositoryEnMemoire, HorlogeModifiable
  acceptation/      lanceur Cucumber et ContexteBanque (TP2)
  e2e/              BaseE2ETest (TP5)
src/test/resources/
  features/         scénarios Cucumber (TP2)
  donnees/          jeux de données de test
docs/               vos réponses aux questions des TP (docs/tp1.md, docs/tp2.md, ...)
cours/, tp/, latex/ supports du module (voir plus haut)
```

## API REST

| Méthode | URL | Réponse |
|---|---|---|
| `GET` | `/api/moi` | l'utilisateur connecté |
| `GET` | `/api/comptes` | ses comptes |
| `GET` | `/api/comptes/{numero}/solde?devise=USD` | `200`, `404` si le compte n'existe pas, `503` si le service de change est indisponible |
| `GET` | `/api/comptes/{numero}/releve?mois=2027-03` | relevé mensuel |
| `POST` | `/api/virements` `{"source", "cible", "montant"}` | `201`, `400` (demande invalide), `404` (compte inconnu), `422` (virement refusé) |

L'API exige d'être connecté (`401` sinon) et, pour les `POST`, le jeton CSRF (cookie `XSRF-TOKEN` recopié dans l'en-tête `X-XSRF-TOKEN`).

## Rendus

À la fin de chaque TP : un tag `tp1`, `tp2`, … poussé sur ce dépôt (voir le sujet).
