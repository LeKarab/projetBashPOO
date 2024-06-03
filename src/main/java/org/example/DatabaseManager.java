package org.example;

import java.sql.*;
import java.util.List;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String dbUrl, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertOrUpdateRecords(List<String[]> records, String timestamp) throws SQLException {
        String insertSql = "INSERT INTO remboursements (id_remboursement, numero_securite_sociale, nom, prenom, date_naissance, numero_telephone, e_mail, code_soin, montant_remboursement, timestamp_fichier) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id_remboursement) DO UPDATE SET " +
                "numero_securite_sociale = EXCLUDED.numero_securite_sociale, " +
                "nom = EXCLUDED.nom, " +
                "prenom = EXCLUDED.prenom, " +
                "date_naissance = EXCLUDED.date_naissance, " +
                "numero_telephone = EXCLUDED.numero_telephone, " +
                "e_mail = EXCLUDED.e_mail, " +
                "code_soin = EXCLUDED.code_soin, " +
                "montant_remboursement = EXCLUDED.montant_remboursement, " +
                "timestamp_fichier = EXCLUDED.timestamp_fichier;";

        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (String[] record : records) {
                ps.setString(1, record[6]); // id_remboursement
                ps.setString(2, record[0]); // numero_securite_sociale
                ps.setString(3, record[1]); // nom
                ps.setString(4, record[2]); // prenom
                ps.setString(5, record[3]); // date_naissance
                ps.setString(6, record[4]); // numero_telephone
                ps.setString(7, record[5]); // e_mail
                ps.setString(8, record[7]); // code_soin
                ps.setString(9, record[8]); // montant_remboursement
                ps.setString(10, timestamp); // timestamp_fichier
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
