package org.example;

public class Main {
    public static void main(String[] args) {
        // Chemin du dossier où se trouvent les fichiers CSV à traiter
        String inputFolder = "C:\\Users\\georj\\Dropbox\\Mon PC (LAPTOP-D1NNT2HT)\\Documents\\FISA 2\\4_DEV\\POO\\projetBashPOO\\src\\main\\resources\\raw";
        // Chemin du dossier où les fichiers traités seront déplacés
        String processedFolder = "C:\\Users\\georj\\Dropbox\\Mon PC (LAPTOP-D1NNT2HT)\\Documents\\FISA 2\\4_DEV\\POO\\projetBashPOO\\src\\main\\resources\\processed";
        String dbUrl = "jdbc:postgresql://localhost:5432/projet";
        String dbUser = "postgres";
        String dbPass = "admin";

        try {
            // Appel de la méthode processFiles de la classe BatchProcessor pour traiter les fichiers CSV
            BatchProcessor.processFiles(inputFolder, processedFolder, dbUrl, dbUser, dbPass);
        } catch (Exception e) {
            // Affichage de la pile d'erreurs en cas d'exception
            e.printStackTrace();
        }
    }
}
