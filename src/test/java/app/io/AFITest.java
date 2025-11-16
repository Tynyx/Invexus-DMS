package app.io;

import app.domain.Asset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AFITest {
    @TempDir Path tmp;

    @Test
    void parseCsv() throws IOException {
        Path csv = tmp.resolve("assets.csv");

        String content = String.join("\n",
                "LT001,Netgear Switch,Office,2020-01-12,79.99, 4, IN_STOCK,true",
                "LT002,PS5 Controller,Gaming,2025-02-10,65.43, 4, REPAIR,false,"
        );
        Files.writeString(csv, content);

        AssetFileImporter importer = new AssetFileImporter();
        List<Asset> assets = importer.parseCsv(csv.toString());
        assertEquals(2, assets.size());

    }

    @Test
    void badParseCsv() {
        AssetFileImporter importer = new AssetFileImporter();
        assertThrows(IOException.class, () -> importer.parseCsv("no/such/file.csv"));
    }

    @Test
    void skipBadLines() throws IOException {
        Path csv = tmp.resolve("mixed.csv");
        String content = String.join("\n",
                "LT001,Good,Office,2020-01-12,79.99, 4, IN_STOCK,true",
                "BAD LINE,MissingColumns,OnlyTwo", // too short → skipped
                "LT003,BadMoney,Office,2020-01-12,abc,IN_STOCK,true,2030-01-12", // invalid money → skipped
                "LT004,Good2,Office,2020-01-12,10.00, 1, IN_STOCK,false,"
        );
        Files.writeString(csv, content);

        AssetFileImporter importer = new AssetFileImporter();
        List<Asset> assets = importer.parseCsv(csv.toString());
        assertEquals(2, assets.size());
        assertTrue(assets.stream().anyMatch(a -> a.getAssetTag().equals("LT001")));
        assertTrue(assets.stream().anyMatch(a -> a.getAssetTag().equals("LT004")));
    }


}
