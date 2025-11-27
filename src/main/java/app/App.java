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
import app.validation.validators;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


/**
 * This class is the app for the Jar files and for the terminal what would populate
 * if you are only using the CLI view
 */
public class App {
    // This is main class that takes all the code and populate the results to CLI and GUI
    // for the users and does all the equations

    /**
     * Creates a new {@code App} instance.
     * <p>
     * This constructor exists so the default constructor has documentation
     * for Javadoc.
     */
    public App() {
        // no-op
    }

    /**
     * the main class is the class that holds the logic and Switch method for the CLI
     * to manage different inputs from the user
     * @param args are not meant to be used in this class
     */
    public static void main(String[] args) {
        // Setup users and managers
        Scanner input = new Scanner(System.in);
        AssetManager manager = new AssetManager(new InMemoryAssetRepository());
        AssetFileImporter importer = new AssetFileImporter();


        List<Asset> importedAssets = CSVAssetImporter.importFromCSV("src/main/resources/sample_assets.csv");

        for (Asset a : importedAssets) {
            manager.add(a);
        }






        boolean running = true;
        while (running) {

            System.out.println("\n======== Invexus DMS =========");
            System.out.println("1. Add Asset");
            System.out.println("2. List All Assets");
            System.out.println("3. Upload Assets by CSV/TXT");
            System.out.println("4. Search Asset by Tag");
            System.out.println("5. Update Asset");
            System.out.println("6. Delete Asset");
            System.out.println("7. Total Inventory Value");
            System.out.println("0. Exit");
            System.out.println("Enter your choice: ");


            int choice;
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please choose between 0 and 7 ");
                continue;
            }


            switch (choice) {
                // Add an asset to the Invexus DMS
                case 1:
                    System.out.println("Enter Asset Tag: ");
                    String tag = input.nextLine().trim();

                    System.out.println("Enter Asset Name: ");
                    String name = input.nextLine().trim();

                    System.out.println("Enter location: ");
                    String location = input.nextLine().trim();

                    // Cost being handled via the validators
                    BigDecimal cost;
                    while(true) {
                        System.out.println("Unit Cost: ");
                        try {
                            cost = validators.parseMoney(input.nextLine().trim());
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }


                    //dates being handled with validators
                    LocalDate purchaseDate;
                    while(true) {
                        System.out.println("Purchase Date (YYYY-MM-DD): ");
                        try {
                            purchaseDate = validators.parseDateFlexible(input.nextLine().trim());
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    int quantity;
                    while(true) {
                        System.out.println("Quantity: ");
                        try {
                            quantity = validators.parseInt(input.nextLine().trim());
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }


                    // Status with validators
                    AssetStatus status;
                    while(true) {
                        System.out.println("Status (IN_STOCK / ASSIGNED / REPAIR / RETIRED): ");
                        try {
                            status = validators.parseStatus(input.nextLine().trim());
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    // Assigned (Optional)
                    System.out.print("Assigned? (y/n): ");
                    boolean assigned = validators.parseBooleanLoose(input.nextLine().trim());

                    Asset asset = new Asset(tag, name, location, purchaseDate, cost, quantity, status, assigned);
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
                    System.out.println("Please enter the file path " +
                            " (please remove and quotation): ");
                    String path = input.nextLine().trim();
                    if (path.isBlank()) { System.out.println("Blank file path."); break; }
                    try {
                        List<Asset> parsed = importer.parseCsv(path);
                        if (parsed.isEmpty()) {
                            System.out.println("No valid rows found.");
                            break;
                        }

                        parsed.stream().limit(5).forEach(System.out::println);
                        int ported = manager.importAssets(parsed);
                        System.out.println("Imported" + ported + " of " + parsed.size() + " (duplicates skipped).");
                    } catch (Exception e) {
                        System.out.println("Import failed: " + e.getMessage());
                    }
                    break;


                case 4:
                    // Search By Asset Tag
                    System.out.println("Enter Asset Tag to search: ");
                    String aTag = input.nextLine().trim();
                    Optional<Asset> res = manager.findByTag(aTag);
                    if (res.isPresent()) {
                        System.out.println(res.get());
                    } else {
                        System.out.println("Asset not found.");
                    }

                    break;

                case 5:
                    System.out.print("Enter Asset Tag to update: ");
                    String aTag2 = input.nextLine().trim();
                    Optional<Asset> exisitingOpt = manager.findByTag(aTag2);
                    if (!exisitingOpt.isPresent()) {
                        System.out.println("Asset not found."); break;
                    }
                    Asset current = exisitingOpt.get();

                    System.out.print("New Name [" + current.getName() + "]: ");
                    String newName = input.nextLine().trim();
                    if (newName.isEmpty()) newName = current.getName();

                    System.out.print(" New location [" + current.getLocation() + "]: ");
                    String NewLocation = input.nextLine().trim();
                    if (NewLocation.isEmpty()) NewLocation = current.getLocation();

                    BigDecimal newCost = current.getUnitCost();
                    while (true) {
                        System.out.print("New cost {" + current.getUnitCost() + "}: ");
                        String s = input.nextLine().trim();
                        if (s.isEmpty()) break;
                        try { newCost = validators.parseMoney(s); break; }
                        catch (IllegalArgumentException ex) {System.out.println(ex.getMessage());}
                    }

                    int newQuantity = current.getQuantity();   // keep existing as default

                    while (true) {
                        System.out.print("New quantity [" + newQuantity + "]: ");
                        String s = input.nextLine().trim();

                        // Empty input = keep current quantity
                        if (s.isEmpty()) {
                            break;
                        }

                        try {
                            int q = Integer.parseInt(s);   // or validators.parseQuantity(s) if you have one
                            if (q < 0) {
                                System.out.println("Quantity must be zero or positive.");
                                continue;
                            }
                            newQuantity = q;
                            break;                         // valid, exit loop
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid quantity. Please enter a whole number.");
                        }
                    }

                    current.setQuantity(newQuantity);

                    String relocation = current.getLocation();
                    while (true) {
                        System.out.print("New location [" + relocation + "]: ");
                        String s = input.nextLine().trim();
                        if (s.isEmpty()) break;
                    }

                    LocalDate newPurchase = current.getPurchaseDate();
                    while (true) {
                        System.out.print("New purchase date (YYYY-MM-DD) [" + current.getPurchaseDate() + "]: ");
                        String s = input.nextLine().trim();
                        if (s.isEmpty()) break;
                        try { newPurchase = validators.parseDateFlexible(s); break; }
                        catch (IllegalArgumentException ex) {System.out.println(ex.getMessage());}

                    }



                    AssetStatus newStatus = current.getStatus();
                    while (true) {
                        System.out.print("New status (IN_STOCK / ASSIGNED / REPAIR / RETIRED) [" + current.getStatus() + "]: ");
                        String s = input.nextLine().trim();
                        if (s.isEmpty()) break;
                        try { newStatus = validators.parseStatus(s); break; }
                        catch (IllegalArgumentException ex) {System.out.println(ex.getMessage());}
                    }

                    Asset updated = new Asset(
                            current.getAssetTag(),
                            newName,
                            relocation,
                            newPurchase,
                            newCost,
                            newQuantity,
                            newStatus,
                            current.isAssigned()
                    );
                    boolean ok = manager.update(updated);
                    System.out.println(ok ? "Asset updated successfully" : "Asset update failed.");
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

                    break;



                default:
                    System.out.println("Invalid choice. Try again.");
                    break;

            }



        }
        input.close();





    }




}
