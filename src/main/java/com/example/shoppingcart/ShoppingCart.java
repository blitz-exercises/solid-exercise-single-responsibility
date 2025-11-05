package com.example.shoppingcart;

import java.util.ArrayList;
import java.util.List;
// import java.util.UUID;
import java.util.logging.Logger;

public class ShoppingCart implements ShoppingCartService {
    private static final Logger logger = Logger.getLogger(ShoppingCart.class.getName());

    private final List<CartItem> items;
    // private final List<Discount> availableDiscounts;
    private String appliedDiscountCode;
    private String orderId;
    // private String emailSentTo;
    // private PaymentResult lastPaymentResult;
    private PaymentProcessor paymentProcessor = new PaymentProcessor();
    private DiscountService discountService = new DiscountService();
    private EmailHandler emailHandler = new EmailHandler();

    public ShoppingCart() {
        this.items = new ArrayList<>();
        // this.availableDiscounts = new ArrayList<>();
        // initializeDiscounts();
    }

    // private void initializeDiscounts() {
    //     availableDiscounts.add(new Discount("SUMMER10", 10.0));
    //     availableDiscounts.add(new Discount("WELCOME20", 20.0));
    //     availableDiscounts.add(new Discount("VIP30", 30.0));
    // }

    public void addItem(String productName, double price, int quantity) {
        items.add(new CartItem(productName, price, quantity));
        logger.info("Added item: " + productName + " x" + quantity);
    }

    public double calculateSubtotal() {
        // return discountService.calculateSubtotal(items);
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public boolean applyDiscount(String discountCode) {
        // Discount discount = availableDiscounts.stream()
        //         .filter(d -> d.getCode().equals(discountCode)).findFirst().orElse(null);

        // if (discount != null) {
        //     appliedDiscountCode = discountCode;
        //     logger.info("Applied discount: " + discountCode);
        //     return true;
        // }
        // logger.warning("Invalid discount code: " + discountCode);
        // return false;
        return discountService.applyDiscount(discountCode);
    }

    public double calculateDiscountAmount() {
        // if (appliedDiscountCode == null) {
        //     return 0.0;
        // }
        // Discount discount = availableDiscounts.stream()
        //         .filter(d -> d.getCode().equals(appliedDiscountCode)).findFirst().orElse(null);

        // if (discount != null) {
        //     double subtotal = calculateSubtotal();
        //     return subtotal * (discount.getPercentage() / 100.0);
        // }
        // return 0.0;
        return discountService.calculateDiscountAmount(items, calculateSubtotal());
    }

    public double calculateTotal() {
        return calculateSubtotal() - calculateDiscountAmount();
    }

    public PaymentResult processPayment(String paymentMethod, double amount) {
        // Simulate payment processing
        // if (amount <= 0) {
        //     lastPaymentResult = new PaymentResult(false, null, "Invalid amount");
        //     return lastPaymentResult;
        // }

        // String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // logger.info("Processing payment: " + paymentMethod + " for amount: " + amount);

        // // Simulate payment processing delay
        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }

        // lastPaymentResult = new PaymentResult(true, transactionId, "Payment successful");
        // logger.info("Payment processed successfully: " + transactionId);

        // return lastPaymentResult;

        return paymentProcessor.processPayment(paymentMethod, amount);
    }

    public String generateOrderId() {
        // orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // logger.info("Generated order ID: " + orderId);
        // return orderId;
        return paymentProcessor.generateOrderId();
    }

    public void sendOrderConfirmationEmail(String customerEmail) {
        // String subject = "Order Confirmation - " + orderId;
        // String body = buildEmailBody();

        // logger.info("Sending email to: " + customerEmail);
        // logger.info("Subject: " + subject);
        // logger.info("Body: " + body);
        
        // // Simulate email sending
        // try {
        //     Thread.sleep(50);
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }
        
        // emailSentTo = customerEmail;
        // logger.info("Email sent successfully to: " + customerEmail);
        emailHandler.sendOrderConfirmationEmail(customerEmail, orderId, items, appliedDiscountCode, calculateTotal(), calculateSubtotal());
    }

    // private String buildEmailBody() {
        // StringBuilder body = new StringBuilder();
        // body.append("Thank you for your order!\n\n");
        // body.append("Order ID: ").append(orderId).append("\n\n");
        // body.append("Items:\n");
        // for (CartItem item : items) {
        //     body.append("- ").append(item.getProductName()).append(" x").append(item.getQuantity())
        //             .append(" - $").append(item.getPrice()).append("\n");
        // }
        // body.append("\nSubtotal: $").append(String.format("%.2f", calculateSubtotal()));
        // if (appliedDiscountCode != null) {
        //     body.append("\nDiscount (").append(appliedDiscountCode).append("): -$")
        //             .append(String.format("%.2f", calculateDiscountAmount()));
        // }
        // body.append("\nTotal: $").append(String.format("%.2f", calculateTotal()));

        // if (lastPaymentResult != null && lastPaymentResult.isSuccess()) {
        //     body.append("\n\nTransaction ID: ").append(lastPaymentResult.getTransactionId());
        // }

        // return body.toString();
    // }

    public void checkout(String customerEmail, String paymentMethod) {
        // Generate order ID
        generateOrderId();

        // Calculate total
        double total = calculateTotal();

        // Process payment needs to be move
        paymentProcessor.processPayment(paymentMethod, total);

        // Send confirmation email needs to be move
        sendOrderConfirmationEmail(customerEmail);

        logger.info("Checkout completed for order: " + orderId);
    }

    // Getters for testing
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getAppliedDiscountCode() {
        return discountService.getAppliedDiscountCode();
        // return appliedDiscountCode;
    }

    public String getOrderId() {
        return paymentProcessor.getOrderId();
    }

    public String getEmailSentTo() {
        return emailHandler.getEmailSentTo();
    }

    public PaymentResult getLastPaymentResult() {
        return paymentProcessor.getLastPaymentResult();
    }
}

