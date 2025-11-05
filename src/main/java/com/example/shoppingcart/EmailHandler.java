package com.example.shoppingcart;

import java.util.List;
import java.util.logging.Logger;

public class EmailHandler {
    private static final Logger logger = Logger.getLogger(EmailHandler.class.getName());
    private DiscountService discountService = new DiscountService();
    private PaymentProcessor paymentProcessor = new PaymentProcessor();
    private String emailSentTo;

    public void sendOrderConfirmationEmail(String customerEmail, String orderId,
            List<CartItem> items, String appliedDiscountCode, double total, double subTotal) {
        String subject = "Order Confirmation - " + orderId;
        String body = buildEmailBody(orderId, items, appliedDiscountCode, total, subTotal);

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

    private String buildEmailBody(String orderId, List<CartItem> items, String appliedDiscountCode,
            double total, double subTotal) {
        PaymentResult lastPaymentResult = paymentProcessor.getLastPaymentResult();
        StringBuilder body = new StringBuilder();
        body.append("Thank you for your order!\n\n");
        body.append("Order ID: ").append(orderId).append("\n\n");
        body.append("Items:\n");
        for (CartItem item : items) {
            body.append("- ").append(item.getProductName()).append(" x").append(item.getQuantity())
                    .append(" - $").append(item.getPrice()).append("\n");
        }
        body.append("\nSubtotal: $").append(String.format("%.2f", subTotal));
        if (appliedDiscountCode != null) {
            body.append("\nDiscount (").append(appliedDiscountCode).append("): -$").append(String
                    .format("%.2f", discountService.calculateDiscountAmount(items, subTotal)));
        }
        body.append("\nTotal: $").append(String.format("%.2f", total));

        if (lastPaymentResult != null && lastPaymentResult.isSuccess()) {
            body.append("\n\nTransaction ID: ").append(lastPaymentResult.getTransactionId());
        }

        return body.toString();
    }

    public String getEmailSentTo() {
        return emailSentTo;
    }
}
