# Address Book AOP System

A Java command-line address book that demonstrates **Aspect-Oriented Programming (AOP) with AspectJ** in a complete application.

The system manages persistent contacts with multiple addresses, phone numbers, and email addresses while using AspectJ to separate cross-cutting concerns—including domain validation, audit logging, notifications, presentation, exception logging, validation logging, system-message logging, and infrastructure tracing—from the core application logic.

This project is intentionally complementary to my `modular-cli-address-book` project. Both demonstrate mature contact-management capabilities, but this application explores how AspectJ can handle cross-cutting behavior that would otherwise be distributed throughout conventional Java classes.

## Features

- Create, search, update, delete, and display contacts
- Multiple addresses, phone numbers, and email addresses per contact
- Numbered standard labels with support for custom labels
- Support for compound, hyphenated, and apostrophe-containing names
- Canonical normalization of names, addresses, cities, states, email addresses, phone numbers, and ZIP codes
- Flexible phone entry with canonical `(XXX) XXX-XXXX` storage
- Search by first name, last name, full name, email address, or phone number
- Normalized phone-number searching
- Selection when multiple contacts match a search
- Detailed contact display before update and delete operations
- Updated contact display after successful changes
- Add, replace, or remove individual contact details during editing
- Operation cancellation during interactive workflows
- Domain validation during both construction and mutation
- Defensive collection handling to protect internal contact state
- CSV persistence with malformed-record isolation
- Migration support for the application's legacy CSV format
- Automatic loading at startup and saving after successful modifications
- Cross-cutting behavior implemented with AspectJ
- Runnable standalone JAR
- 116 automated JUnit 5 tests

## Why Aspect-Oriented Programming?

Some application behavior affects many otherwise unrelated classes. Validation, logging, notifications, exception observation, presentation, and infrastructure tracing are examples of these **cross-cutting concerns**.

Instead of embedding that behavior throughout the domain, controller, persistence, and utility classes, this project uses AspectJ pointcuts and advice to apply it at defined join points.

The core Java classes remain responsible for contact management, persistence, parsing, formatting, searching, and interaction. The aspects independently apply behavior that cuts across those responsibilities.

This makes the project a practical demonstration of AOP rather than simply a Java application that happens to include AspectJ as a dependency.

## AspectJ Design

The application contains ten aspects with distinct responsibilities:

| Aspect | Responsibility |
| --- | --- |
| `ValidationAspect` | Enforces domain validation at contact construction, mutation, and address-book boundaries |
| `ValidationLoggingAspect` | Records failed field-validation attempts |
| `AddContactLoggingAspect` | Records successful contact additions |
| `UpdateContactLoggingAspect` | Records before-and-after states for successful updates |
| `DeleteContactLoggingAspect` | Records successful contact deletions |
| `NotificationAspect` | Records successful add, update, and delete operation notifications |
| `DisplayAspect` | Applies detailed contact presentation at woven `Contact.toString()` call sites |
| `ExceptionHandlingAspect` | Observes and logs exceptions from controller, persistence, parsing, and I/O operations |
| `SystemMessageLoggingAspect` | Records significant CLI messages such as validation failures and cancellations |
| `AppPathsLoggingAspect` | Traces application file and directory path resolution |

Logging infrastructure is deliberately excluded from applicable recursive logging paths so that writing a log entry cannot continuously trigger additional logging advice.

## Architecture

The project combines object-oriented design with AspectJ-based cross-cutting behavior.

### Application and Domain

`address_book` contains the application workflow and contact domain:

- `Contact`
- `Address`
- `PhoneNumber`
- `EmailAddress`
- `AddressBook`
- `AddressBookController`
- `AddressBookApp`
- `ContactInputHandler`
- `ContactSearcher`
- `ContactUpdater`
- `FieldValidator`

A contact owns collections of addresses, phone numbers, and email addresses. Defensive copying prevents callers from using returned collections or mutable child objects to modify internal contact state unintentionally.

### Formatting, Normalization, and Parsing

`address_utils` contains:

- human-readable contact formatting
- CSV serialization
- contact-value normalization
- current-format CSV parsing
- legacy-format migration
- address-book persistence

User-entered values are normalized before being stored. Names, streets, and cities use consistent presentation casing; state abbreviations are uppercase; email addresses are lowercase; phone numbers and ZIP codes use canonical storage formats.

### File I/O and Utilities

`io` provides reusable path, loading, saving, and delimited-record parsing support.

`utilities` contains the logging and error-logging infrastructure used by the aspects.

### Validation

`validators` provides whole-contact integrity validation.

The CLI performs validation to provide immediate feedback, while `ValidationAspect` independently enforces validation at domain boundaries. Valid domain state therefore does not depend on data entering through the command-line interface.

## Contact Model

Each contact contains:

- first name
- last name
- zero or more labeled addresses
- zero or more labeled phone numbers
- zero or more labeled email addresses

Standard labels are selected through numbered menus. Users can also create validated custom labels such as `Vacation`, `School`, or `Emergency`.

Phone input accepts common ten-digit forms such as:

```text
2105551212
210-555-1212
(210) 555-1212
```

Valid phone numbers are normalized to:

```text
(210) 555-1212
```

State input uses two-letter abbreviations and is normalized to uppercase:

```text
tx → TX
```

ZIP codes support five-digit ZIP codes and dashed ZIP+4 values.

Email addresses are normalized to lowercase:

```text
James@Example.COM → james@example.com
```

Names and common address text receive consistent presentation casing:

```text
jAMES sTEVENS → James Stevens
123 main street → 123 Main Street
fort worth → Fort Worth
```

## Persistence

Contacts are stored in:

```text
data/address_book.csv
```

The current persistence schema stores each contact as a five-column CSV record:

```text
First Name,Last Name,Addresses,Phone Numbers,Email Addresses
```

Multiple values inside a contact are encoded within their corresponding CSV fields. Reserved internal separators are escaped before persistence and decoded when records are loaded.

The parser also recognizes the application's previous eight-column contact format and converts valid legacy records into the current domain model.

Malformed or invalid persisted records are isolated and skipped instead of preventing the rest of the address book from loading.

The base data directory can be overridden with the `APP_DATA_DIR` environment variable, which is also useful for isolated execution and testing.

## Searching and Editing

Contacts can be searched by:

- first name
- last name
- full name
- email address
- phone number

Phone searches normalize formatting so users do not need to enter a number exactly as it is displayed.

When multiple contacts match, the CLI presents the matching contacts and allows the user to select the intended contact or cancel the operation.

Before an update begins, the selected contact is displayed in full. Users can then change names and independently add, replace, or remove addresses, phone numbers, and email addresses.

Successful changes display the updated contact so the result can be reviewed immediately. Replace and remove workflows also display the selected value before it is changed or removed.

Delete operations similarly display the selected contact before confirmation.

## Display

The display workflow supports:

1. View One Contact
2. View All Contacts
3. Back

Detailed presentation is woven through `DisplayAspect`. Application call sites explicitly invoke `Contact.toString()`, providing the AspectJ call join point used to replace the basic domain representation with the formatted contact view.

A formatted contact can include sections such as:

```text
James Stevens

Home Address:
    123 Main Street
    Fort Worth, TX 76102

Mobile Phone:
    (210) 555-1212

Personal Email:
    james@example.com
```

This keeps detailed presentation as an AOP concern while allowing the underlying `Contact` domain object to retain a simple representation.

## Validation

Validation covers:

- first and last names
- custom labels
- street addresses
- cities
- two-letter state abbreviations
- ZIP codes
- phone numbers
- email addresses

Validation occurs at both the interactive input layer and woven domain boundaries. This provides user-friendly CLI feedback while preventing invalid state from bypassing the interface through direct object construction or mutation.

Phone validation intentionally accepts only supported ten-digit representations. Arbitrary characters are not stripped from otherwise invalid input.

## Testing

The project contains **116 automated JUnit 5 tests** covering:

- field validation
- AspectJ validation enforcement
- contact construction and defensive copying
- address-book behavior
- complete add, update, delete, and display controller workflows
- contact input and cancellation workflows
- contact update workflows
- contact searching and multiple-match selection
- name and contact-value normalization
- phone and ZIP normalization
- contact display formatting
- CSV serialization
- current and legacy CSV parsing
- malformed-record handling
- file parsing
- storage
- whole-contact integrity validation

Run the suite with:

```bash
mvn clean test
```

The Maven build compiles the Java and `.aj` production sources together so AspectJ execution and call join points are woven directly into the application.

In addition to automated testing, the CLI has been manually verified for creation, validation failures, searching by every supported field, normalized phone searching, multiple-match selection, cancellation, editing individual contact values, display, deletion, persistence across application restarts, and canonical input normalization.

## Repository Structure

```text
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
            ├── io/
            └── validators/
```

## Build and Run

### Prerequisites

- Java 17 or later
- Maven 3.8 or later

### Run the Tests

```bash
mvn clean test
```

### Build the Application

```bash
mvn clean package
```

The package build runs the complete automated test suite and creates the standalone application JAR.

### Run the Standalone JAR

```bash
java -jar target/address-book-aop-system-1.0.0.jar
```

The Maven Shade Plugin packages the AspectJ runtime with the application so the generated JAR can be executed directly.

## AOP vs. Conventional Modular Design

This repository and `modular-cli-address-book` intentionally explore two approaches to a similar problem.

`modular-cli-address-book` emphasizes conventional modular Java design, where responsibilities are separated through classes, interfaces, and application layers.

`address-book-aop-system` retains object-oriented domain and application design while moving appropriate cross-cutting concerns into AspectJ aspects.

The comparison demonstrates both conventional separation of concerns and the situations in which AOP can centralize behavior spanning multiple application layers.

## Technologies

- Java 17
- AspectJ
- Maven
- JUnit 5
- Maven Surefire Plugin
- Maven Shade Plugin
- CSV file persistence

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.