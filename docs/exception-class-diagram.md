# Exception Class Diagram

The following diagram represents the custom exception hierarchy designed for GameZoneUnicesar.

```mermaid
classDiagram
    class RuntimeException {
        <<Java>>
    }

    class GameZoneException {
        -String errorCode
        +GameZoneException(String message)
        +GameZoneException(String message, String errorCode)
        +getErrorCode() String
    }

    class ResourceNotFoundException {
        +ResourceNotFoundException(String resourceType, String id)
    }

    class BusinessRuleException {
        +BusinessRuleException(String message)
    }

    class InvalidDataException {
        +InvalidDataException(String fieldName, String description)
    }

    class PersistenceException {
        +PersistenceException(String message, Throwable cause)
    }

    RuntimeException <|-- GameZoneException
    GameZoneException <|-- ResourceNotFoundException
    GameZoneException <|-- BusinessRuleException
    GameZoneException <|-- InvalidDataException
    GameZoneException <|-- PersistenceException
```

## Description

- `GameZoneException` is the base class for application-specific exceptions and stores an error code.
- `ResourceNotFoundException` represents a resource that cannot be found.
- `BusinessRuleException` represents a violation of a business rule.
- `InvalidDataException` represents invalid input data.
- `PersistenceException` represents an error when accessing or storing data and preserves the original cause.

## Validation and Application Integration

The validation layer checks input data before the service layer performs business operations.

```mermaid
classDiagram
    class GameZoneException {
        -String errorCode
        +getErrorCode() String
    }

    class ResourceNotFoundException
    class BusinessRuleException
    class InvalidDataException
    class PersistenceException

    class ProductValidator
    class PersonValidator
    class SaleValidator

    class ProductService
    class PersonService
    class SaleService
    class ConsoleMenu

    GameZoneException <|-- ResourceNotFoundException
    GameZoneException <|-- BusinessRuleException
    GameZoneException <|-- InvalidDataException
    GameZoneException <|-- PersistenceException

    ProductValidator ..> ProductService : validates data for
    PersonValidator ..> PersonService : validates data for
    SaleValidator ..> SaleService : validates data for

    ConsoleMenu ..> ProductService : invokes
    ConsoleMenu ..> PersonService : invokes
    ConsoleMenu ..> SaleService : invokes

    ConsoleMenu ..> GameZoneException : handles errors
```

The validators are responsible for checking input data, while services coordinate business operations. The console interface invokes services and handles application-specific exceptions to display understandable messages to users.
