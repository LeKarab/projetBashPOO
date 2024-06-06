package org.example;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {
    private WatchService watchService;
    private Path pathToWatch;

    // Constructeur pour initialiser le service de surveillance et le chemin à surveiller
    public FileWatcher(String pathToWatch) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.pathToWatch = Paths.get(pathToWatch);
        this.pathToWatch.register(watchService, ENTRY_CREATE);
    }

    // Méthode pour démarrer la surveillance des fichiers
    public void watch() throws IOException, InterruptedException {
        WatchKey key;
        // Boucle infinie ==> surveiller les événements de création
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                // Vérifier (si on a bien un fichier)
                if (event.kind() == ENTRY_CREATE) {
                    // Récupère le chemin
                    Path createdPath = ((WatchEvent<Path>) event).context();
                    System.out.println("File created: " + createdPath);
                }
            }
            // Réinitialiser clé pour continuer à recevoir des événements
            key.reset();
        }
    }

    // Méthode pour fermer le service
    public void close() throws IOException {
        watchService.close();
    }
}
