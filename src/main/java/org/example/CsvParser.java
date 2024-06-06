package org.example;

import java.io.*;
import java.util.*;

public class CsvParser {

    // Méthode pour analyser un fichier CSV et retourner une liste d'enregistrements
    public static List<String[]> parse(File file) throws IOException {
        List<String[]> records = new ArrayList<>();
        // BufferedReader pour lire fichier
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Sépare les valeurs de la ligne par des virgules
                String[] values = line.split(",");
                // Vérifie que la ligne contient exactement 9 valeurs
                if (values.length == 9) {
                    // Ajoute les valeurs à la liste des enregistrements
                    records.add(values);
                }
            }
        }
        // Retourne la liste des enregistrements
        return records;
    }
}
