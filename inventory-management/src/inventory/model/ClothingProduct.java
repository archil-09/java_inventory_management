package inventory.model;

/** Apparel, with a size. */
public class ClothingProduct extends Product {
    private final String size;

    public ClothingProduct(String id, String name, double price, int quantity, String size) {
        super(id, name, price, quantity);
        if (size == null || size.isBlank()) throw new IllegalArgumentException("Size is required");
        this.size = size.trim().toUpperCase();
    }

    public String getSize() { return size; }

    @Override
    public String getCategory() { return "Clothing"; }

    @Override
    public String getExtraInfo() { return "Size: " + size; }

    @Override
    public double getTaxRate() { return 0.12; }
}
