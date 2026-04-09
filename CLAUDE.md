# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
mvn compile          # Compile sources
mvn test             # Run tests
mvn package          # Build JAR
mvn clean install    # Full clean build
```

Java 21 is required. Entry point is `com.solvd.airport.Main`.

## Architecture

**Maven project** (`com.solvd` / `airport-db`) with two main packages:

- `com.solvd.airport.models` — Plain Java entity classes. `Passenger` and `Staff` both extend `User`. Other entities (Airline, Flight, Plane, Gate, Terminal, AirportInfo, Ticket, Baggage, Maintenance) hold foreign-key IDs as fields (e.g. `planesId`, `airlinesId`) rather than object references.

- `com.solvd.airport.dao` — DAO interfaces only, no implementations yet. `IBaseDAO<T>` defines the generic CRUD contract (`save`, `update`, `getById`, `deleteById`, `getAll`). Specialized interfaces (`IAirlineDAO`, `IPassengerDAO`, `IStaffDAO`, and others) extend it with domain-specific queries. `IUserDAO` was removed — passenger and staff data is accessed through their own DAOs via JOIN queries on the `Users` table.

**Current state:** DAO implementations, database connectivity, service layer, and tests are all absent. The next layer to build is concrete DAO implementations (likely JDBC).

## Dependencies

- **Logback** (1.5.32) — logs to console and `logs/development_company.log` (configured in `src/main/resources/logback.xml`, root level DEBUG)
- **Commons-IO** (2.15.1) and **Commons-Lang3** (3.20.0)

No ORM, no web framework, no test framework configured yet.

## File Directory

### Root
- `pom.xml` — Maven build config: Java 21, groupId `com.solvd`, dependencies on Logback, Commons-IO, Commons-Lang3.
- `schema.sql` — MySQL Workbench-generated DDL for all 11 tables with foreign key constraints.
- `.gitignore` — Excludes `target/`, `.idea/`, and IDE artifacts from version control.
- `CLAUDE.md` — This file; guidance for Claude Code on build, architecture, and structure.

### src/main/resources
- `logback.xml` — Logback config: DEBUG root level, console + file appenders (`logs/development_company.log`).

### src/main/java/com/solvd/airport
- `Main.java` — Application entry point; currently empty.

### …/dao
- `IBaseDAO.java` — Generic CRUD contract (`save`, `update`, `getById`, `deleteById`, `getAll`) extended by all DAO interfaces.
- `IAirlineDAO.java` — Airline queries: by name, contact email, maintenance provider, safety rating, fleet size.
- `IAirportInfoDAO.java` — AirportInfo queries: by IATA code and country.
- `IBaggageDAO.java` — Baggage queries: by tag number and ticket ID.
- `IFlightDAO.java` — Flight queries: by flight number, departure time range, plane ID, gate ID.
- `IGateDAO.java` — Gate queries: by operational status and terminal ID.
- `IMaintenanceDAO.java` — Maintenance queries: by check type, plane ID, staff ID, and upcoming date.
- `IPassengerDAO.java` — Passenger queries: by nationality and top flyers by bonus miles.
- `IPlaneDAO.java` — Plane queries: by board number and airline ID.
- `IStaffDAO.java` — Staff queries: by role and minimum salary.
- `ITerminalDAO.java` — Terminal queries: by airport ID and terminal name.
- `ITicketDAO.java` — Ticket queries: by booking status and passenger ID.

### …/util
- `AbstractMySQLDB.java` — Abstract base class for all DAO implementations; loads `db.properties` from the classpath and exposes a `protected getConnection()` method.

### …/dao/impl
- *(empty — DAO implementations go here, each extending `AbstractMySQLDB` and implementing their respective interface)*

### …/models
- `User.java` — Abstract base entity: id, firstName, lastName, dateOfBirth, email, phoneNumber.
- `Passenger.java` — Extends `User`; adds nationality, passengerType, bonusMiles, membershipDate.
- `Staff.java` — Extends `User`; adds hireDate, role, salary.
- `Airline.java` — Airline entity: name, safetyRating, fleetSize, contactEmail, maintenanceProvider.
- `Plane.java` — Aircraft entity: model, boardNumber, seatsCapacity, yearProduction, airlinesId.
- `Flight.java` — Flight entity: flightNumber, departureTime, arrivalTime, planesId, gatesId.
- `Gate.java` — Gate entity: gateNumber, floorLevel, currentStatus, boardingStartTime, terminalsId.
- `Terminal.java` — Terminal entity: terminalName, capacity, checkInCount, luggageBelts, workingHours, airportInfoId.
- `AirportInfo.java` — Airport entity: name, city, country, code, website.
- `Ticket.java` — Ticket entity: seatNumber, price, bookingStatus, passengersId.
- `Baggage.java` — Baggage entity: tagNumber, checkedInDate, weight, ticketsId.
- `Maintenance.java` — Maintenance record: checkType, lastServiceDate, nextServiceDate, staffId, planesId.
