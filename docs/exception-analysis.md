# Exception Handling Analysis

## 1. Why is it important to use custom exceptions instead of generic exceptions?

Custom exceptions make errors easier to identify and handle. In GameZoneUnicesar, they help distinguish between missing resources, invalid data, business rule violations, and persistence problems. This improves code readability, maintenance, and error handling across the application.

## 2. What is the difference between a technical exception and a business exception?

A technical exception is related to system operations, such as an input/output error when reading or writing a CSV file. A business exception occurs when an operation violates an application rule, such as attempting to register a sale with insufficient stock. Separating these errors allows the system to respond appropriately to each situation.

## 3. Why should exceptions be handled at different levels of the application?

Each application layer has different responsibilities. Repositories handle data access errors, services validate business rules, and the user interface presents understandable messages. Handling exceptions at the appropriate level prevents technical details from being exposed to users and keeps responsibilities clearly separated.

## 4. What are the benefits of using an exception hierarchy?

An exception hierarchy organizes related errors under a common parent class. In GameZoneUnicesar, `GameZoneException` allows the application to handle general system errors, while subclasses such as `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, and `PersistenceException` provide more specific handling. This makes the code easier to extend and maintain.

## 5. How should the application communicate errors to users?

The application should display clear, friendly messages in Spanish that explain what happened and, when possible, how to correct the problem. It should not expose stack traces, file paths, or internal technical details. Technical information should be kept available for authorized debugging and maintenance.
