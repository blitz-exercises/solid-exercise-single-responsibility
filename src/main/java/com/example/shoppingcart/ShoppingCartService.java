package com.example.shoppingcart;

import java.util.List;

/**
 * Contract for shopping cart operations.
 * 
 * IMPORTANT: This interface should remain UNTOUCHED during refactoring exercises.
 * It defines the public API that ShoppingCart must implement.
 * 
 * Refactoring goal: Extract responsibilities from ShoppingCart implementation
 * into separate classes while maintaining this interface contract.
 */
public interface ShoppingCartService {
    // Cart management
    void addItem(String productName, double price, int quantity);
    List<CartItem> getItems();
    
    // Price calculations
    double calculateSubtotal();
    double calculateDiscountAmount();
    double calculateTotal();
    
    // Discount management
    boolean applyDiscount(String discountCode);
    String getAppliedDiscountCode();
    
    // Order processing
    void checkout(String customerEmail, String paymentMethod);
    String getOrderId();
    
    // Payment
    PaymentResult processPayment(String paymentMethod, double amount);
    PaymentResult getLastPaymentResult();
    
    // Email (for testing)
    void sendOrderConfirmationEmail(String customerEmail);
    String getEmailSentTo();
    
    // Order ID generation (used internally by checkout)
    String generateOrderId();
}

