package app.service;

import app.domain.Asset;
import app.domain.AssetStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for importing {@link app.domain.Asset} data
 * from CSV files or CSV-formatted input.
 */
public class CSVAssetImporter {

    /**
     * Creates a new {@code CSVAssetImporter}.
     * <p>
     * This default constructor is used when importing assets from CSV data.
     */
    public CSVAssetImporter() {
        // no-op
    }

    /**
     * Import an CSV or TXT file and have display the results or kick back any errors
     * @param filePath is the string that contains the actual filepath to the CSV/TXT
     * @return the parsed assets that contained no errors or misinformation
     */
    public static List<Asset> importFromCSV(String filePath) {
        List<Asset> assets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Include empty fields

                if (parts.length < 8) continue;

                String tag = parts[0].trim();
                String name = parts[1].trim();
                String location = parts[2].trim();
                LocalDate purchaseDate = LocalDate.parse(parts[3].trim());
                BigDecimal unitCost = new BigDecimal(parts[4].trim());
                int quantity = Integer.parseInt(parts[5].trim());
                AssetStatus status = AssetStatus.valueOf(parts[6].trim().toUpperCase());
                boolean assigned = Boolean.parseBoolean(parts[7].trim());

                assets.add(new Asset(tag, name, location, purchaseDate, unitCost, quantity, status, assigned));
            }

        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        return assets;
    }
}
