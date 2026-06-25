## ADDED Requirements

### Requirement: Submit borrow request
The system SHALL allow authenticated users to submit a borrow request for available equipment, specifying quantity and expected return time.

#### Scenario: Successful borrow request
- **WHEN** user submits borrow request for equipment with sufficient available_quantity
- **THEN** system creates a borrow_record with status PENDING

#### Scenario: Borrow exceeds available quantity
- **WHEN** user requests quantity greater than available_quantity
- **THEN** system returns error "Insufficient available quantity"

### Requirement: Approve borrow request (Admin)
The system SHALL allow admin users to approve or reject pending borrow requests. On approval, the system deducts available_quantity. On rejection, admin provides a reason.

#### Scenario: Admin approves request
- **WHEN** admin approves a PENDING borrow request
- **THEN** system sets status to APPROVED and deducts available_quantity

#### Scenario: Admin rejects request
- **WHEN** admin rejects a PENDING borrow request with a rejection reason
- **THEN** system sets status to REJECTED and stores reject_reason

### Requirement: Return equipment
The system SHALL allow users to return borrowed equipment. On return, the system restores available_quantity.

#### Scenario: User returns equipment
- **WHEN** user clicks return on an APPROVED borrow_record
- **THEN** system sets status to RETURNED and restores available_quantity

### Requirement: View borrow records
The system SHALL allow users to view their own borrow records and admins to view all records. Supports search by equipment name and filter by status.

#### Scenario: User views own records
- **WHEN** user requests borrow record list
- **THEN** system returns only this user's records

#### Scenario: Admin views all records
- **WHEN** admin requests borrow record list
- **THEN** system returns all users' records
