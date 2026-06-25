## 1. Database setup

- [x] 1.1 Add MySQL datasource config to application.properties
- [x] 1.2 Create init SQL script with user, equipment, borrow_record tables
- [x] 1.3 Add MyBatis-Plus and JWT dependencies to pom.xml

## 2. Backend: User auth

- [x] 2.1 Create User entity with id, username, password, displayName, role, createdAt, updatedAt
- [x] 2.2 Create UserMapper (MyBatis-Plus)
- [x] 2.3 Create AuthController: register and login endpoints
- [x] 2.4 Create JWT utility: generate and validate token
- [x] 2.5 Configure Spring Security: permit register/login, require auth for other endpoints, role filtering
- [x] 2.6 Create UserService with password encoding (BCrypt)

## 3. Backend: Equipment catalog

- [x] 3.1 Create Equipment entity with id, name, code, category, location, totalQuantity, availableQuantity, status, createdAt, updatedAt
- [x] 3.2 Create EquipmentMapper
- [x] 3.3 Create EquipmentService: CRUD, search, quantity tracking
- [x] 3.4 Create EquipmentController: admin CRUD endpoints + public list/search endpoints
- [x] 3.5 Add preload of equipment data in init SQL

## 4. Backend: Borrow/return

- [x] 4.1 Create BorrowRecord entity with id, userId, equipmentId, quantity, startTime, endTime, status, rejectReason, createdAt, updatedAt
- [x] 4.2 Create BorrowRecordMapper
- [x] 4.3 Create BorrowService: submit, approve, reject, return, list with filters
- [x] 4.4 Create BorrowController: user borrow/return endpoints + admin approve/reject endpoints

## 5. Frontend: Project scaffold

- [x] 5.1 Create Vue 3 + Vite project in frontend/ directory
- [x] 5.2 Add Vue Router with route definitions (login, register, equipment, my-borrows, admin-equipment, admin-approvals)
- [x] 5.3 Add Pinia store for auth state (token, user info)
- [x] 5.4 Load CSS files (css/*.css) and apply .light/.dark classes
- [x] 5.5 Create API helper (axios wrapper with JWT interceptor)

## 6. Frontend: Auth pages

- [x] 6.1 Create LoginPage with username/password form
- [x] 6.2 Create RegisterPage with username/password/displayName form
- [x] 6.3 Add navigation guard: redirect to login if unauthenticated

## 7. Frontend: Equipment pages

- [x] 7.1 Create EquipmentListPage: browse all equipment, keyword search, category filter
- [x] 7.2 Create EquipmentDetailPage: show equipment info, "Borrow" button
- [x] 7.3 Create AdminEquipmentPage (admin only): equipment table with add/edit/delete, search

## 8. Frontend: Borrow/return pages

- [x] 8.1 Create MyBorrowsPage: list user's borrow records with status, return button
- [x] 8.2 Create borrow dialog/modal: select quantity, set return time, submit
- [x] 8.3 Create AdminApprovalsPage (admin only): list pending requests with approve/reject buttons
