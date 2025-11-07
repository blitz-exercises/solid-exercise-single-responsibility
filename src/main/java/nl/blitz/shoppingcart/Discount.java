package nl.blitz.shoppingcart;

public class Discount {
    private final String code;
    private final double percentage;

    public Discount(String code, double percentage) {
        this.code = code;
        this.percentage = percentage;
    }

    public String getCode() {
        return code;
    }

    public double getPercentage() {
        return percentage;
    }
}

