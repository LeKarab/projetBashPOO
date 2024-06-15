# FISA DE2 : GEORJON Rémy & FOND Romain

# Projet Batch Processing avec Java et PostgreSQL

## Description

Programme batch en Java qui :
- Scrute un dossier à la recherche de fichiers CSV nommés `users_<YYYYMMDDHHmmSS>.csv`.
- Parse les fichiers pour extraire les données.
- Peuple une base de données PostgreSQL avec les données extraites.
- Déplace les fichiers traités dans un autre dossier.

## Structure du Projet

- **Main.java** : Point d'entrée principal du programme.
- **BatchProcessor.java** : Logique principale pour le traitement des fichiers.
- **CsvParser.java** : Analyse les fichiers CSV.

## Configuration

### Base de Données PostgreSQL

- Si vous souhaitez modifier la configuration SQL, modifiez les variables suivantes dans le fichier `Main.java` :
```java
String dbUrl = "jdbc:postgresql://localhost:5432/projet";
String dbUser = "postgres";
String dbPass = "admin";
```

**Configuration actuelle** :  
Adresse du serveur : `localhost:5432`  
Base de données : `projet`  
Utilisateur : `postgres`  
Mot de passe : `admin`

- Dans votre base de données SQL, créez la table `remboursements` :
```sql
CREATE TABLE remboursements (
     id_remboursement VARCHAR(255) PRIMARY KEY,
     numero_securite_sociale VARCHAR(255),
     nom VARCHAR(255),
     prenom VARCHAR(255),
     date_naissance DATE,
     numero_telephone VARCHAR(255),
     e_mail VARCHAR(255),
     code_soin VARCHAR(255),
     montant_remboursement DECIMAL,
     timestamp_fichier VARCHAR(255)
);
```
### Dossiers de Fichiers CSV
**Configuration actuelle** :  
Dossier contenant les fichiers **CSV à traiter** : `src/main/resources/raw`  
Dossier où les **fichiers traités** seront déplacés : `src/main/resources/processed`

Si vous souhaitez modifier les dossiers de fichiers, modifiez les variables suivantes dans le fichier `Main.java` :
```java
String inputFolder = "src/main/resources/raw";
String processedFolder = "src/main/resources/processed";
```