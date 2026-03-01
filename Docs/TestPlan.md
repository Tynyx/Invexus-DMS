# Test Plan

## Overview

The Invexus DMS testing strategy ensures system reliability through unit testing, validation checks, and automated CI/CD verification.

---

## Testing Levels

### Unit Testing

Conducted using **JUnit 5**.

Focus Areas:

- Asset validation logic
- CSV import parsing
- Repository CRUD operations
- Business rule enforcement

---

### Integration Testing

Verifies:

- UI and service interaction
- Repository switching functionality
- Data persistence behavior

---

### Automated CI Testing

The Jenkins CI pipeline executes:

- Maven build
- Automated unit tests
- Packaging validation

---

## Test Scenarios Covered

- Adding valid assets
- Rejecting duplicate tags
- Parsing CSV imports
- Handling invalid data formats
- Updating and deleting assets

---

## Error Handling Validation

Tests confirm system resilience against:

- Invalid currency values
- Incorrect date formats
- Missing required fields
- Unsupported status values

---

## Future Testing Improvements

Planned additions:

- UI automation testing
- Database integration tests
- Performance testing
- Load testing