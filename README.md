# Invexus DMS – Phase 1: Logic and Data Validation

### 📦 Overview
**Invexus DMS (Data Management System)** is a console-based Java application designed to manage company assets and demonstrate proper CRUD functionality, file parsing, and input validation.  
This project was developed as part of the *Phase 1 Implementation* requirement for the **Valencia College BAS in Computing Technology – Software Development** coursework.

---

### 🧠 Phase 1 Goal
Implement the **core logic and data validation** for all CRUD operations using Java objects and text file input/output (no database or GUI yet).

---

### ⚙️ Features Implemented
| Feature | Description |
|----------|--------------|
| **Create** | Add a new asset through the CLI with full validation. |
| **Read** | List all assets and import asset lists from `.txt` files. |
| **Update** | Modify any field of an existing asset with validation checks. |
| **Delete** | Remove an asset safely by its unique tag. |
| **Custom Action** | Calculates and displays the total inventory value of all stored assets. |
| **Validation** | Ensures no invalid data, malformed input, or duplicate tags crash the system. |

---

### 🗂️ Project Structure
src/
├── app/
│ ├── App.java # Main CLI entry point
│ ├── io/
│ │ └── AssetFileImporter.java
│ ├── service/
│ │ └── AssetManager.java
│ ├── repository/
│ │ ├── AssetRepository.java
│ │ └── InMemoryAssetRepository.java
│ ├── domain/
│ │ ├── Asset.java
│ │ └── AssetStatus.java
│ └── validation/
│ └── Validators.java
└── assets/
├── goodAssets.txt
└── badAssets.txt


---

### 🧩 How to Run
**Option 1: From IntelliJ / IDE**
1. Open the project.
2. Run the main class: `app.App`.

**Option 2: From Terminal**
1. Build the JAR file:  
   `Build → Build Artifacts → Invexus_DMS_jar → Build`
2. Navigate to the JAR folder:  
   `cd out/artifacts/Invexus_DMS_jar`
3. Run the program:  
   ```bash
   java -jar Invexus_DMS.jar

🧮 Custom Action

Total Inventory Value – Summation of all asset costs currently stored in the system.

🧰 Technologies Used

Java 17+

IntelliJ IDEA

Command-Line Interface (CLI)

java.time, BigDecimal, Collections, and Streams


👨🏽‍💻 Author

LaTroy Richardson (Tynyx / NyameDrift)
📍 Montevideo, Uruguay | Orlando, Florida
🎓 BAS – Computing Technology & Software Development
🔗 GitHub: github.com/latro-richardson
