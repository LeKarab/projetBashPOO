package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class BatchProcessor {
    public static void processFiles(String inputFolder, String processedFolder, String dbUrl, String dbUser, String dbPass) throws Exception {
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        Files.list(Paths.get(inputFolder))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().matches(".*users_\\d{14}\\.csv"))
                .forEach(path -> {
                    try {
                        processFile(path, conn);
                        Files.move(path, Paths.get(processedFolder, path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        conn.close();
    }

    public static void processFile(Path path, Connection conn) throws Exception {
        String timestamp = path.getFileName().toString().substring(6, 20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date fileDate = dateFormat.parse(timestamp);

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 9) {
                    String dateStr = values[3].trim(); // Trim whitespace around date
                    if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        System.err.println("Date format is incorrect for value: " + dateStr);
                        continue; // Skip this record
                    }

                    System.out.println("Inserting record: " + Arrays.toString(values));

                    String sql = "INSERT INTO remboursements (numero_securite_sociale, nom, prenom, date_naissance, numero_telephone, e_mail, id_remboursement, code_soin, montant_remboursement, timestamp_fichier) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                            + "ON CONFLICT (id_remboursement) DO UPDATE SET "
                            + "numero_securite_sociale = EXCLUDED.numero_securite_sociale, nom = EXCLUDED.nom, prenom = EXCLUDED.prenom, "
                            + "date_naissance = EXCLUDED.date_naissance, numero_telephone = EXCLUDED.numero_telephone, e_mail = EXCLUDED.e_mail, "
                            + "code_soin = EXCLUDED.code_soin, montant_remboursement = EXCLUDED.montant_remboursement, timestamp_fichier = EXCLUDED.timestamp_fichier";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, values[0].trim());
                        pstmt.setString(2, values[1].trim());
                        pstmt.setString(3, values[2].trim());
                        pstmt.setDate(4, java.sql.Date.valueOf(dateStr));
                        pstmt.setString(5, values[4].trim());
                        pstmt.setString(6, values[5].trim());
                        pstmt.setString(7, values[6].trim());
                        pstmt.setString(8, values[7].trim());
                        pstmt.setBigDecimal(9, new BigDecimal(values[8].trim()));
                        pstmt.setString(10, timestamp); // Store timestamp as string
                        int rowsAffected = pstmt.executeUpdate();
                        System.out.println("Rows affected: " + rowsAffected);
                    } catch (SQLException e) {
                        System.err.println("SQL Error: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Failed to parse date for record: " + Arrays.toString(values));
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
