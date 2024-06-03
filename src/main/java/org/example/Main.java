package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("On est de retour en Ligue 1");

        String rawFolderPath = "src/main/resources/raw";
        String processedFolderPath = "src/main/resources/processed";

        FileWatcher fileWatcher = new FileWatcher(rawFolderPath, processedFolderPath);
        try {
            fileWatcher.watchFolder();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}