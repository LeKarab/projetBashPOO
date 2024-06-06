package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class BatchProcessor {

    // Méthode pour traiter les fichiers du dossier d'entrée
    public static void processFiles(String inputFolder, String processedFolder, String dbUrl, String dbUser, String dbPass) throws Exception {
        // Connexion à la base de données PostgreSQL
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

        // Liste les fichiers dans le dossier d'entrée
        Files.list(Paths.get(inputFolder))
                // Filtre les fichiers réguliers
                .filter(Files::isRegularFile)
                // Filtre les fichiers qui correspondent au format "users_<YYYYMMDDHHmmSS>.csv"
                .filter(path -> path.toString().matches(".*users_\\d{14}\\.csv"))
                // Pour chaque fichier correspondant, traiter le fichier et le déplacer dans le dossier traité
                .forEach(path -> {
                    try {
                        // Appel de la méthode processFile pour traiter le fichier
                        processFile(path, conn);
                        // Déplace le fichier traité vers le dossier des fichiers traités
                        Files.move(path, Paths.get(processedFolder, path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        // Affiche la pile d'erreurs en cas d'exception
                        e.printStackTrace();
                    }
                });
        // Ferme la connexion à la base de données
        conn.close();
    }

    // Méthode pour traiter un fichier spécifique
    public static void processFile(Path path, Connection conn) throws Exception {
        // Extrait le timestamp du nom du fichier
        String timestamp = path.getFileName().toString().substring(6, 20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date fileDate = dateFormat.parse(timestamp);

        // Lit le fichier ligne par ligne
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Sépare les valeurs de la ligne par des virgules
                String[] values = line.split(",");
                if (values.length == 9) {
                    // Trim les espaces autour de la date
                    String dateStr = values[3].trim();
                    // Vérifie que la date est dans le format "YYYY-MM-DD"
                    if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        System.err.println("Date format is incorrect for value: " + dateStr);
                        continue; // Passe à l'enregistrement suivant si la date est incorrecte
                    }

                    // Affiche les données de l'enregistrement en cours d'insertion
                    System.out.println("Inserting record: " + Arrays.toString(values));

                    // Requête SQL pour insérer ou MAJ un enregistrement dans la table "remboursements" (créer en amont sur DataGrip)
                    String sql = "INSERT INTO remboursements (numero_securite_sociale, nom, prenom, date_naissance, numero_telephone, e_mail, id_remboursement, code_soin, montant_remboursement, timestamp_fichier) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                            + "ON CONFLICT (id_remboursement) DO UPDATE SET "
                            + "numero_securite_sociale = EXCLUDED.numero_securite_sociale, nom = EXCLUDED.nom, prenom = EXCLUDED.prenom, "
                            + "date_naissance = EXCLUDED.date_naissance, numero_telephone = EXCLUDED.numero_telephone, e_mail = EXCLUDED.e_mail, "
                            + "code_soin = EXCLUDED.code_soin, montant_remboursement = EXCLUDED.montant_remboursement, timestamp_fichier = EXCLUDED.timestamp_fichier";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // Définit les valeurs des paramètres de la requête SQL
                        pstmt.setString(1, values[0].trim());
                        pstmt.setString(2, values[1].trim());
                        pstmt.setString(3, values[2].trim());
                        pstmt.setDate(4, java.sql.Date.valueOf(dateStr));
                        pstmt.setString(5, values[4].trim());
                        pstmt.setString(6, values[5].trim());
                        pstmt.setString(7, values[6].trim());
                        pstmt.setString(8, values[7].trim());
                        pstmt.setBigDecimal(9, new BigDecimal(values[8].trim()));
                        pstmt.setString(10, timestamp); // Stocke le timestamp comme chaîne de caractères
                        // Exécute la requête SQL et récupère le nombre de lignes affectées
                        int rowsAffected = pstmt.executeUpdate();
                        // Affiche le nombre de lignes affectées
                        System.out.println("Rows affected: " + rowsAffected);
                    } catch (SQLException e) {
                        // Affiche l'erreur SQL en cas d'exception
                        System.err.println("SQL Error: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        // Affiche l'erreur de parsing de la date en cas d'exception
                        System.err.println("Failed to parse date for record: " + Arrays.toString(values));
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
