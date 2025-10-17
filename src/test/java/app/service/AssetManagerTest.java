// LaTroy

// this is a test class for the class Assetmanager it will test all the methods and calls inside the Assetmanager
// And it will also test the CRUD Logic here and the custom action we implemented
package app.service;

import app.domain.Asset;
import app.domain.AssetStatus;
import app.repository.InMemoryAssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//Create the class to handle all the CRUD Test
class AssetManagerTest {

    // call upon the repository to use the asset object
    private InMemoryAssetRepository repo;
    private AssetManager manager;

    @BeforeEach
    void setUp() {
        repo = new InMemoryAssetRepository();
        manager = new AssetManager(repo);
    }

    // we are going to create a make method to produce and asset we can call upon when we need and not have to generate
    // multiple assets for every method
    private Asset make(String tag) {
        return new Asset(tag, "Netgear Switch", "Office Network",
                LocalDate.of(2025, 8, 20),
                new BigDecimal("79.99"),
                AssetStatus.IN_STOCK, true, LocalDate.of(2030, 8, 19)
        );
    }

    // This method is to test the Create/add asset and ensures it saves the data
    @Test
    void addAsset() {
        Asset a = make("LT001");
        // now we add the new asset to the data bucket
        assertTrue(manager.add(a));

        // Assert now we test to see if it actually added the asset
        assertTrue(manager.findByTag("LT001").isPresent());
    }

    @Test
    void noDuplicate() {
        // We are making sure the system won't allow duplicate IDs as part of our criteria
        Asset first = make("LT001");
        Asset second = make("LT001");
        assertTrue(manager.add(first));
        assertFalse(manager.add(second)); // this should return false since its a duplicate

        // This is to test to make sure there is only one asset with the tag "LT001"
        long count = manager.listAll().stream().filter(x -> x.getAssetTag().equals("LT001")).count();
        assertEquals(1, count);

    }

    @Test
    void updateAsset() {
        // now we will make and asset and update its information can confirm the update
        Asset a = make("LT001");
        manager.add(a);

        Asset updated = new Asset(
                "LT001",
                "PS5 Controller",
                "Gaming",
                LocalDate.of(2025, 2, 10),
                new BigDecimal("65.43"),
                AssetStatus.ASSIGNED,
                true,
                LocalDate.of(2035, 9, 10)
        );

        //now we test to make sure it actually updated
        assertTrue(manager.update(updated));

        // Test to confirm every part was correctly changed
        Asset after = manager.findByTag("LT001").orElseThrow();
        assertEquals("PS5 Controller", after.getName());
        assertEquals("Gaming", after.getCategory());
        assertEquals(LocalDate.of(2025, 2, 10), after.getPurchaseDate());
        assertEquals(0, new BigDecimal("65.43").compareTo(after.getUnitCost()));
        assertEquals(AssetStatus.ASSIGNED, after.getStatus());
        assertTrue(after.getAssigned());
        assertEquals(LocalDate.of(2035, 9, 10), after.getWarrantyEnd());
    }

    // This test is to confirm that if the asset don't exist it won't update
    @Test
    void updateMissingFalse(){
        Asset ghost = make("Nope");
        assertFalse(manager.update(ghost));
    }

    // This is testing the delete method to ensure the assets is deleted properly
    @Test
    void deleteAsset() {
        manager.add(make("LT001"));
        assertTrue(manager.delete("LT001"));
        assertTrue(manager.findByTag("LT001").isEmpty());
    }
    //This test to make sure we cant delete anything that dont exist
    @Test
    void deleteMissingFalse() {
        assertFalse(manager.delete("Nope"));
        assertFalse(manager.delete(""));
        assertFalse(manager.delete("  "));
        assertFalse(manager.delete(null));
    }

    @Test
    void getAllAssets() {
        // We are testing to make sure all assets populate when the method listall is called
        manager.add(make("LT001"));
        manager.add(make("LT002"));
        manager.add(make("LT003"));
        manager.add(make("LT004"));

        // We test to make sure that all assets actual are in the database
        var all = manager.listAll();
        assertEquals(4, all.size());
        assertTrue(all.stream().anyMatch(x -> x.getAssetTag().equals("LT001")));
        assertTrue(all.stream().anyMatch(x -> x.getAssetTag().equals("LT002")));
        assertTrue(all.stream().anyMatch(x -> x.getAssetTag().equals("LT003")));
        assertTrue(all.stream().anyMatch(x -> x.getAssetTag().equals("LT004")));
    }

    @Test
    void getTotalAssetsCost() {
        // we are testing the custom method to add all the values of all assets so we are going to add a few assets and
        // test to make sure the total value is calculated properly
        manager.add(new Asset("A1","A","x", LocalDate.of(2020,1,1),
                new BigDecimal("79.99"), AssetStatus.IN_STOCK,
                true, LocalDate.of(2030,1,1)));
        manager.add(new Asset("A2","B","x", LocalDate.of(2020,1,1),
                new BigDecimal("65.43"), AssetStatus.REPAIR,
                false, LocalDate.of(2030,1,1)));
        manager.add(new Asset("A3","C","x", LocalDate.of(2020,1,1),
                new BigDecimal("65.43"), AssetStatus.REPAIR,
                false, LocalDate.of(2030,1,1)));
        manager.add(new Asset("A4","D", "x", LocalDate.of(2025,5,30),
                new BigDecimal("1200.32"), AssetStatus.REPAIR,
                false, LocalDate.of(2030,1,1)));

        // Test to make sure the total value actually equals the total value
        var total = manager.totalInventoryValue();
        assertEquals(0, new BigDecimal("1411.17").compareTo(total));





    }
}