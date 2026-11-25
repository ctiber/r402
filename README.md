# R4.02 — Qualité de développement : supports

Diapositives des cours, sujets de TP et plan du module (BUT 2 Informatique, IUT de Vannes).
Le projet fil rouge est sur la branche `main` de ce dépôt.

| Semaine | Cours | TP |
|---|---|---|
| 1 | [Qualité logicielle et panorama des tests](cours/cm1_panorama_tests/cm1_panorama_tests.pdf) | [Projet fil rouge et JUnit avancé](tp/tp1_junit/tp1_junit.pdf) |
| 2 | [User stories, tests d'acceptation et faisabilité](cours/cm2_stories_faisabilite/cm2_stories_faisabilite.pdf) | [User stories, faisabilité et Cucumber](tp/tp2_bdd_cucumber/tp2_bdd_cucumber.pdf) |
| 3 | [Testabilité et doublures de test](cours/cm3_doublures/cm3_doublures.pdf) | [Tests unitaires isolés avec Mockito](tp/tp3_mockito/tp3_mockito.pdf) |
| 4 | [Tests d'intégration avec une base de données](cours/cm4_integration/cm4_integration.pdf) | [Tests d'intégration avec une base de données](tp/tp4_integration_bd/tp4_integration_bd.pdf) |
| 5 | [Tests de bout en bout et tests d'interface](cours/cm5_e2e/cm5_e2e.pdf) | [Tests de bout en bout avec Playwright](tp/tp5_e2e/tp5_e2e.pdf) |
| 6 | [Environnements, intégration continue et couverture](cours/cm6_ci_qualite/cm6_ci_qualite.pdf) | [Pipeline GitLab CI, couverture et mutation](tp/tp6_ci_qualite/tp6_ci_qualite.pdf) |

Exemple de pipeline vu au CM6 : [`cours/cm6_ci_qualite/gitlab-ci-exemple.yml`](cours/cm6_ci_qualite/gitlab-ci-exemple.yml).
Plan du module : [`plan_cours.md`](plan_cours.md).

## Compiler les supports

Sources LaTeX à côté de chaque PDF ; préambules et logos communs dans `latex/common/`.
Il faut une distribution TeX Live récente (classe `beamer` et thème `metropolis` pour les diapositives).

Chaque document se compile depuis son propre dossier :

```
cd cours/cm1_panorama_tests && latexmk -pdf cm1_panorama_tests.tex && latexmk -c
cd tp/tp1_junit && latexmk -pdf tp1_junit.tex && latexmk -c
```

Tout recompiler :

```
for f in cours/*/*.tex tp/*/*.tex; do (cd "$(dirname "$f")" && latexmk -pdf -interaction=nonstopmode "$(basename "$f")" && latexmk -c); done
```

---
Supports élaborés avec l'aide de Claude Code Opus 5.5
