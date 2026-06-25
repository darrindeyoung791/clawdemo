# AGENTS.md

## Stack

- **Backend:** Java 21, Spring Boot 4.1.0, Spring Security, Lombok
- **Frontend:** Vue 3 + Vite + Vue Router + Pinia + Axios
- **DB:** MySQL (`mysql-connector-j`, runtime scope), accessed via Spring JDBC `JdbcTemplate` (no MyBatis/JPA)
- **Build:** Maven wrapper 3.9.16 (`mvnw`/`mvnw.cmd`)
- **Design:** Material Design 3 (colors in `css/*.css`), no shadows, no hover lift, Material Icons only, no emoji

## Source layout

```
src/
  main/java/io/github/darrindeyoung791/clawdemo/
    ClawdemoApplication.java          # @SpringBootApplication entry point
    config/                           # Spring Security, CORS config
    controller/                       # Auth, Equipment, Borrow controllers
    dao/                              # JdbcTemplate-based DAOs (UserDao, EquipmentDao, BorrowRecordDao)
    entity/                           # POJOs: User, Equipment, BorrowRecord
    service/                          # Business logic
    util/                             # JWT utility
  main/resources/
    application.properties             # MySQL + JWT config
    sql/init.sql                       # Table DDL + seed data
  test/.../clawdemo/
    ClawdemoApplicationTests.java      # default @SpringBootTest context load
css/                                   # MD3 color tokens: light, light-mc, light-hc, dark, dark-mc, dark-hc
openspec/                              # OpenSpec dev-planning artifacts
```

## Key conventions

- **Package:** `io.github.darrindeyoung791.clawdemo`
- **CSS:** load from `css/` folder, apply `.light`/`.dark` classes, standard contrast unless specified
- **Icons:** Material Icons only, never emoji
- **Layout:** fluid/responsive across all screen widths
- **UI:** flat (no `box-shadow`, no hover lift / translateY effects)
- **DB access:** hand-written SQL via `JdbcTemplate` in `dao/` package (no ORM)
- **User IDs:** UUID strings (not auto-increment)
- **Equipment codes:** UUID-derived on creation (not manually assigned)
- **Passwords:** BCrypt hash via Spring Security `PasswordEncoder`
- **Auth:** JWT in `Authorization: Bearer` header, no refresh token

## Dev workflow

Uses OpenSpec (`openspec` CLI) for change-driven development:

1. `/opsx:propose <name>` — create change with proposal/design/tasks
2. `/opsx:apply <name>` — implement tasks
3. `/opsx:archive <name>` — finalize and archive

Supported across `.opencode/`, `.claude/`, `.qwen/`, `.github/` configurations.

## Build & run

```powershell
./mvnw spring-boot:run          # dev server
./mvnw test                     # run tests
./mvnw clean package            # build JAR
```

Requires MySQL running and configured — add `spring.datasource.*` to `application.properties`.
