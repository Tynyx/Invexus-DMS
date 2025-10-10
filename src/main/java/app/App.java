//LaTroy Richardson CEN-3024C -13950
//Software Development 1
//App class is the main class that will be the face of the app where it handles all the logic
// that is being manipulated from asset object and asset manager object
//This class needs to have all the CRUD features and listed in the SDLC and ensure the code
//follows the constraints that was listed as well

package app;

import app.domain.*;
import app.repository.*;
import app.service.*;
import app.io.AssetFileImporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class App {
    // This is main class that takes all the code and populate the results to CLI and GUI
    // for the users and does all the equations

    public static void main(String[] args) {
        // Setup users and managers
        Scanner input = new Scanner(System.in);
        AssetManager manager = new AssetManager(new InMemoryAssetRepository());
        AssetFileImporter importer = new AssetFileImporter();

        boolean running = true;
        while (running) {

            System.out.println("\n======== Invexus DMS =========");
            System.out.println("1. Add Asset");
            System.out.println("2. List All Assets");
            System.out.println("3. Upload Assets by CSV/TXT");
            System.out.println("4. Search by Tag");
            System.out.println("5. Update Asset");
            System.out.println("6. Delete Asset");
            System.out.println("7. Total Inventory Value");
            System.out.println("0. Exit");
            System.out.println("Enter your choice: ");


            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                // Add an asset to the Invexus DMS
                case 1:
                    System.out.println("Enter Asset Tag: ");
                    String tag = input.nextLine();

                    System.out.println("Enter Asset Name: ");
                    String name = input.nextLine();

                    System.out.println("Enter Category: ");
                    String category = input.nextLine();

                    System.out.println("Enter Unit Cost: ");
                    BigDecimal cost = input.nextBigDecimal();
                    input.nextLine();

                    System.out.println("Enter Purchase Year, Month, Day (YYYY-MM-DD): ");
                    String line = input.nextLine();
                    String[] parts = line.split("[^0-9]+");
                    int y = parseInt(parts[0]), m = parseInt(parts[1]), d = parseInt(parts[2]);
                    LocalDate purchaseDate = LocalDate.of(y, m, d);

                    System.out.println("Enter Warranty Year, Month, Day (YYYY-MM-DD): ");
                    line = input.nextLine();
                    String[] parts2 = line.split("[^0-9]+");
                    y = parseInt(parts2[0]);
                    m = parseInt(parts2[1]);
                    d = parseInt(parts2[2]);

                    LocalDate warrantyEnd = LocalDate.of(y, m, d);

                    Asset asset = new Asset(tag, name, category, purchaseDate, cost, AssetStatus.IN_STOCK, false, warrantyEnd);
                    boolean added = manager.add(asset);

                    if (added) {
                        System.out.println("Asset" + asset.getName() + " was added to Invexus DMS");
                    } else {
                        System.out.println("Asset not added.");
                    }
                    break;

                case 2:
                    // List all assets
                    manager.listAll().forEach(System.out::println);
                    break;

                case 3:
                    // Upload a File
                    System.out.println("Please enter the file path to the list of assets (please remove and quotation: ");
                    String path = input.nextLine();
                    if (path.isBlank()) {
                        System.out.println("Blank file path. try again.");
                        break;
                    }

                    try {
                        List<Asset> parsed = importer.parseCsv(path);

                        if (parsed.isEmpty()) {
                            System.out.println("No valid rows found in file.");
                            break;
                        }

                        // preview a few rows to satisfy 'display file contents'
                        System.out.println("\nPreview (up to 5 rows):");
                        parsed.stream().limit(5).forEach(System.out::println);
                        if (parsed.size() > 5) {
                            System.out.println("... (" + (parsed.size() - 5) + " more rows)");
                        }

                        // ask to import
                        System.out.println("\nImport these into the system? (Y/N)");
                        String yesNo = input.nextLine().trim().toLowerCase();
                        if (yesNo.equals("y")) {
                            int ported = manager.importAssets(parsed);
                            System.out.println("Imported " + ported + " of " + parsed.size() + "(duplicates skipped).");
                        } else {
                            System.out.println("Import canceled. Nothing was saved.");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid path: " + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + e.getMessage());
                    }


                    break;

                case 4:
                    // Search By Tag
                    System.out.println("Enter Asset Tag to search: ");
                    String findTag = input.nextLine();

                    Optional<Asset> found = manager.findByTag(findTag);
                    if (found.isPresent()) {
                        System.out.println("Asset Found: ");
                        System.out.println(found.get());
                    } else {
                        System.out.println("Asset not found.");
                    }


                    break;

                case 5:
                    // Update an Asset
                    System.out.println("Enter Asset Tag to update: ");
                    tag = input.nextLine();

                    Optional<Asset> existing = manager.findByTag(tag);
                    if(!existing.isPresent()) {
                        System.out.println("Asset not found.");
                        break;
                    }

                    Asset current = existing.get();

                    System.out.println("New Asset name [ " + current.getName() + "]: ");
                    String newName = input.nextLine().trim();
                    if (newName.isBlank()) newName = current.getName();

                    System.out.println("New Asset category [ " + current.getCategory() + "]: ");
                    String newCategory = input.nextLine().trim();
                    if (newCategory.isBlank()) newCategory = current.getCategory();

                    LocalDate newPurchaseDate;
                    while (true) {

                        System.out.println("New purchase date [ " + current.getPurchaseDate() + "]: ");
                        String pDateStr = input.nextLine().trim();
                        if (pDateStr.isBlank()) { newPurchaseDate = current.getPurchaseDate(); break; }
                        try {
                            newPurchaseDate = LocalDate.parse(pDateStr);
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid purchase date: " + pDateStr);
                        }
                    }

                    BigDecimal newCost;
                    while (true) {
                        System.out.println("New Asset Unit Cost [ " + current.getUnitCost() + "]: ");
                        String costStr = input.nextLine().trim();
                        if (costStr.isEmpty()) {
                            newCost = current.getUnitCost();
                            break;
                        }
                        try {
                            newCost = new BigDecimal(costStr);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Try again.");
                        }
                    }


                    // updating status
                    AssetStatus newStatus;
                    while (true) {
                        System.out.println("New Status (IN_STOCK / IN_USE / REPAIR / RETIRED) [" + current.getStatus() + "]" );
                        String statusStr = input.nextLine().trim();
                        if (statusStr.isEmpty()) { newStatus = AssetStatus.IN_STOCK; break; }
                        try {
                            newStatus = AssetStatus.valueOf(statusStr.toUpperCase());
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status. Try again.");
                        }
                    }

                    // updating warrantyEnd
                    LocalDate newWarrantyEnd;
                    while (true) {
                        System.out.println("New warranty end [ " + current.getWarrantyEnd() + "]" );
                        String endStr = input.nextLine().trim();
                        if (endStr.isEmpty()) { newWarrantyEnd = current.getWarrantyEnd(); break; }
                        try {
                            newWarrantyEnd = LocalDate.parse(endStr);
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid date. Try again.");
                        }
                    }

                    Asset updated = new Asset(
                            current.getAssetTag(),
                            newName,
                            newCategory,
                            newPurchaseDate,
                            newCost,
                            newStatus,
                            current.getAssigned(),
                            newWarrantyEnd
                    );

                    boolean success = manager.update(updated);
                    System.out.println( success ? "Asset updated" : "Asset not updated");


                    break;

                case 6:
                    // Delete
                    System.out.println("Enter Asset Tag to delete: ");
                    String tag2 = input.nextLine();
                    boolean removed = manager.delete(tag2);
                    System.out.println(removed ? "deleted" + tag2 : " Asset not found");
                    break;


                case 7:
                    // Custom Action Total Inventory Value
                    var total = manager.totalInventoryValue();
                    System.out.println("Total Inventory Value: $" + total.setScale(2, java.math.RoundingMode.HALF_UP));

                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting Invexus DMS...");



                default:
                    System.out.println("Invalid choice. Try again.");
                    break;

            }



        }
        input.close();





    }
}
