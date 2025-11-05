package com.example.shoppingcart;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscountService {
    private static final Logger logger = Logger.getLogger(DiscountService.class.getName());
    private final List<Discount> availableDiscounts;
    private String appliedDiscountCode;

    public DiscountService() {
        // this.items = new ArrayList<>();
        this.availableDiscounts = new ArrayList<>();
        initializeDiscounts();
    }

    private void initializeDiscounts() {
        availableDiscounts.add(new Discount("SUMMER10", 10.0));
        availableDiscounts.add(new Discount("WELCOME20", 20.0));
        availableDiscounts.add(new Discount("VIP30", 30.0));
    }

    public double calculateDiscountAmount(List<CartItem> items, Double subTotaal) {
        if (appliedDiscountCode == null) {
            return 0.0;
        }
        Discount discount = availableDiscounts.stream()
                .filter(d -> d.getCode().equals(appliedDiscountCode)).findFirst().orElse(null);

        if (discount != null) {
            double subtotal = subTotaal;
            return subtotal * (discount.getPercentage() / 100.0);
        }
        return 0.0;
    }

    public boolean applyDiscount(String discountCode) {
        Discount discount = availableDiscounts.stream()
                .filter(d -> d.getCode().equals(discountCode)).findFirst().orElse(null);

        if (discount != null) {
            appliedDiscountCode = discountCode;
            logger.info("Applied discount: " + discountCode);
            return true;
        }
        logger.warning("Invalid discount code: " + discountCode);
        return false;
    }

    // public double calculateSubtotal(List<CartItem> items) {
    //     return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    // }

    public String getAppliedDiscountCode() {
        return appliedDiscountCode;
    }
}
