package com.example.shoppingcart;

import java.util.UUID;
import java.util.logging.Logger;

public class PaymentProcessor {
    private static final Logger logger = Logger.getLogger(ShoppingCart.class.getName());
    private PaymentResult lastPaymentResult;

    public PaymentResult processPayment(String paymentMethod, double amount) {
        // Simulate payment processing
        PaymentResult lastPaymentResult = this.getLastPaymentResult();
        if (amount <= 0) {
            lastPaymentResult = new PaymentResult(false, null, "Invalid amount");
            return lastPaymentResult;
        }

        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        logger.info("Processing payment: " + paymentMethod + " for amount: " + amount);

        // Simulate payment processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        lastPaymentResult = new PaymentResult(true, transactionId, "Payment successful");
        logger.info("Payment processed successfully: " + transactionId);
        return lastPaymentResult;
    }

    public PaymentResult getLastPaymentResult() {
        return lastPaymentResult;
    }
}
