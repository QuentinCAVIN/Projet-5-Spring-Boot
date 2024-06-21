
# SafetyNet Alerts API

## Projet 5 OC : Société SafetyNet

**SafetyNet Alerts** est une API développée avec Spring Boot, destinée à fournir des renseignements aux services de secours en cas de sinistre. Elle a pour but d’envoyer des informations vitales aux systèmes de services d’urgence lors de situations critiques telles que les incendies, les ouragans ou les inondations.


  ![Diagramme de classe](https://github.com/QuentinCAVIN/Projet-5-Spring-Boot/assets/117484688/7a10e7ab-30f3-49b1-8e89-4e7eece34b0a)


## Objectif

- **Conception et développement** d'une application backend à partir de zéro.
- **Création d'endpoints** permettant de fournir des données précises sur les personnes en détresse, en exploitant un fichier de données contenant des informations sur les individus et leur localisation.
- **Implémentation de tests unitaires et d'intégration** avec un rapport de couverture des tests.
- **Intégration d'un système de gestion des logs** pour faciliter le suivi et le débogage de l'application.
- Adoption de **l'architecture modèle-vue-contrôleur** en respectant les principes SOLID pour garantir la maintenabilité.

## Caractéristiques du projet

### Technologies Utilisées

- Spring Boot
- Maven
- API RESTful
- Architecture MVC
- Principes SOLID
- Base de données H2
- JPA (Java Persistence API)
- Sérialisation JSON
- Log4j
- Postman pour les tests d'API

### Outils de développement

- Couverture des tests avec Jacoco : 95%
- Diagramme de classe UML pour la modélisation




## Liste des Endpoints

#### 1. Récupérer les personnes couvertes par une caserne de pompiers

- **URL :** `http://localhost:8080/firestation?stationNumber={station_number}`
- **Méthode :** GET
- **Description :** Retourne une liste des personnes couvertes par la caserne de pompiers correspondant au numéro fourni. La liste inclut le prénom, nom, adresse, numéro de téléphone des habitants, ainsi qu'un décompte du nombre d'adultes et d'enfants (individus âgés de 18 ans ou moins) dans la zone desservie.

#### 2. Récupérer les enfants habitant à une adresse spécifique

- **URL :** `http://localhost:8080/childAlert?address={address}`
- **Méthode :** GET
- **Description :** Retourne une liste d'enfants (individus âgés de 18 ans ou moins) habitant à l'adresse spécifiée. La liste comprend le prénom, nom, âge de chaque enfant, ainsi qu'une liste des autres membres du foyer. Si aucun enfant n'est trouvé, la réponse est une chaîne vide.

#### 3. Récupérer les numéros de téléphone des résidents desservis par une caserne de pompiers

- **URL :** `http://localhost:8080/phoneAlert?firestation={firestation_number}`
- **Méthode :** GET
- **Description :** Retourne une liste des numéros de téléphone des résidents desservis par la caserne de pompiers spécifiée.

#### 4. Récupérer les habitants vivant à une adresse donnée

- **URL :** `http://localhost:8080/fire?address={address}`
- **Méthode :** GET
- **Description :** Retourne la liste des habitants vivant à l'adresse donnée ainsi que le numéro de la caserne de pompiers la desservant. La liste inclut le nom, numéro de téléphone, âge et antécédents médicaux (médicaments, posologie, allergies) de chaque personne.

#### 5. Récupérer les foyers desservis par des casernes spécifiques

- **URL :** `http://localhost:8080/flood/stations?stations={list_of_station_numbers}`
- **Méthode :** GET
- **Description :** Retourne une liste de tous les foyers desservis par les casernes spécifiées. La liste regroupe les personnes par adresse et inclut le nom, numéro de téléphone, âge des habitants, ainsi que leurs antécédents médicaux (médicaments, posologie, allergies).

#### 6. Récupérer les informations d'une personne spécifique

- **URL :** `http://localhost:8080/personInfo?firstName={firstName}&lastName={lastName}`
- **Méthode :** GET
- **Description :** Retourne le nom, adresse, âge, adresse mail et antécédents médicaux (médicaments, posologie, allergies) de chaque habitant correspondant au prénom et nom spécifiés. Si plusieurs personnes portent le même nom, elles apparaissent toutes.

#### 7. Récupérer les adresses email des habitants d'une ville

- **URL :** `http://localhost:8080/communityEmail?city={city}`
- **Méthode :** GET
- **Description :** Retourne les adresses email de tous les habitants de la ville spécifiée.

### Gestion des Personnes

#### 8. Ajouter, mettre à jour ou supprimer une personne

- **URL :** `http://localhost:8080/person`
- **Méthodes :** POST, PUT, DELETE
- **Description :**
  - **POST :** Ajouter une nouvelle personne.
  - **PUT :** Mettre à jour une personne existante (en supposant que le prénom et le nom ne changent pas).
  - **DELETE :** Supprimer une personne (utiliser une combinaison de prénom et nom comme identificateur unique).

### Gestion des Casernes de Pompiers

#### 9. Ajouter, mettre à jour ou supprimer un mapping caserne/adresse

- **URL :** `http://localhost:8080/firestation`
- **Méthodes :** POST, PUT, DELETE
- **Description :**
  - **POST :** Ajouter un mapping caserne/adresse.
  - **PUT :** Mettre à jour le numéro de la caserne de pompiers d'une adresse.
  - **DELETE :** Supprimer le mapping d'une caserne ou d'une adresse.

### Gestion des Dossiers Médicaux

#### 10. Ajouter, mettre à jour ou supprimer un dossier médical

- **URL :** `http://localhost:8080/medicalRecord`
- **Méthodes :** POST, PUT, DELETE
- **Description :**
  - **POST :** Ajouter un dossier médical.
  - **PUT :** Mettre à jour un dossier médical existant (en supposant que le prénom et le nom ne changent pas).
  - **DELETE :** Supprimer un dossier médical (utiliser une combinaison de prénom et nom comme identificateur unique).
