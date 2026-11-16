package inventory.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Food / dairy etc. Gets a clearance discount when close to expiry. */
public class PerishableProduct extends Product {
    private final LocalDate expiryDate;

    public PerishableProduct(String id, String name, double price, int quantity, LocalDate expiryDate) {
        super(id, name, price, quantity);   // calls the parent constructor
        if (expiryDate == null) throw new IllegalArgumentException("Expiry date is required");
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() { return expiryDate; }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String getCategory() { return "Perishable"; }

    @Override
    public String getExtraInfo() {
        return isExpired() ? "EXPIRED on " + expiryDate : "Expires: " + expiryDate;
    }

    @Override
    public double getTaxRate() { return 0.05; }

    /** 30% off when 3 days or fewer remain (polymorphic override). */
    @Override
    public double getSellingPrice() {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        return daysLeft <= 3 ? getPrice() * 0.70 : getPrice();
    }
}
