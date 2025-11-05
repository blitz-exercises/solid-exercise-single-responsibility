package com.example.shoppingcart;

import java.util.List;
import java.util.logging.Logger;

public class EmailHandler {
    private static final Logger logger = Logger.getLogger(ShoppingCart.class.getName());
    private DiscountService discountService = new DiscountService();
    private ShoppingCart shoppingCart = new ShoppingCart();
    private PaymentProcessor paymentProcessor = new PaymentProcessor();
    private String emailSentTo;

    public void sendOrderConfirmationEmail(String customerEmail, String orderId,
            List<CartItem> items, String appliedDiscountCode) {
        String subject = "Order Confirmation - " + orderId;
        String body = buildEmailBody(orderId, items, appliedDiscountCode);

        logger.info("Sending email to: " + customerEmail);
        logger.info("Subject: " + subject);
        logger.info("Body: " + body);

        // Simulate email sending
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        emailSentTo = customerEmail;
        logger.info("Email se nt successfully to: " + customerEmail);
    }

    private String buildEmailBody(String orderId, List<CartItem> items,
            String appliedDiscountCode) {
        StringBuilder body = new StringBuilder();
        body.append("Thank you for your order!\n\n");
        body.append("Order ID: ").append(orderId).append("\n\n");
        body.append("Items:\n");
        for (CartItem item : items) {
            body.append("- ").append(item.getProductName()).append(" x").append(item.getQuantity())
                    .append(" - $").append(item.getPrice()).append("\n");
        }
        body.append("\nSubtotal: $")
                .append(String.format("%.2f", shoppingCart.calculateSubtotal()));
        if (appliedDiscountCode != null) {
            body.append("\nDiscount (").append(appliedDiscountCode).append("): -$")
                    .append(String.format("%.2f", discountService.calculateDiscountAmount(items,
                            shoppingCart.calculateSubtotal())));
        }
        body.append("\nTotal: $").append(String.format("%.2f", shoppingCart.calculateTotal()));

        if (lastPaymentResult != null && lastPaymentResult.isSuccess()) {
            body.append("\n\nTransaction ID: ").append(lastPaymentResult.getTransactionId());
        }

        return body.toString();
    }

    public String getEmailSentTo() {
        return emailSentTo;
    }
}
