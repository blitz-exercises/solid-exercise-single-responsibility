# User Registration - Single Responsibility Principle Exercise

## Overview

This exercise demonstrates a user registration system that was built by a developer who was not familiar with the Single Responsibility Principle. The code works correctly and passes all tests, but it violates SRP by combining multiple responsibilities into a single class.

## The Problem

The `UserRegistration` class currently handles **eight different responsibilities**:

1. **User Validation** - Validating email format, password strength, and checking for duplicate emails
2. **Password Hashing** - Encrypting passwords using hashing algorithms
3. **User Data Storage** - Persisting user data in memory
4. **Verification Token Management** - Generating, storing, and validating verification tokens
5. **Verification Email Service** - Composing and sending account verification emails
6. **Welcome Email Service** - Composing and sending welcome emails after activation
7. **Account Activation** - Verifying tokens and activating user accounts
8. **Registration Logging** - Maintaining audit trail of registration events
9. **Profile Initialization** - Setting up default user profile settings

## Why This Is a Problem

Having multiple responsibilities in a single class leads to several issues:

### 1. **Difficulty in Maintenance**
- Changes to email formatting require modifying the `UserRegistration` class
- Changes to password hashing algorithms require modifying the `UserRegistration` class
- Changes to validation rules require modifying the `UserRegistration` class
- Changes to token generation logic require modifying the `UserRegistration` class
- Any modification risks breaking unrelated functionality

### 2. **Testing Challenges**
- Testing email functionality requires instantiating the entire registration system
- Testing password hashing requires creating user objects
- Cannot test validation logic in isolation
- Cannot test token generation without user storage
- Tests become fragile and tightly coupled

### 3. **Code Reusability**
- Password hashing logic cannot be reused for password reset flows
- Email service cannot be used for other notification types
- Validation logic cannot be reused for login or profile updates
- Token management cannot be reused for password reset tokens

### 4. **Future Changes Become Difficult**
Consider these future requirements:
- **Add SMS verification**: You'd need to modify `UserRegistration` class
- **Change email template format**: You'd need to modify `UserRegistration` class
- **Add password complexity rules**: You'd need to modify `UserRegistration` class
- **Support multiple languages**: You'd need to modify `UserRegistration` class
- **Add OAuth integration**: You'd need to modify `UserRegistration` class
- **Change token expiry time**: You'd need to modify `UserRegistration` class
- **Add biometric authentication**: You'd need to modify `UserRegistration` class
- **Implement password history**: You'd need to modify `UserRegistration` class

Each change touches the same class, increasing the risk of introducing bugs in unrelated areas.

## Your Task: Refactor to Follow SRP

Refactor the `UserRegistration` class to follow the Single Responsibility Principle by extracting each responsibility into its own dedicated class.

### What Must Remain Unchanged

**IMPORTANT**: The following must remain **completely untouched** during refactoring:

- `UserRegistrationService` interface - defines the public API contract
- `UserRegistrationIntegrationTest` - verifies the contract is maintained
- All data classes (`User`, `VerificationToken`, `RegistrationResult`)

### Refactoring Steps

1. **Extract User Validation**
   - Create a `UserValidator` class
   - Responsible for: validating email format, password strength, checking duplicates

2. **Extract Password Hashing**
   - Create a `PasswordHasher` class
   - Responsible for: hashing passwords and verifying password hashes

3. **Extract User Storage**
   - Create a `UserRepository` class
   - Responsible for: storing and retrieving user data

4. **Extract Verification Token Management**
   - Create a `VerificationTokenService` class
   - Responsible for: generating tokens, storing tokens, validating tokens

5. **Extract Email Services**
   - Create a `VerificationEmailService` class
   - Responsible for: composing and sending verification emails
   - Create a `WelcomeEmailService` class
   - Responsible for: composing and sending welcome emails
   - Alternatively, create a single `EmailService` with separate methods

6. **Extract Account Activation**
   - Create an `AccountActivationService` class
   - Responsible for: verifying tokens and activating accounts

7. **Extract Registration Logging**
   - Create a `RegistrationLogger` class
   - Responsible for: logging registration events and maintaining audit trail

8. **Extract Profile Initialization**
   - Create a `ProfileInitializer` class
   - Responsible for: setting up default user profile settings

9. **Refactor UserRegistration**
   - Keep only coordination/orchestration as its core responsibility
   - Inject the extracted services via constructor
   - Delegate to services while maintaining the same public API

### Expected Structure After Refactoring

```
src/main/java/com/example/userregistration/
├── UserRegistrationService.java      (interface - UNTOUCHED)
├── UserRegistration.java              (refactored - coordinator)
├── UserValidator.java                (NEW)
├── PasswordHasher.java               (NEW)
├── UserRepository.java               (NEW)
├── VerificationTokenService.java    (NEW)
├── VerificationEmailService.java     (NEW)
├── WelcomeEmailService.java          (NEW)
├── AccountActivationService.java    (NEW)
├── RegistrationLogger.java           (NEW)
├── ProfileInitializer.java           (NEW)
├── User.java                         (unchanged)
├── VerificationToken.java            (unchanged)
└── RegistrationResult.java           (unchanged)
```

### Success Criteria

After refactoring:
1. ✅ All tests pass (run `mvn test`)
2. ✅ Each class has a single, clear responsibility
3. ✅ The `UserRegistrationService` interface remains unchanged
4. ✅ The `UserRegistrationIntegrationTest` remains unchanged and passes

### Evaluation

After completing the refactoring, evaluate how future changes would be easier:

**Before Refactoring:**
- Changing email format → Modify `UserRegistration` class (risks breaking validation logic)
- Adding SMS verification → Modify `UserRegistration` class (risks breaking email logic)
- Changing password rules → Modify `UserRegistration` class (risks breaking token logic)
- Adding new password hashing algorithm → Modify `UserRegistration` class (risks breaking all other features)

**After Refactoring:**
- Changing email format → Modify only `VerificationEmailService` or `WelcomeEmailService` class (isolated change)
- Adding SMS verification → Create new `SmsVerificationService` class (no changes to existing code)
- Changing password rules → Modify only `UserValidator` class (isolated change)
- Adding new password hashing algorithm → Modify only `PasswordHasher` class (isolated change)
- Adding two-factor authentication → Create new `TwoFactorAuthService` class (no changes to existing code)
- Changing token expiry rules → Modify only `VerificationTokenService` class (isolated change)

### Benefits of Following SRP

1. **Easier Maintenance**: Changes are isolated to specific classes
2. **Better Testability**: Each responsibility can be tested independently
3. **Improved Reusability**: Services can be reused in other contexts (login, password reset, etc.)
4. **Reduced Risk**: Changes to one feature don't affect others
5. **Clearer Code**: Each class has a clear, single purpose
6. **Easier Scalability**: Services can be replaced or enhanced independently

## Getting Started

1. Review the current `UserRegistration` implementation
2. Identify all responsibilities
3. Extract each responsibility into its own class
4. Refactor `UserRegistration` to use the extracted services
5. Ensure all tests pass
6. Reflect on how the refactored code would make future changes easier

## Running Tests

```bash
# Run all tests
mvn test

# Run tests for this exercise specifically
mvn test -Dtest=UserRegistrationIntegrationTest
```

## Notes

- The code currently works correctly but violates SRP
- Focus on maintaining the public API contract defined by `UserRegistrationService`
- Use dependency injection to provide services to `UserRegistration`
- Each extracted class should have a single, clear responsibility
- Consider whether some services (like email services) should be combined or kept separate


