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
- **DatabaseManager.java** : Interagit avec la base de données.
- **FileWatcher.java** : Surveille le dossier de fichiers brutes.

## Configuration

**Base de Données PostgreSQL** :
   - Créez la base de données `projet` et la table `remboursements` :
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
