package com.example.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the complete purchase flow.
 * 
 * IMPORTANT: This test class should remain UNTOUCHED during refactoring exercises.
 * The ShoppingCartService interface defines the public API contract that must be maintained.
 * 
 * Classes that should remain UNTOUCHED:
 * - ShoppingCartService (interface) - defines the immutable contract
 * - ShoppingCartIntegrationTest (this class) - verifies the contract is maintained
 * 
 * Refactoring goal: Extract responsibilities from ShoppingCart implementation
 * into separate classes while maintaining the ShoppingCartService interface contract.
 */
public class ShoppingCartIntegrationTest {
    private ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    public void testCompletePurchaseFlow() {
        // Step 1: Add items to cart
        cart.addItem("Laptop", 999.99, 1);
        cart.addItem("Wireless Mouse", 29.99, 2);
        cart.addItem("USB-C Cable", 19.99, 1);

        // Step 2: Verify items added correctly
        assertEquals(3, cart.getItems().size());
        assertEquals(1079.97, cart.calculateSubtotal(), 0.01);

        // Step 3: Apply discount
        assertTrue(cart.applyDiscount("SUMMER10"));
        assertEquals("SUMMER10", cart.getAppliedDiscountCode());

        // Step 4: Verify discount calculation
        double subtotal = cart.calculateSubtotal();
        double discountAmount = cart.calculateDiscountAmount();
        double expectedDiscount = subtotal * 0.10;
        assertEquals(expectedDiscount, discountAmount, 0.01);

        // Step 5: Complete checkout
        String customerEmail = "customer@example.com";
        cart.checkout(customerEmail, "CREDIT_CARD");

        // Step 6: Verify order was created
        assertNotNull(cart.getOrderId());
        assertTrue(cart.getOrderId().startsWith("ORD-"));

        // Step 7: Verify payment was processed
        assertNotNull(cart.getLastPaymentResult());
        assertTrue(cart.getLastPaymentResult().isSuccess());
        assertNotNull(cart.getLastPaymentResult().getTransactionId());
        assertTrue(cart.getLastPaymentResult().getTransactionId().startsWith("TXN-"));

        // Step 8: Verify email was sent
        assertEquals(customerEmail, cart.getEmailSentTo());

        // Step 9: Verify total calculation
        double expectedTotal = subtotal - discountAmount;
        assertEquals(expectedTotal, cart.calculateTotal(), 0.01);
    }
}

