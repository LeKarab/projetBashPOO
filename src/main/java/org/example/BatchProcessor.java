package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;

public class BatchProcessor {

    public static void processFile(String filePath, String processedFolderPath) {
        try {
            List<String[]> records = CsvParser.parseCsvFile(filePath);
            String timestamp = extractTimestampFromFileName(filePath);
            DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/remboursements", "Postgres", "admin");
            dbManager.insertOrUpdateRecords(records, timestamp);
            moveFileToProcessedFolder(filePath, processedFolderPath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static String extractTimestampFromFileName(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        return fileName.substring(6, 20); // assuming the format is users_<YYYYMMDDHHmmSS>.csv
    }

    private static void moveFileToProcessedFolder(String filePath, String processedFolderPath) throws IOException {
        Path source = Paths.get(filePath);
        Path target = Paths.get(processedFolderPath).resolve(source.getFileName());
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
