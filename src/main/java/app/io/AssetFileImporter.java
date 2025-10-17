

package app.io;

import app.domain.Asset;
import app.domain.AssetStatus;
import app.validation.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssetFileImporter {

    public List<Asset> parseCsv(String path) throws IOException {
        List<Asset> assets = new ArrayList<>();
        Path file = Path.of(path);
        //path resolve + header: keep it simple and auto-close the file
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Ignore blank lines so users can space out their file
                if (line.isBlank()) continue;

                // Spilt the CSV so we check each section of var or strings to determine if they are valid or not
                String[] cols = line.split(",");
                if (cols.length < 8) {
                    System.out.println("Skipping bad line (" + lineNumber + "): not enough columns");
                    continue;
                }

                //scan each line to validate each field. if it dont match it will skip that row
                try {
                    String tag         = cols[0].trim();
                    String name        = cols[1].trim();
                    String category    = cols[2].trim();
                    LocalDate purchase = validators.parseDateFlexible(cols[3].trim());
                    BigDecimal cost    = validators.parseMoney(cols[4].trim());
                    AssetStatus status = validators.parseStatus(cols[5].trim());
                    boolean assigned   = validators.parseBooleanLoose(cols[6].trim());
                    LocalDate warranty = validators.parseDateFlexible(cols[7].trim());

                    Asset a = new Asset(tag, name, category, purchase, cost, status, assigned, warranty);
                    assets.add(a);
                    //These here are cathces to handle anything we didnt expect and keep moving
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping bad line (" + lineNumber + "): " + e.getMessage());
                } catch (Exception e) {

                    System.out.println("Skipping bad line (" + lineNumber + "): " + e);
                }
            }
        }

        return assets;
    }
}
