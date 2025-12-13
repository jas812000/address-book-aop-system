# Address Book System with Aspect-Oriented Logging and Validation

## Overview
This project implements a modular command-line Address Book system in Java, designed to demonstrate backend architecture principles and the practical use of Aspect-Oriented Programming (AOP). The system supports adding, updating, deleting, searching, and displaying contact records while cleanly separating core business logic from cross-cutting concerns such as logging, validation, exception handling, and system notifications.

AspectJ is used to intercept key operations and apply these concerns without polluting the core application code, resulting in a maintainable and extensible design.

---

## Key Features
- Create, update, delete, search, and display address book contacts
- Structured contact data (name, address, city, state, ZIP, phone)
- File-based persistence with custom parsing and formatting
- Input normalization and field-level validation
- Centralized exception handling
- Audit logging for contact updates and deletions
- System message and notification handling via aspects

---

## Architecture Overview
The system is organized into clearly defined components:

### Core Application
- `AddressBook` – Manages in-memory contact storage
- `AddressBookController` – Coordinates user actions and system operations
- `Main` / `AddressBookApp` – Application entry points
- `Contact` – Domain model representing a contact record

### Input, Parsing, and Storage
- `ContactInputHandler` – Handles user input
- `AddressBookStorage` – Loads and saves contacts
- `FileLoader`, `FileSaver` – File I/O utilities
- `ContactLineParser`, `FileParser`, `LineParser` – Parsing logic
- `ContactFormatter`, `ContactCSVFormatter`, `TextFormatter` – Output formatting

### Validation & Integrity
- `FieldValidator`, `StringValidator`
- `ContactIntegrityValidator`
- `ContactNormalizer`

### Aspect-Oriented Components (AspectJ)
- Add, update, and delete logging aspects
- Validation and validation logging aspects
- Exception handling aspect
- Display and notification aspects
- System path and message logging aspects

These aspects encapsulate cross-cutting behavior and apply it declaratively via pointcuts and advice.

---

## Aspect-Oriented Design
AspectJ is used to modularize concerns that would otherwise be scattered across the codebase:

- **Logging:** Records contact updates and deletions to an audit file
- **Validation:** Intercepts operations to enforce data correctness
- **Exception Handling:** Centralizes error handling and messaging
- **Notifications & Display:** Standardizes user-facing output
- **System Tracing:** Logs application paths and execution context

This approach improves readability, reduces duplication, and makes the system easier to extend or refactor.

---

## Tools & Technologies
- Java
- AspectJ
- Command-line interface
- File-based persistence
- Object-Oriented Programming (OOP)
- Aspect-Oriented Programming (AOP)

---

## Artifacts
- Java source files for core application logic
- AspectJ source files for cross-cutting concerns
- Audit log output files capturing update and delete operations

---

## Outcome
This project demonstrates:
- Strong separation of concerns through AOP
- Modular backend design
- Practical logging and validation strategies
- Maintainable error handling architecture
- Clean organization of parsing, formatting, and persistence logic

The system is structured to support future enhancements such as a graphical interface or database-backed persistence without requiring architectural changes.

---

## Repository Structure
/src
  /aspects
  /core
  /storage
  /validation
  /utils

---

## License
© 2025 James Stevens. All rights reserved.

This source code is provided for educational, evaluation, and portfolio review purposes.
Permission is granted to clone and run the code locally for non-commercial review.

No permission is granted to copy, modify, redistribute, or use this code in
commercial or production systems without explicit written consent from the author.

---
