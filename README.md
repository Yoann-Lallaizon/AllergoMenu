# 🥗 AllergoMenu

_AllergoMenu_ est une application Java permettant à ses utilisateurs de générer des menus personnalisés tout en prenant en compte leurs allergies ou préférences alimentaires. Le projet met en œuvre une architecture **MVC (Modèle-Vue-Contrôleur)** propre et modulaire.

---

## 📋 Fonctionnalités principales

- ✅ Inscription / Connexion des utilisateurs
- 👤 Complétion de profil avec gestion des allergies
- 🍽️ Création et consultation de recettes
- 🧑‍🍳 Génération automatique de menus selon les restrictions
- ✍️ Ajout et modération de commentaires
- 🛠️ Interface d'administration pour modérer recettes et utilisateurs

---

## 🧱 Architecture du projet

Le projet est structuré de manière modulaire en quatre packages :

### `controllers/`

Contient la logique métier liée à l'interaction entre la vue et les modèles.
Exemples :

- `ControllerConnexion.java` : Gère la logique d'authentification
- `ControllerCreerRecette.java` : Gère la création de nouvelles recettes
- `ControllerGenererMenu.java` : Génère un menu personnalisé selon les allergies

### `models/`

Contient les classes représentant les entités du système.
Exemples :

- `Utilisateur.java`, `Recette.java`, `Ingredient.java`, `Maladie.java`, etc.

### `views/`

Contient les interfaces utilisateur (JavaFX).
Exemples :

- `VueConnexion.java`, `VueAccueil.java`, `VueGenererMenu.java`, etc.

### `main/`

Point d'entrée de l'application + gestion de la connexion à la base de données.

- `MainApp.java` : Lance l'application
- `ConnectionDB.java` : Gère la connexion JDBC

---

## 🚀 Lancer l'application

### ✅ Prérequis

- Java 11 ou supérieur
- JavaFX SDK
- IDE compatible Java (Eclipse recommandé)

### ⚙️ Étapes

1. Cloner le dépôt :

```bash
git clone <https://github.com/projetBTSSIO/AllergoMenu>
```

2. Ouvrir le projet dans Eclipse ou IntelliJ

3. Ajouter le JavaFX SDK au classpath

4. Lancer `MainApp.java`

> ⚠️ Le projet nécessite une base de données connectée (via `ConnectionDB.java`). Vérifiez les identifiants dans ce fichier.

---

## 🛠️ Base de données

Le projet repose sur une base de données relationnelle (probablement MySQL). Les entités `Utilisateur`, `Recette`, `Maladie`, etc. sont persistées et liées via des relations.

---

## 🎨 Ressources

- Feuille de style : `styles.css`
- Images et illustrations dans `src/images/`

---

## 🙋‍♂️ Auteur

Projet éducatif développé dans le cadre du BTS SIO au Greta de Quimper.
