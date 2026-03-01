# User Interface Design

## Overview

The Invexus DMS user interface is built using **JavaFX** and focuses on usability, clarity, and efficient asset management workflows.

The UI enables users to quickly perform CRUD operations while maintaining visibility into asset status and inventory value.

---

## Main Interface Components

### Asset Table View

Displays all tracked assets with the following columns:

- Asset Tag
- Name
- Location
- Purchase Date
- Unit Cost
- Quantity
- Status
- Assigned Flag

---

### Action Controls

Users can perform core operations through intuitive buttons:

- Add Asset
- Edit Asset
- Delete Asset
- Import CSV

---

### Filtering and Search

Supports:

- Status filtering
- Text search by tag or name
- Real-time table refresh

---

## Form Design

The asset form includes:

- Text inputs
- Date picker
- Numeric validation
- Dropdown for status selection

Validation ensures:

- Unique asset tags
- Valid cost formatting
- Acceptable quantity ranges

---

## Usability Principles Applied

- Minimal clicks for common tasks
- Clear feedback on errors
- Automatic UI refresh after changes
- Consistent layout and spacing

---

## Future UI Enhancements

- Dashboard analytics
- Dark mode theme
- Drag-and-drop asset import
- Mobile-friendly interface