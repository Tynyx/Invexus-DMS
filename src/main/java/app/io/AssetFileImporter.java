/**
 * Invexus DMS - <ClassName>
 * Author: LaTroy Richardson (CEN-3024C)
 * Purpose: This is the brains behind the file import to make an easy flow for the Assetmanager and CLi to use
 * Notes: Phase 1 - CLI, in-memory repository, file import (CSV/TXT).
 */


package app.io;

import app.domain.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AssetFileImporter {

    public List<Asset> parseCsv(String filePath) throws IOException {
        List<Asset> assets = new ArrayList<>();
       if (filePath == null || filePath.isBlank()){
           throw new IllegalArgumentException("File path is null or empty");
       }
       try (Stream<String> lines = Files.lines(Path.of(filePath))) {
           int[] counter = {0};

           lines.forEach(line -> {
               counter[0]++;
               if (counter[0] == 1 || line.isBlank()) return;


               String[] cols = line.split(",");
               if(cols.length <8){
                   System.out.println("Skipping bad line (" + counter[0] + "): not enough columns");
                   return;
               }

               try {

                       Asset a = new Asset(
                               cols[0].trim(),
                               cols[1].trim(),
                               cols[2].trim(),
                               LocalDate.parse(cols[3].trim()),
                               new BigDecimal(cols[4].trim()),
                               AssetStatus.valueOf(cols[5].trim().toUpperCase()),
                               Boolean.parseBoolean(cols[6].trim()),
                               LocalDate.parse(cols[7].trim())
                       );
                       assets.add(a);

               } catch (Exception e) {
                   System.out.println("Skipping bad line (" + counter[0] + "): " + e.getMessage());
               }
           });
       }
        return assets;
    }
}
