package org.example;

import java.io.*;
        import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static List<String[]> parseCsvFile(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(values);
            }
        }
        return records;
    }
}
