# Invexus DMS

**Team and Roles:**
* LaTroy Richardson Sr - Lead Developer and Project Documentation Manager


**Invexus DMS** – Track Smarter. Manage Better.

Invexus DMS (Data Management System) is a Java / JavaFX desktop application for managing business and IT assets such as laptops, monitors, servers, and peripherals. It supports in-memory storage for quick testing and optional MySQL persistence for real-world scenarios.



## Design

- [Architecture Overview](docs/Architecture.md)
- [UI & Flow](docs/UI.md)
- [Data Model](docs/DataModel.md)
- [Test Plan](docs/TestPlan.md)

> _Note:_ These docs reflect the overall system design, including the CLI → GUI evolution, layered architecture, and testing strategy.



## Built With

<a href="https://www.java.com/">
  <img src="https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg" alt="Java" width="120"/>
</a>

<a href="https://openjfx.io/">
  <img src="https://upload.wikimedia.org/wikipedia/en/f/fd/JavaFX_Logo.png" alt="JavaFX" width="150"/>
</a>

<a href="https://maven.apache.org/">
  <img src="https://maven.apache.org/images/maven-logo-black-on-white.png" alt="Maven" width="200"/>
</a>

<a href="https://www.mysql.com/">
  <img src="https://www.mysql.com/common/logos/logo-mysql-170x115.png" alt="MySQL" width="150"/>
</a>

<a href="https://junit.org/">
  <img src="https://junit.org/junit5/assets/img/junit5-logo.png" alt="JUnit" width="150"/>
</a>



## Intended Users

Invexus DMS is designed for:

* **Small IT Teams / Help Desks:** Track laptops, monitors, docks, and other hardware assigned to employees or workstations.
* **Computer Labs & Training Rooms:** Keep up with shared equipment, where it is, and whether it’s in use or in repair.
* **Small Businesses & Home Offices:** Maintain a lightweight inventory of assets without needing a full-blown enterprise system.

The goal is to provide a **simple, focused** inventory and asset tracker that can grow from a classroom project into a practical, real-world tool.



## Functionality

* **Asset Management (CRUD)**
   * Add new assets with fields:
      * `assetTag` (unique ID)
      * `name`
      * `location`
      * `purchaseDate`
      * `unitCost`
      * `quantity`
      * `status` (`IN_STOCK`, `ASSIGNED`, `REPAIR`, `RETIRED`)
      * `assigned` (true/false)
   * Edit existing assets and save changes back to the repository
   * Delete assets by tag

* **JavaFX UI**
   * Table view showing all assets
   * Location displayed alongside other core attributes
   * Add / Edit / Delete buttons
   * Filters by status and tag/search text
   * UI refreshes automatically after every change

* **CSV / File Import**
   * Import assets from CSV or text files
   * Validates:
      * Money format (`unitCost`)
      * Flexible date formats (`purchaseDate`)
      * Quantity ranges
      * Status values (mapping to `AssetStatus`)
      * Boolean values for `assigned` (supports yes/no, y/n, true/false)
   * Skips duplicates based on `assetTag` instead of crashing

* **Reporting / Metrics**
   * Calculate **total inventory value** across all assets (`unitCost * quantity`)

* **Multiple Storage Options**
   * In-memory repository (`InMemoryAssetRepository`) for quick testing and demos
   * MySQL-backed repository (`ARepoMySQL`) for persistent storage



## Install Extensions (Optional but Helpful)

If you are working in VS Code or IntelliJ, the following extensions / tools are recommended:

-   Checkstyle or similar Java linter/formatter
-   Lombok plugin (if you extend the project to use Lombok)
-   Java and JavaFX support plugins for your IDE



## Java & Maven Setup (Local Development)

Make sure you have:

- Java JDK 17 or higher
- Maven 3.x

From the project root:

```bash
mvn -version
java -version
