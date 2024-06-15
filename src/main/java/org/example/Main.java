package org.example;

public class Main {
    public static void main(String[] args) {
        // Chemin du dossier où se trouvent les fichiers CSV à traiter
        String inputFolder = "src/main/resources/raw";
        // Chemin du dossier où les fichiers traités seront déplacés
        String processedFolder = "src/main/resources/processed";

        // Paramètres de connexion à la base de données PostgreSQL
        String dbUrl = "jdbc:postgresql://localhost:5432/projet";
        String dbUser = "postgres";
        String dbPass = "admin";

        try {
            // Appel de la méthode processFiles de la classe BatchProcessor pour lancer le traitement des fichiers CSV
            BatchProcessor.processFiles(inputFolder, processedFolder, dbUrl, dbUser, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
