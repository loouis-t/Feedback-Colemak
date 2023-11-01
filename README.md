# Feedback Colemak

## Lancer le projet (démarrer un serveur)

Dans un terminal, à la racine du projet, lancer la commande `./mvnw spring-boot:run`.  
Le site est accessible à l'adresse [http://localhost:8080]('http://localhost:8080').

## Structure (MVC)

### Model

La partie qui gère l'accès aux données. Elle contient les classes qui représentent les données, et les classes qui gèrent l'accès à ces données (ie. les `@Repository`, laissés vides car l'accès aux données est simplifié par l'utilisation des relations `@OneToOne`, `@ManyToOne`, `@OneToMany`, ...).

### View

C'est la partie qui concerne l'affichage.  
Ce sont les fichiers placés dans le dossier `resources/`. Les pages (en `.html`) sont dans le dossier `template/`, les fichiers statiques (css, js, ...) sont dans le dossier `static/`.

### Controller

C'est la partie qui gère les requêtes.  
Ce code `java` est placé dans le package `controller`, et permet de créer les routes (ex : `@GetMapping('/')` pour la page d'accueil, ou `@RequestMapping(value = "/logout", method = RequestMethod.GET)` pour la fonction de déconnexion) permettant soit d'afficher les pages, soit d'effectuer des traitements sur les données.