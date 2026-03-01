# Architecture Overview

## System Architecture

Invexus DMS follows a **layered architecture pattern** to maintain separation of concerns, improve maintainability, and support future scalability.

The system is organized into the following layers:

---

## Application Layers

### Presentation Layer (UI)

- Built using **JavaFX**
- Handles user interactions
- Displays asset data in table views
- Provides forms for CRUD operations

Key Components:
- `MainController`
- `AssetFormController`
- JavaFX FXML Views

---

### Service Layer (Business Logic)

Responsible for implementing application logic and enforcing rules.

Responsibilities include:

- Validating asset data
- Managing CRUD operations
- Coordinating repository access
- Calculating inventory metrics

Key Class:
- `AssetManager`

---

### Repository Layer (Data Access)

Abstracts storage implementation.

Supports multiple persistence strategies:

- **In-Memory Repository**  
  Used for testing and demos

- **MySQL Repository**  
  Used for real-world persistence

Key Classes:
- `AssetRepository` (interface)
- `InMemoryAssetRepository`
- `ARepoMySQL`

---

### Domain Layer (Core Models)

Represents business entities and data structures.

Key Classes:
- `Asset`
- `AssetStatus`

---

## Architectural Benefits

This design provides:

- Clear separation of responsibilities
- Easy switching between storage implementations
- Improved testability
- Maintainable and scalable structure

---

## Evolution of the System

The application evolved through multiple phases:

1. CLI prototype
2. In-memory asset management
3. JavaFX GUI implementation
4. CSV import support
5. Optional database persistence
6. CI/CD automation pipeline

---

## Future Architecture Enhancements

Planned improvements include:

- REST API layer
- Cloud database support
- Role-based access control
- Web interface version