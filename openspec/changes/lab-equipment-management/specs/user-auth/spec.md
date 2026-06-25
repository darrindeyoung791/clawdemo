## ADDED Requirements

### Requirement: User registration
The system SHALL allow anyone to register a new account with username, password, and display name. New users default to ROLE_USER.

#### Scenario: Successful registration
- **WHEN** user submits registration with valid username (>2 chars), password (>6 chars), and display name
- **THEN** system creates the user account with ROLE_USER and returns success

#### Scenario: Duplicate username
- **WHEN** user submits registration with an existing username
- **THEN** system returns error "Username already exists"

### Requirement: User login
The system SHALL authenticate users with username + password and return a JWT token.

#### Scenario: Successful login
- **WHEN** user submits correct username and password
- **THEN** system returns a JWT token with user id, username, display name, and role

#### Scenario: Wrong password
- **WHEN** user submits incorrect password
- **THEN** system returns 401 Unauthorized

### Requirement: Role-based access control
The system SHALL enforce access control based on user roles. ROLE_ADMIN can manage equipment and approve/return requests. ROLE_USER can browse equipment and submit/return own borrow requests.

#### Scenario: Admin accesses equipment management
- **WHEN** a user with ROLE_ADMIN requests /api/equipment/manage
- **THEN** system allows access

#### Scenario: User accesses admin-only endpoint
- **WHEN** a user with ROLE_USER requests /api/equipment/manage
- **THEN** system returns 403 Forbidden
