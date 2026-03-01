# Invexus DMS

**Team and Roles:**
- LaTroy Richardson Sr — Lead Developer & Project Documentation Manager

---

## 🚀 Build Status (CI/CD)

Invexus DMS uses a **Jenkins CI pipeline running in Docker** to automate building, testing, and packaging.

### ✔ Pipeline Features
- Automated Maven builds (`mvn clean test`)
- JUnit test reporting
- Artifact packaging
- Continuous integration from GitHub commits

> 📸 Add your screenshot here:
> `assets/jenkins-build-success.png`

---

## 🖥️ Invexus DMS — Track Smarter. Manage Better.

Invexus DMS (Data Management System) is a **Java / JavaFX desktop application** for managing business and IT assets such as laptops, monitors, servers, and peripherals.

It supports:

- ⚡ In-memory storage for quick testing
- 💾 MySQL persistence for real-world scenarios
- 🧪 Automated testing and CI pipeline integration

---

## 📐 Design Documentation

- [Architecture Overview](Docs/Architecture.md)
- [UI & Flow](Docs/UI.md)
- [Data Model](Docs/DataModel.md)
- [Test Plan](Docs/TestPlan.md)

> These documents reflect the full system lifecycle, including CLI → GUI evolution, layered architecture, and testing strategy.

---

## 🛠️ Built With

<a href="https://www.jenkins.io">
    <img src="https://devtools.in/wp-content/uploads/2024/03/image-2.png" alt="jenkins" width="100">
</a>

<a href="https://www.java.com/">
  <img src="https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg" alt="Java" width="50"/>
</a>

<a href="https://openjfx.io/">
  <img src="https://codigojava.online/wp-content/uploads/2023/02/JavaFX.png" alt="JavaFX" width="100"/>
</a>

<a href="https://maven.apache.org/">
  <img src="https://maven.apache.org/images/maven-logo-black-on-white.png" alt="Maven" width="100"/>
</a>

<a href="https://www.mysql.com/">
  <img src="https://www.mysql.com/common/logos/logo-mysql-170x115.png" alt="MySQL" width="100"/>
</a>

<a href="https://junit.org/">
  <img src="https://junit.org/junit5/assets/img/junit5-logo.png" alt="JUnit" width="50"/>
</a>

<a href="https://www.Docker.com">
    <img src="https://images.icon-icons.com/2415/PNG/512/docker_original_logo_icon_146556.png"alt="Docker" width="100"/>
</a>


---

## 👥 Intended Users

### 💻 Small IT Teams / Help Desks
Track laptops, monitors, peripherals, and employee assignments.

### 🏫 Computer Labs / Training Rooms
Manage shared equipment and availability status.

### 🏢 Small Businesses / Home Offices
Maintain lightweight inventory without enterprise complexity.

---

## ⚙️ Core Functionality

### Asset Management (CRUD)
- Add, edit, and delete assets
- Unique asset tagging system
- Status tracking (`IN_STOCK`, `ASSIGNED`, `REPAIR`, `RETIRED`)
- Assignment tracking

---

### JavaFX User Interface
- Interactive TableView display
- Real-time filtering by tag/status
- Automatic UI refresh after changes

---

### CSV / File Import
- Import assets from CSV or text files
- Data validation:
    - Currency formatting
    - Flexible date parsing
    - Status mapping
    - Boolean value interpretation
- Duplicate asset detection

---

### Reporting Features
- Calculate total inventory value
- Quantity-based cost calculations

---

### Storage Options
- In-memory repository for testing
- MySQL-backed repository for persistence

---

## 🔄 CI/CD Pipeline (Jenkins)

This project demonstrates a full **continuous integration workflow**.

### Pipeline Stages

1. **Checkout**
    - Pull latest code from GitHub

2. **Build & Test**
    - Runs:
      ```bash
      mvn clean test
      ```
    - Executes JUnit tests
    - Publishes test reports

3. **Package**
    - Runs:
      ```bash
      mvn -DskipTests package
      ```
    - Builds deployable JAR

4. **Artifact Archiving**
    - Jenkins stores build outputs for download

---

## 🐳 Containerization (Docker)

Jenkins runs inside a Docker container to ensure:

- Consistent build environment
- Easy reproducibility
- Portable CI setup

---

## 💻 Local Development Setup

### Requirements
- Java 21
- Maven 3.x

---

### Verify Installation

```bash
java -version
mvn -version