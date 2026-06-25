## ADDED Requirements

### Requirement: Equipment CRUD (Admin)
The system SHALL allow admin users to create, read, update, and delete equipment. Each equipment has name, code (unique), category, location, total quantity, and status.

#### Scenario: Admin adds equipment
- **WHEN** admin submits valid equipment info
- **THEN** system creates the equipment with available_quantity = total_quantity

#### Scenario: Admin deletes equipment
- **WHEN** admin deletes an equipment with no active borrow records
- **THEN** system removes the equipment

#### Scenario: Admin cannot delete equipment with active borrows
- **WHEN** admin deletes an equipment that has PENDING or APPROVED borrow records
- **THEN** system returns error "Equipment has active borrow records"

#### Scenario: Admin updates equipment
- **WHEN** admin updates equipment fields
- **THEN** system updates the equipment record

### Requirement: Equipment search
The system SHALL allow searching equipment by name, code, category, location, and status. Supports keyword matching on name and code.

#### Scenario: Search by keyword
- **WHEN** user searches with keyword "micro"
- **THEN** system returns equipment whose name or code contains "micro"

#### Scenario: Filter by category
- **WHEN** user filters by category INSTRUMENT
- **THEN** system returns only equipment in that category

### Requirement: Equipment categories
The system SHALL support fixed categories: INSTRUMENT (仪器), CONSUMABLE (耗材), SOFTWARE (软件), OTHER (其他).

#### Scenario: List categories
- **WHEN** user requests available categories
- **THEN** system returns the four predefined categories

### Requirement: Equipment availability tracking
The system SHALL track available_quantity separately from total_quantity. Available quantity decreases when borrow is approved and increases when equipment is returned.

#### Scenario: View available quantity
- **WHEN** user views equipment detail
- **THEN** system shows both total_quantity and available_quantity

#### Scenario: Equipment exhausted
- **WHEN** available_quantity reaches 0
- **THEN** user cannot submit new borrow requests for this equipment
