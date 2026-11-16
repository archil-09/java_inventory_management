package inventory.model;

/** Gadgets and appliances, with a warranty period. */
public class ElectronicProduct extends Product {
    private final int warrantyMonths;

    public ElectronicProduct(String id, String name, double price, int quantity, int warrantyMonths) {
        super(id, name, price, quantity);
        if (warrantyMonths < 0) throw new IllegalArgumentException("Warranty cannot be negative");
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() { return warrantyMonths; }

    @Override
    public String getCategory() { return "Electronic"; }

    @Override
    public String getExtraInfo() { return "Warranty: " + warrantyMonths + " months"; }

    @Override
    public double getTaxRate() { return 0.18; }
}
