package inventory.service;

import inventory.model.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Holds all products and performs the inventory operations. Works with the Product base type only. */
public class InventoryManager {
    private final Map<String, Product> products = new LinkedHashMap<>();

    public void addProduct(Product p) {
        if (products.containsKey(p.getId())) {
            throw new IllegalArgumentException("A product with ID '" + p.getId() + "' already exists");
        }
        products.put(p.getId(), p);
    }

    public boolean removeProduct(String id) {
        return products.remove(id) != null;
    }

    public void updateQuantity(String id, int newQuantity) {
        Product p = products.get(id);
        if (p == null) throw new IllegalArgumentException("No product with ID '" + id + "'");
        p.setQuantity(newQuantity);
    }

    public List<Product> getAll() {
        return new ArrayList<>(products.values());
    }

    public List<Product> search(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product p : products.values()) {
            if (k.isEmpty()
                    || p.getId().toLowerCase().contains(k)
                    || p.getName().toLowerCase().contains(k)
                    || p.getCategory().toLowerCase().contains(k)) {
                result.add(p);
            }
        }
        return result;
    }

    public double getTotalStockValue() {
        double total = 0;
        for (Product p : products.values()) {
            total += p.getStockValue();   // polymorphic call
        }
        return total;
    }
}
