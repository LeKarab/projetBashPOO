package org.example;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {
    private WatchService watchService;
    private Path pathToWatch;

    public FileWatcher(String pathToWatch) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.pathToWatch = Paths.get(pathToWatch);
        this.pathToWatch.register(watchService, ENTRY_CREATE);
    }

    public void watch() throws IOException, InterruptedException {
        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == ENTRY_CREATE) {
                    Path createdPath = ((WatchEvent<Path>) event).context();
                    System.out.println("File created: " + createdPath);
                    // Trigger batch processing or other actions here
                }
            }
            key.reset();
        }
    }

    public void close() throws IOException {
        watchService.close();
    }
}
