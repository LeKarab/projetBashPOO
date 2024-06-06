package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DatabaseManager {
    private Connection conn;

    // Constructeur pour initialiser la connexion à la base de données
    public DatabaseManager(String dbUrl, String user, String pass) throws SQLException {
        this.conn = DriverManager.getConnection(dbUrl, user, pass);
    }

    // Méthode pour insérer ou MAJ des enregistrements dans la table "users"
    public void insertOrUpdateRecords(List<String[]> records, Timestamp fileTimestamp) throws SQLException {
        // Requête SQL pour insérer ou MAJ un enregistrement dans la table "users"
        String sql = "INSERT INTO users (Numero_Securite_Sociale, Nom, Prenom, Date_Naissance, Numero_Telephone, E_Mail, ID_Remboursement, Code_Soin, Montant_Remboursement, Timestamp_fichier) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (ID_Remboursement) DO UPDATE SET "
                + "Numero_Securite_Sociale = EXCLUDED.Numero_Securite_Sociale, Nom = EXCLUDED.Nom, Prenom = EXCLUDED.Prenom, "
                + "Date_Naissance = EXCLUDED.Date_Naissance, Numero_Telephone = EXCLUDED.Numero_Telephone, E_Mail = EXCLUDED.E_Mail, "
                + "Code_Soin = EXCLUDED.Code_Soin, Montant_Remboursement = EXCLUDED.Montant_Remboursement, Timestamp_fichier = EXCLUDED.Timestamp_fichier";

        // Préparer SQL
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                // Définit valeurs des paramètres de la req. SQL pour chaque enregistrement
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);
                pstmt.setString(3, record[2]);
                pstmt.setDate(4, java.sql.Date.valueOf(record[3]));
                pstmt.setString(5, record[4]);
                pstmt.setString(6, record[5]);
                pstmt.setInt(7, Integer.parseInt(record[6]));
                pstmt.setInt(8, Integer.parseInt(record[7]));
                pstmt.setBigDecimal(9, new BigDecimal(record[8]));
                pstmt.setTimestamp(10, fileTimestamp);
                // Ajoute la requête à un lot
                pstmt.addBatch();
            }
            // Exécute toutes les requêtes du lot
            pstmt.executeBatch();
        }
    }

    // Méthode pour fermer la connexion à la BDD
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
