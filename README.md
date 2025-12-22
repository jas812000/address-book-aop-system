# Address Book AOP System

## Overview
The Address Book AOP System is a modular Java command-line backend application designed to manage a persistent address book of contacts. The system supports adding, updating, deleting, and displaying contacts while enforcing validation and consistency rules.

The project emphasizes backend engineering concerns such as domain modeling, validation, file-based persistence, and separation of cross-cutting concerns using Aspect-Oriented Programming (AOP). Rather than relying on a database, all data is stored using structured CSV files, making the system portable, deterministic, and easy to test.

---

## Features
- Automatic loading of contacts at application startup  
- Create, update, delete, and display contact records  
- Structured CSV-based persistence  
- Input normalization and validation  
- Aspect-Oriented Programming for cross-cutting concerns  
- Centralized controller coordinating domain logic  
- Graceful handling of malformed or missing data files  
- Runnable standalone JAR via Maven

---

## Architecture Overview
The system follows a layered, object-oriented architecture with AspectJ-based cross-cutting concerns:

### Controller / Application Layer
- Coordinates user interaction, menu flow, and orchestration of address book operations.

---

## Domain Model  

#### Contact  
- Encapsulates contact data such as name, address, phone number, city, state, and ZIP code.

#### Contact State Management  
Contacts follow a controlled lifecycle governed by validation rules:
- Valid contact fields are required for creation and update  
- Invalid or malformed records are rejected  
- Updates preserve data integrity  
- Delete operations are explicit and irreversible  

Validation rules are enforced consistently through AspectJ aspects prior to state modification.

### Persistence Layer  
- File-based storage using CSV format  
- Explicit parsing and formatting logic  
- Deterministic load and save behavior  

### Validation & Cross-Cutting Concerns (AOP)  

AspectJ aspects are used to enforce validation, logging, and error handling without polluting core business logic.

---

## Repository Structure

The project follows the standard Maven directory layout:
```
address-book-aop-system/
├── pom.xml
├── README.md
├── LICENSE
├── docs/
│   └── sample-address-book.csv
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── address_book/
    │   │   ├── address_utils/
    │   │   ├── io/
    │   │   ├── utilities/
    │   │   └── validators/
    │   └── aspectj/
    │       └── address_aspects/
    └── test/
        └── java/
            ├── address_book/
            ├── address_utils/
            └── validators/
```

Notes:
- Production code lives under `src/main/java`
- AspectJ aspects live under `src/main/aspectj`
- Tests live under `src/test/java` and run with `mvn test`

---

## Data Persistence Model  
The address book is stored as a CSV file located in the data directory:
```
data/address_book.csv
```

Each contact is represented as a single CSV record with structured fields. The system automatically loads the file at startup and writes changes back to disk when modifications occur.

If the file does not exist, the application initializes an empty address book and creates the file on first save.

---

## Error Handling Strategy
The system enforces correctness and resilience through:

- Defensive parsing of CSV input  
- Graceful handling of malformed records  
- Explicit validation of required fields  
- Centralized exception handling via aspects  
- Non-fatal handling of file I/O failures  

Malformed input does not crash the application and is safely handled.

---

## Build & Run

### Prerequisites
- Java 17+  
- Maven 3.8+

### Build  
```bash
mvn clean package  
```

### Run (Standalone JAR)  
```bash
java -jar target/address-book-aop-system-1.0.0.jar  
```

The CLI menu provides options to:
- Add a contact  
- Update an existing contact  
- Delete a contact  
- Display all contacts  
- Exit the application  

---

## Tools & Technologies  
- **Language**: Java 17  
- **Build Tool**: Maven  
- **Aspect-Oriented Programming**: AspectJ  
- **Testing**: JUnit 5  
- **Persistence**: CSV file-based storage  
- **Architecture Style**: Layered backend architecture  

---

## Purpose  
This project serves as a backend engineering case study demonstrating:
- Separation of concerns using AOP  
- Clean domain modeling  
- Deterministic file-based persistence  
- Defensive input validation  
- Controlled state transitions  
- Build tooling and runnable artifacts with Maven  

---

## License
This project is licensed under the MIT License.
See the [LICENSE](LICENSE) file for details.

---

