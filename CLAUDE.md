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

- `com.solvd.airport.dao` — DAO interfaces only, no implementations yet. `IBaseDAO<T>` defines the generic CRUD contract (`save`, `update`, `getById`, `deleteById`, `getAll`). Specialized interfaces (`IAirlineDAO`, `IPassengerDAO`, `IStaffDAO`, `IUserDAO`) extend it with domain-specific queries.

**Current state:** DAO implementations, database connectivity, service layer, and tests are all absent. The next layer to build is concrete DAO implementations (likely JDBC).

## Dependencies

- **Logback** (1.5.32) — logs to console and `logs/development_company.log` (configured in `src/main/resources/logback.xml`, root level DEBUG)
- **Commons-IO** (2.15.1) and **Commons-Lang3** (3.20.0)

No ORM, no web framework, no test framework configured yet.
