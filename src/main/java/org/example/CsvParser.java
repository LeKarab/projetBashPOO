package org.example;

import java.io.*;
import java.util.*;

public class CsvParser {
    public static List<String[]> parse(File file) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 9) {
                    records.add(values);
                }
            }
        }
        return records;
    }
}
