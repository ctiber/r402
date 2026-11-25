# R4.02 — Qualité de développement (BUT2 Informatique) — 2026/2027

Plan prévisionnel — version de travail

## 1. Cadrage

**Programme national (fiche R4.02)**

- Objectif : approfondir la production de tests et identifier les critères de faisabilité d'un projet informatique.
- Savoirs de référence : problématique de la non-régression, tests d'intégration.
- Prolongements suggérés : tests fonctionnels, tests UI, tests d'utilisabilité, CI/CD, couverture de tests.
- AC21.04 : vérifier et valider la qualité de l'application par les tests.
- AC25.03 : identifier les critères de faisabilité d'un projet informatique.
- Volume PN : 13 h, dont 10 h de TP. Volume prévu ici : 13 h 30 sur 6 semaines, dont 4 h 30 de CM (6 × 45 min) et 9 h de TP (6 × 1 h 30).

**Prérequis des étudiant·es**

- JUnit en S2 (assertions, cycle de vie d'un test).
- R3.07 en S3 : JDBC, JPA, DAO/DTO, Repository, Spring Data, PostgreSQL, Docker / docker-compose, Git.

**Fil directeur**

Une même idée relie les six semaines : partir du besoin pour aller jusqu'à une pipeline qui vérifie automatiquement l'application.

> user story → critères d'acceptation → tests unitaires (avec doublures) → tests d'intégration (BD) → tests de bout en bout (UI) → pipeline de CI (non-régression, couverture)

**Projet fil rouge des TP (proposition)**

On repart de l'application bancaire de R3.07 (Client, Compte, Operation, Virement). Vous connaissez déjà le domaine, ce qui vous permet de vous concentrer sur les tests. On vous fournit un projet de départ :

- back-end Spring Boot + Spring Data JPA + PostgreSQL (docker-compose), exposant une API REST ;
- front-end **Vue 3, sans étape de construction** (pas de Node.js à installer), fourni et volontairement minimal : connexion, liste des comptes, formulaire de virement. Vous n'aurez pas à le développer, seulement à le lire, à le corriger et à y ajouter des attributs `data-testid`. Il est servi par Spring Boot : une seule application à lancer, en local comme en CI ;
- une couche service qui dépend de collaborateurs externes à simuler : un service de taux de change, un service de notification par e-mail ou SMS, une horloge (`Clock`) ;
- quelques bugs volontaires et du code non testé, pour que les tests aient quelque chose à trouver ;
- Maven, avec un `pom.xml` déjà configuré pour JUnit Jupiter, Mockito, AssertJ, Testcontainers, Cucumber, Playwright for Java et JaCoCo.

Chaque binôme travaille sur son propre dépôt Git, créé à partir d'un dépôt modèle. Le rendu final est ce dépôt, avec une pipeline de CI qui passe.

## 2. Déroulement sur 6 semaines

Chaque semaine : **un CM de 45 min suivi d'un TP de 1 h 30** qui le met en pratique tout de suite. Total : 6 × 45 min = 4 h 30 de CM et 6 × 1 h 30 = 9 h de TP.

| Sem. | CM (45 min) | TP (1 h 30) | Supports |
|---|---|---|---|
| 1 | CM1 Qualité logicielle, panorama des tests, non-régression | TP1 Projet fil rouge et JUnit avancé | cours/cm1_panorama_tests, tp/tp1_junit |
| 2 | CM2 User stories, tests d'acceptation et faisabilité | TP2 Stories, grille de faisabilité et Cucumber | cours/cm2_stories_faisabilite, tp/tp2_bdd_cucumber |
| 3 | CM3 Testabilité et doublures de test (Mockito) | TP3 Tests unitaires isolés avec Mockito | cours/cm3_doublures, tp/tp3_mockito |
| 4 | CM4 Tests d'intégration avec une base de données | TP4 Testcontainers, tranches Spring, MockMvc | cours/cm4_integration, tp/tp4_integration_bd |
| 5 | CM5 Tests de bout en bout et tests d'interface | TP5 Playwright et Page Object | cours/cm5_e2e, tp/tp5_e2e |
| 6 | CM6 Environnements, intégration continue et couverture de tests | TP6 Pipeline GitLab CI, JaCoCo, PIT | cours/cm6_ci_qualite, tp/tp6_ci_qualite |

Les deux quiz ont lieu dans des **séances à part**, en fin de semaine 3 et en fin de semaine 6 : ils ne prennent pas de temps sur les TP.

## 3. Cours magistraux (6 × 45 min)

### CM1 — Qualité logicielle et panorama des tests

1. **Pourquoi tester ?** (10 min)
   - Coût d'un bug selon le moment où on le découvre ; exemples d'incidents réels (Ariane 5, Knight Capital, CrowdStrike 2024).
   - Qualité logicielle : l'ISO 25010 en une diapo.
   - Vérification (« construire le produit correctement ») et validation (« construire le bon produit »).
2. **Panorama des tests** (15 min)
   - Niveaux : unitaire, intégration, système / bout en bout, acceptation.
   - Types : fonctionnels, non fonctionnels (performance, sécurité, utilisabilité), boîte noire et boîte blanche.
   - Pyramide des tests, et ses variantes (trophée, cornet de glace comme anti-modèle).
   - **Non-régression** : définition, pourquoi l'automatiser, lien avec la CI (on y revient au CM6).
3. **JUnit : rappels et nouveautés** (15 min)
   - Structure AAA / Given-When-Then ; propriétés FIRST.
   - Tests paramétrés, `assertThrows`, `assertAll`, `@Nested`, `@DisplayName`, AssertJ.
4. **Présentation du projet fil rouge** (5 min)

### CM2 — User stories, tests d'acceptation et faisabilité

1. **Du besoin au test** (20 min)
   - Contexte : les user stories viennent des méthodes agiles (XP, Scrum) ; *product backlog*, *product owner*, les 3C (carte, conversation, confirmation).
   - User story : « En tant que… je veux… afin de… » ; critères INVEST.
   - Critères d'acceptation au format Gherkin (Given / When / Then), avec des exemples concrets (*specification by example*).
   - Definition of Ready et Definition of Done.
   - Tests d'acceptation et **recette** : le client valide les stories sur un environnement dédié avant la mise en production (détaillé au CM6).
   - ATDD et BDD : écrire les tests d'acceptation avant le code ; Cucumber-JVM (`.feature`, *step definitions*).
2. **Critères de faisabilité d'un projet** (20 min) — AC25.03
   - Faisabilité technique (technologies maîtrisées ou non, dépendances externes, données), temporelle (estimation, *planning poker*, vélocité), économique et humaine (compétences de l'équipe), juridique (RGPD, licences).
   - Risques : identification, probabilité × impact, *spike* ou preuve de concept (POC) pour lever une incertitude.
   - La testabilité comme critère de faisabilité : une story dont on ne sait pas écrire les critères d'acceptation n'est pas prête.
   - Présentation de la grille d'analyse de faisabilité, réutilisée en TP2.
3. **Synthèse** (5 min)

### CM3 — Testabilité et doublures de test

1. **Testabilité du code** (10 min)
   - Dépendances difficiles à tester : base de données, réseau, horloge, aléatoire.
   - Injection de dépendances (par constructeur), programmation par interfaces : lien avec le Repository vu en R3.07.
2. **Doublures de test** (15 min)
   - Typologie de Meszaros : dummy, stub, fake, spy, mock.
   - Vérifier un état ou vérifier un comportement ; école de Londres (*mockist*) et école de Detroit ou de Chicago (*classicist*).
3. **Mockito** (15 min)
   - `@Mock`, `@InjectMocks`, `when/thenReturn`, `thenThrow`, `verify`, `ArgumentCaptor`, `@Spy`.
   - Erreurs fréquentes : tout simuler, simuler ce qu'on ne possède pas, tests trop couplés à l'implémentation.
4. **Synthèse** (5 min)

### CM4 — Tests d'intégration avec une base de données

1. **Qu'est-ce qu'on intègre ?** (10 min)
   - Code et base de données, code et API externe, couches de l'application entre elles.
   - Stratégies : *big bang*, ascendante, descendante. Quand utiliser une doublure, quand utiliser un vrai composant.
2. **Base de données de test** (15 min)
   - H2 en mémoire et ses limites (dialecte, triggers, procédures stockées).
   - **Testcontainers** : un vrai PostgreSQL dans Docker, le temps des tests.
   - Isolation et jeux de données : rollback transactionnel, `@Sql`, données de test reproductibles.
3. **Tests d'intégration avec Spring** (15 min)
   - `@DataJpaTest`, `@SpringBootTest`, `@MockitoBean` (remplace `@MockBean`), `MockMvc`.
   - Une configuration par environnement : profils Spring (`application-test.properties`), variables d'environnement ; premier contact avec la notion d'environnement, reprise au CM6.
4. **Synthèse** (5 min)

### CM5 — Tests de bout en bout et tests d'interface

1. **Tests de bout en bout** (15 min)
   - Objectif, coût, lenteur, fragilité (*flaky tests*) ; place dans la pyramide.
   - Outils : Selenium (historique, encore très présent en entreprise), Cypress (JavaScript uniquement, en perte de vitesse), Playwright (le plus adopté aujourd'hui pour les nouveaux projets, disponible en Java) ; comparaison rapide.
   - Playwright : attentes automatiques, plusieurs navigateurs (Chromium, Firefox, WebKit), générateur de code (`codegen`), *trace viewer* pour comprendre un test qui échoue.
2. **Écrire des tests UI robustes** (20 min)
   - Patron Page Object, attentes explicites, sélecteurs robustes (`data-testid`), navigateur *headless*.
   - Réutiliser les scénarios Cucumber du CM2 au niveau de l'interface.
3. **Ouverture : tests d'utilisabilité** (10 min)
   - Tests avec des utilisateurs, heuristiques de Nielsen, accessibilité.

### CM6 — Environnements, intégration continue et couverture de tests

1. **Les environnements d'une application** (12 min)
   - Développement (poste du développeur), intégration (CI), **recette** (*staging*, souvent avec une préproduction), **production**.
   - Ce qu'on teste dans chaque environnement : unitaires et intégration en dev et en CI ; bout en bout et acceptation (recette client) en recette ; *smoke tests* et supervision en production.
   - Principes : la recette doit ressembler le plus possible à la production ; on construit une seule fois et on promeut le même artefact (image Docker) d'un environnement à l'autre ; la configuration (URL, base, secrets) change, pas le code.
   - Données : jeux de test en dev et en CI, données réalistes en recette (anonymisées : RGPD), jamais de tests sur les données de production.
2. **Intégration et déploiement continus** (15 min)
   - CI, livraison continue et déploiement continu ; la non-régression automatisée à chaque *push* et à chaque *merge request*.
   - Anatomie d'une pipeline GitLab CI (`.gitlab-ci.yml`) : étapes (*stages*), jobs, images, artefacts, services (PostgreSQL, Docker pour Testcontainers), environnements GitLab (`environment:`), déploiement en production déclenché manuellement.
   - Séparer tests rapides et tests lents (Surefire / Failsafe, tags JUnit) ; rapports de tests dans la *merge request*.
3. **Couverture de tests** (12 min)
   - Couverture des lignes, des branches, des conditions ; JaCoCo.
   - Seuil minimal qui fait échouer le build ; affichage de la couverture dans GitLab (badge, lignes couvertes dans la *merge request*).
   - Limites : 100 % de couverture ne garantit pas l'absence de bugs.
   - Tests de mutation (PIT) : mesurer si les tests détectent vraiment des erreurs.
4. **Synthèse générale du module et ouverture** (6 min)
   - Récapitulatif : quel test, pour quel risque, dans quel environnement.
   - Pour aller plus loin : analyse statique du code, *quality gates*, analyse de sécurité dans la pipeline.

## 4. Travaux pratiques (6 × 1 h 30)

### TP1 — Projet fil rouge et JUnit avancé

- Prise en main du projet de départ : `docker compose up`, `mvn test`, lecture de l'architecture.
- Tests de la couche domaine (classe `Compte`) : tests paramétrés (`@CsvSource`), exceptions, `assertAll`, `@Nested`, AssertJ.
- Trouver, grâce aux tests, les bugs volontaires de la couche domaine.
- Non-régression : corriger un bug, ajouter le test qui le reproduit, vérifier que les autres tests passent toujours.

### TP2 — User stories, faisabilité et Cucumber

- Partie 1 (45 min) : à partir d'un court cahier des charges (virement programmé, plafond de retrait, conversion de devises, notification), rédiger des user stories INVEST avec leurs critères d'acceptation en Gherkin, puis remplir la grille de faisabilité. Au moins une story est volontairement problématique (dépendance externe incertaine, critère non testable, règle contradictoire) et doit être repérée.
- Partie 2 (45 min) : transformer les critères en fichiers `.feature` (Cucumber déjà configuré dans le projet), écrire les *step definitions*, utiliser `Scenario Outline` et `Examples`. Implémenter une story en ATDD (scénario rouge, puis code, puis scénario vert).
- Livrable : `docs/user_stories.md` + scénarios Cucumber qui passent.

### TP3 — Tests unitaires isolés avec Mockito

- Tester `VirementService` en isolant ses dépendances : `CompteRepository`, `TauxChangeClient` (API externe), `NotificationService`, `Clock`.
- Stubs (`when/thenReturn`, `thenThrow`), vérification d'interactions (`verify`, `never`, `times`), `ArgumentCaptor`.
- Cas d'erreur : API de change indisponible, solde insuffisant, compte inexistant.
- Refactoriser une classe difficile à tester (`new` et `LocalDate.now()` dans le code) pour la rendre testable.
- Discussion : relancer les tests après un refactoring interne et voir lesquels cassent (tests trop couplés à l'implémentation).

### TP4 — Tests d'intégration avec une base de données

- `@DataJpaTest` + Testcontainers (PostgreSQL) : tester les requêtes dérivées et `@Query` des repositories.
- Montrer un cas où H2 donne un résultat différent de PostgreSQL (fonction SQL, contrainte ou trigger de R3.07).
- `@SpringBootTest` + `MockMvc` : tester un contrôleur REST jusqu'à la base de données, avec l'API externe simulée par `@MockitoBean`.
- Isolation des tests : jeux de données (`@Sql`), rollback, ordre d'exécution indifférent.
- Configuration de test séparée avec un profil Spring `test` (aucun test ne doit utiliser la base de développement).
- Vérifier une transaction : un virement qui échoue à mi-parcours ne laisse pas de données incohérentes.

### TP5 — Tests de bout en bout de l'interface web

- Playwright for Java (navigateur en mode *headless*) sur le front Vue ; générer un premier test avec `codegen`, puis le réécrire proprement.
- Scénario complet : connexion, consultation des comptes, virement, vérification du solde affiché.
- Patron Page Object, sélecteurs robustes (`getByRole`, `getByTestId`), ajout d'attributs `data-testid`.
- Déboguer un test qui échoue avec le *trace viewer*.
- Brancher un scénario Cucumber du TP2 sur Playwright : la même spécification exécutée de bout en bout.

### TP6 — Pipeline GitLab CI et couverture de tests

- Écrire le `.gitlab-ci.yml` (sur la forge GitLab de l'IUT) : build, tests unitaires, tests d'intégration (Docker / Testcontainers), tests de bout en bout (image Playwright).
- Publier les rapports de tests JUnit dans GitLab.
- Simuler un environnement de recette : construire l'image Docker de l'application, la lancer dans la pipeline avec le profil `staging` et sa propre base, puis exécuter les tests Playwright et un *smoke test* contre elle (job déclaré avec `environment: staging`). Ajouter un job de « mise en production » manuel (`when: manual`), qui ne fait qu'afficher l'image qui serait déployée.
- JaCoCo : générer le rapport, fixer un seuil minimal (par exemple 70 % des branches) qui fait échouer le build s'il n'est pas atteint ; afficher la couverture dans GitLab.
- Non-régression : introduire un bug dans une *merge request*, constater que la pipeline échoue, corriger.
- PIT : lancer les tests de mutation sur `VirementService`, comprendre un mutant qui survit et ajouter le test qui le tue.
- État final attendu du dépôt : pipeline qui passe, README (comment lancer les tests, couverture obtenue), `docs/user_stories.md`.

## 5. Évaluation

- **Quiz 1**, dans une **séance dédiée** (hors TP) à la fin de la semaine 3 : CM1 à CM3 (panorama des tests, JUnit, user stories, faisabilité, doublures de test).
- **Quiz 2**, dans une **séance dédiée** (hors TP) à la fin de la semaine 6 : CM4 à CM6 (intégration, bout en bout, CI, couverture), avec quelques questions transverses.
- **Rendus de TP** : les six TP sont rendus en binôme, à la fin de chaque séance (tag Git `tp1` … `tp6` sur le dépôt du binôme, date limite indiquée dans chaque sujet). Un seul TP est corrigé, **choisi par l'enseignant à la fin du module et non annoncé** : tous les rendus doivent donc être soignés. La grille est adaptée au TP retenu ; critères communs : tests qui passent, pertinence des cas testés (cas nominaux, cas limites, cas d'erreur), lisibilité des tests (nommage, AAA), README à jour.
- **Coefficient d'assiduité** sur la note de TP, par paliers. On compte les défauts sur les six rendus :
  - un rendu non rendu ou en retard = 1 défaut ;
  - deux rendus incomplets = 1 défaut (un rendu incomplet seul ne compte pas ; trois incomplets = 1 défaut, quatre = 2).

  | Défauts | 0 | 1 | 2 et plus |
  |---|---|---|---|
  | `coef_assiduité` | 1 | 0,9 | 0,75 |

  Formule : `défauts = nb_non_rendus + nb_en_retard + ⌊nb_incomplets / 2⌋`.

**Note finale**

```
note_finale = MOY( MOY(quiz1, quiz2), note_TP × coef_assiduité )
```

La moyenne des quiz et la note de TP (pondérée par l'assiduité) comptent chacune pour moitié.

## 6. Couverture du programme national

| Élément du PN | Où |
|---|---|
| Problématique de la non-régression | CM1, CM6 ; TP1 (bug corrigé + test), TP6 (CI) |
| Tests d'intégration | CM4 ; TP4 |
| Tests fonctionnels | CM2 ; TP2 (Cucumber), TP5 |
| Test UI | CM5 ; TP5 |
| Tests d'utilisabilité | CM5 (ouverture) |
| CI/CD (dont environnements dev, recette, production) | CM6 ; TP4 (profil de test), TP6 (recette simulée, mise en production manuelle) |
| Couvertures de tests | CM6 ; TP6 (JaCoCo, seuil, PIT) |
| AC21.04 Vérifier et valider la qualité par les tests | Tous les TP |
| AC25.03 Critères de faisabilité | CM2 ; TP2 (grille de faisabilité) |

## 7. Organisation des fichiers (calquée sur R3.07)

```
cours/cm1_… à cm6_…            diapos beamer (une par CM de 45 min)
tp/tp1_… à tp6_…                sujets + correction/
latex/common/                    préambules r402-slides-preamble.tex, r402-tp-preamble.tex, logos
code/banque-starter/             projet de départ (dépôt modèle) : Spring Boot + front Vue 3 sans étape de construction
docs/plan_cours.md
```

## 8. Choix faits

- Outil de bout en bout : **Playwright for Java**. C'est aujourd'hui l'outil le plus adopté pour les nouveaux projets ; il reste en Java (JUnit, Cucumber, même projet Maven), contrairement à Cypress qui impose JavaScript et un projet séparé.
- Le module se concentre sur les tests : la couverture est mesurée par JaCoCo, complétée par les tests de mutation (PIT).
- CI : **GitLab CI** sur la forge de l'IUT. Il faut que Docker soit disponible sur les *runners* pour Testcontainers.
- Projet fil rouge : l'application bancaire de R3.07.
- Interface web : front Vue 3 fourni, qui appelle l'API REST Spring Boot.
- Évaluation : deux quiz en séances dédiées (fin des semaines 3 et 6) + six TP rendus en binôme, dont un seul corrigé, choisi par l'enseignant à la fin et non annoncé.
- Note : MOY(MOY(quiz1, quiz2), note_TP × coef_assiduité).
- Coefficient d'assiduité par paliers : 1 / 0,9 / 0,75 selon le nombre de défauts (non rendu ou retard = 1 ; deux incomplets = 1).
