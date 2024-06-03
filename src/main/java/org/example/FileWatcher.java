package org.example;

import java.nio.file.*;
import java.io.IOException;

public class FileWatcher {

    private Path folderPath;
    private Path processedFolderPath;

    public FileWatcher(String folderPath, String processedFolderPath) {
        this.folderPath = Paths.get(folderPath);
        this.processedFolderPath = Paths.get(processedFolderPath);
    }

    public void watchFolder() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path filePath = folderPath.resolve((Path) event.context());
                    if (filePath.toString().matches("users_\\d{14}\\.csv")) {
                        BatchProcessor.processFile(filePath.toString(), processedFolderPath.toString());
                    }
                }
            }
            key.reset();
        }
    }
}
