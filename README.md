# SOLID Principles - Single Responsibility Principle Exercises

This repository contains exercises focused on understanding and applying the **Single Responsibility Principle (SRP)**, the first principle of SOLID.

## What is Single Responsibility Principle?

The Single Responsibility Principle states that **a class should have only one reason to change**. In other words, a class should have only one job or responsibility.

## Exercises

### [Shopping Cart Exercise](./shoppingcart-exercise.md)

A shopping cart system that violates SRP, demonstrating the problems that arise when a class has multiple responsibilities.

### [User Registration Exercise](./userregistration-exercise.md)

A user registration system that violates SRP with eight different responsibilities, including validation, password hashing, email services, and account activation.

## Learning Objectives

After completing these exercises, you should be able to:
- Identify violations of the Single Responsibility Principle
- Recognize the problems caused by SRP violations
- Refactor code to comply with SRP
- Understand how SRP improves maintainability and testability

## Running the Exercises

Each exercise includes:
- Working code that violates SRP
- Integration tests that verify functionality
- Clear instructions for refactoring

To run tests:
```bash
mvn test
```

## Project Structure

```
src/
  main/
    java/
      com/
        example/
          shoppingcart/
            ... (shopping cart classes)
          userregistration/
            ... (user registration classes)
  test/
    java/
      com/
        example/
          shoppingcart/
            ... (shopping cart integration tests)
          userregistration/
            ... (user registration integration tests)
shoppingcart-exercise.md (exercise-specific instructions and documentation)
userregistration-exercise.md (exercise-specific instructions and documentation)
```

Each exercise maintains its own package structure for clarity and isolation. Exercise documentation is located at the root level with descriptive names.

