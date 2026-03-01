# Data Model

## Core Entity: Asset

The primary entity in Invexus DMS is the **Asset** object.

It represents a trackable item within the inventory system.

---

## Asset Attributes

| Field | Description |
|------|-------------|
| assetTag | Unique identifier |
| name | Asset name |
| location | Physical or logical location |
| purchaseDate | Acquisition date |
| unitCost | Cost per unit |
| quantity | Number of units |
| status | Current lifecycle state |
| assigned | Assignment flag |

---

## Status Enumeration

Assets use an enum for lifecycle management:

- IN_STOCK
- ASSIGNED
- REPAIR
- RETIRED

---

## Repository Pattern

The system uses an interface-based repository pattern:
AssetRepository
├── InMemoryAssetRepository
└── ARepoMySQL


This allows switching storage without modifying business logic.

---

## Data Validation Rules

The system enforces:

- Unique asset tags
- Valid currency formatting
- Acceptable quantity ranges
- Valid status mapping
- Flexible date parsing

---

## Future Data Model Enhancements

Planned extensions include:

- User assignment tracking
- Asset history logs
- Depreciation tracking
- Vendor management