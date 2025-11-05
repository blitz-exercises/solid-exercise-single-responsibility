# Shopping Cart - Single Responsibility Principle Exercise

## Overview

This exercise demonstrates a shopping cart system that was built by a developer who was not familiar with the Single Responsibility Principle. The code works correctly and passes all tests, but it violates SRP by combining multiple responsibilities into a single class.

## The Problem

The `ShoppingCart` class currently handles **seven different responsibilities**:

1. **Cart Item Management** - Adding and retrieving items from the cart
2. **Price Calculation** - Calculating subtotals, discount amounts, and totals
3. **Discount Management** - Storing available discounts, validating codes, and applying discounts
4. **Payment Processing** - Processing payments and generating transaction IDs
5. **Order ID Generation** - Creating unique order identifiers
6. **Email Service** - Composing and sending order confirmation emails
7. **Checkout Orchestration** - Coordinating the entire checkout process

## Why This Is a Problem

Having multiple responsibilities in a single class leads to several issues:

### 1. **Difficulty in Maintenance**
- Changes to payment processing logic require modifying the `ShoppingCart` class
- Changes to email formatting require modifying the `ShoppingCart` class
- Changes to discount validation require modifying the `ShoppingCart` class
- Any modification risks breaking unrelated functionality

### 2. **Testing Challenges**
- Testing price calculations requires instantiating the entire shopping cart
- Testing email functionality requires creating cart items
- Cannot test payment processing in isolation
- Tests become fragile and tightly coupled

### 3. **Code Reusability**
- Price calculation logic cannot be reused elsewhere
- Email service cannot be used for other order types
- Payment processing cannot be reused for different entities

### 4. **Future Changes Become Difficult**
Consider these future requirements:
- **Add a new payment method**: You'd need to modify `ShoppingCart` class
- **Change email format**: You'd need to modify `ShoppingCart` class
- **Add tax calculation**: You'd need to modify `ShoppingCart` class
- **Support multiple discounts**: You'd need to modify `ShoppingCart` class
- **Add SMS notifications**: You'd need to modify `ShoppingCart` class
- **Change order ID format**: You'd need to modify `ShoppingCart` class

Each change touches the same class, increasing the risk of introducing bugs in unrelated areas.

## Your Task: Refactor to Follow SRP

Refactor the `ShoppingCart` class to follow the Single Responsibility Principle by extracting each responsibility into its own dedicated class.

### What Must Remain Unchanged

**IMPORTANT**: The following must remain **completely untouched** during refactoring:

- `ShoppingCartService` interface - defines the public API contract
- `ShoppingCartIntegrationTest` - verifies the contract is maintained
- All data classes (`CartItem`, `Discount`, `PaymentResult`)

### Refactoring Steps

1. **Extract Price Calculation**
   - Create a `PriceCalculator` class
   - Responsible for: calculating subtotals, discount amounts, and totals

2. **Extract Discount Management**
   - Create a `DiscountService` class
   - Responsible for: managing available discounts, validating codes

3. **Extract Order ID Generation**
   - Create an `OrderIdGenerator` class
   - Responsible for: generating unique order IDs

4. **Extract Payment Processing**
   - Create a `PaymentProcessor` class
   - Responsible for: processing payments and generating transaction IDs

5. **Extract Email Service**
   - Create an `EmailService` class
   - Responsible for: composing and sending order confirmation emails
   - Create an `OrderDetails` data class to pass order information

6. **Refactor ShoppingCart**
   - Keep only cart item management as its core responsibility
   - Inject the extracted services via constructor
   - Delegate to services while maintaining the same public API

### Expected Structure After Refactoring

```
src/main/java/com/example/shoppingcart/
├── ShoppingCartService.java      (interface - UNTOUCHED)
├── ShoppingCart.java              (refactored - coordinator)
├── PriceCalculator.java           (NEW)
├── DiscountService.java           (NEW)
├── OrderIdGenerator.java          (NEW)
├── PaymentProcessor.java          (NEW)
├── EmailService.java              (NEW)
├── OrderDetails.java              (NEW - data class)
├── CartItem.java                  (unchanged)
├── Discount.java                  (unchanged)
└── PaymentResult.java             (unchanged)
```

### Success Criteria

After refactoring:
1. ✅ All tests pass (run `mvn test`)
2. ✅ Each class has a single, clear responsibility
3. ✅ The `ShoppingCartService` interface remains unchanged
4. ✅ The `ShoppingCartIntegrationTest` remains unchanged and passes

### Evaluation

After completing the refactoring, evaluate how future changes would be easier:

**Before Refactoring:**
- Changing email format → Modify `ShoppingCart` class (risks breaking payment logic)
- Adding new payment method → Modify `ShoppingCart` class (risks breaking email logic)
- Changing discount rules → Modify `ShoppingCart` class (risks breaking price calculation)

**After Refactoring:**
- Changing email format → Modify only `EmailService` class (isolated change)
- Adding new payment method → Modify only `PaymentProcessor` class (isolated change)
- Changing discount rules → Modify only `DiscountService` class (isolated change)
- Adding tax calculation → Modify only `PriceCalculator` class (isolated change)
- Adding SMS notifications → Create new `SmsService` class (no changes to existing code)

### Benefits of Following SRP

1. **Easier Maintenance**: Changes are isolated to specific classes
2. **Better Testability**: Each responsibility can be tested independently
3. **Improved Reusability**: Services can be reused in other contexts
4. **Reduced Risk**: Changes to one feature don't affect others
5. **Clearer Code**: Each class has a clear, single purpose

## Getting Started

1. Review the current `ShoppingCart` implementation
2. Identify all responsibilities
3. Extract each responsibility into its own class
4. Refactor `ShoppingCart` to use the extracted services
5. Ensure all tests pass
6. Reflect on how the refactored code would make future changes easier

## Running Tests

```bash
# Run all tests
mvn test

# Run tests for this exercise specifically
mvn test -Dtest=ShoppingCartIntegrationTest
```

## Notes

- The code currently works correctly but violates SRP
- Focus on maintaining the public API contract defined by `ShoppingCartService`
- Use dependency injection to provide services to `ShoppingCart`
- Each extracted class should have a single, clear responsibility

