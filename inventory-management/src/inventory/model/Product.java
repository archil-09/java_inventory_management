package inventory.model;

/**
 * Base class for every item in the inventory.
 * Demonstrates: abstract class, constructors (overloading + chaining),
 * encapsulation, and abstract methods that subclasses must override.
 */
public abstract class Product {
    private final String id;
    private String name;
    private double price;
    private int quantity;

    // Constructor 1: full details
    protected Product(String id, String name, double price, int quantity) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.id = id.trim();
        this.name = name.trim();
        this.price = price;
        this.quantity = quantity;
    }

    // Constructor 2: overloaded, chains to constructor 1 with quantity = 0
    protected Product(String id, String name, double price) {
        this(id, name, price, 0);
    }

    // ---- Abstract methods: each subclass provides its own behaviour (polymorphism) ----
    public abstract String getCategory();
    public abstract String getExtraInfo();
    public abstract double getTaxRate();

    // ---- Overridable methods ----
    /** Price the customer actually pays before tax. Subclasses may override (e.g. discounts). */
    public double getSellingPrice() {
        return price;
    }

    public double getPriceWithTax() {
        return getSellingPrice() * (1 + getTaxRate());
    }

    public double getStockValue() {
        return price * quantity;
    }

    // ---- Getters / setters ----
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - price: %.2f, qty: %d, %s",
                id, name, getCategory(), price, quantity, getExtraInfo());
    }
}
