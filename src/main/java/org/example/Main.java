package org.example;

public class Main {
    public static void main(String[] args) {
        String inputFolder = "C:\\Users\\georj\\Dropbox\\Mon PC (LAPTOP-D1NNT2HT)\\Documents\\FISA 2\\4_DEV\\POO\\projetBashPOO\\src\\main\\resources\\raw";
        String processedFolder = "C:\\Users\\georj\\Dropbox\\Mon PC (LAPTOP-D1NNT2HT)\\Documents\\FISA 2\\4_DEV\\POO\\projetBashPOO\\src\\main\\resources\\processed";
        String dbUrl = "jdbc:postgresql://localhost:5432/projet";
        String dbUser = "postgres";
        String dbPass = "admin";

        try {
            BatchProcessor.processFiles(inputFolder, processedFolder, dbUrl, dbUser, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
