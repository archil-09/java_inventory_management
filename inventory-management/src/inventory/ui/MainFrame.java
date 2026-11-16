package inventory.ui;

import inventory.model.*;
import inventory.service.InventoryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/** Swing GUI for the inventory system. */
public class MainFrame extends JFrame {
    private final InventoryManager manager = new InventoryManager();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Category", "Price", "Qty", "Selling Price", "With Tax", "Details"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(10);
    private final JTextField priceField = new JTextField(10);
    private final JTextField qtyField = new JTextField(10);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Perishable", "Electronic", "Clothing"});
    private final JLabel extraLabel = new JLabel("Expiry (yyyy-MM-dd):");
    private final JTextField extraField = new JTextField(10);
    private final JTextField searchField = new JTextField(14);
    private final JLabel statusLabel = new JLabel(" ");

    public MainFrame() {
        super("Inventory Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        typeBox.addActionListener(e -> updateExtraLabel());
        loadSampleData();
        refreshTable(manager.getAll());
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Add Product"));
        p.add(new JLabel("ID:"));    p.add(idField);
        p.add(new JLabel("Name:"));  p.add(nameField);
        p.add(new JLabel("Price:")); p.add(priceField);
        p.add(new JLabel("Qty:"));   p.add(qtyField);
        p.add(new JLabel("Type:"));  p.add(typeBox);
        p.add(extraLabel);           p.add(extraField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> addProduct());
        p.add(addBtn);
        return p;
    }

    private JPanel buildBottomPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

        JButton updateBtn = new JButton("Update Quantity");
        updateBtn.addActionListener(e -> updateQuantity());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelected());
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refreshTable(manager.search(searchField.getText())));
        JButton resetBtn = new JButton("Show All");
        resetBtn.addActionListener(e -> { searchField.setText(""); refreshTable(manager.getAll()); });

        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(new JLabel("   Search:"));
        buttons.add(searchField);
        buttons.add(searchBtn);
        buttons.add(resetBtn);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 6, 10));
        p.add(buttons, BorderLayout.NORTH);
        p.add(statusLabel, BorderLayout.SOUTH);
        return p;
    }

    private void updateExtraLabel() {
        switch ((String) typeBox.getSelectedItem()) {
            case "Perishable" -> extraLabel.setText("Expiry (yyyy-MM-dd):");
            case "Electronic" -> extraLabel.setText("Warranty (months):");
            default -> extraLabel.setText("Size (S/M/L/XL):");
        }
        extraField.setText("");
    }

    private void addProduct() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());
            String extra = extraField.getText().trim();

            Product product;   // parent-type reference, child-type object
            switch ((String) typeBox.getSelectedItem()) {
                case "Perishable" -> product = new PerishableProduct(id, name, price, qty, LocalDate.parse(extra));
                case "Electronic" -> product = new ElectronicProduct(id, name, price, qty, Integer.parseInt(extra));
                default -> product = new ClothingProduct(id, name, price, qty, extra);
            }
            manager.addProduct(product);
            clearForm();
            refreshTable(manager.getAll());
        } catch (NumberFormatException ex) {
            error("Price, quantity and warranty must be valid numbers.");
        } catch (DateTimeParseException ex) {
            error("Expiry date must be in yyyy-MM-dd format, e.g. 2026-12-31.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void updateQuantity() {
        String id = selectedId();
        if (id == null) return;
        String input = JOptionPane.showInputDialog(this, "New quantity for " + id + ":");
        if (input == null) return;
        try {
            manager.updateQuantity(id, Integer.parseInt(input.trim()));
            refreshTable(manager.getAll());
        } catch (NumberFormatException ex) {
            error("Enter a whole number.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void deleteSelected() {
        String id = selectedId();
        if (id == null) return;
        int ok = JOptionPane.showConfirmDialog(this, "Delete product " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            manager.removeProduct(id);
            refreshTable(manager.getAll());
        }
    }

    private String selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            error("Select a product in the table first.");
            return null;
        }
        return (String) tableModel.getValueAt(row, 0);
    }

    private void refreshTable(List<Product> items) {
        tableModel.setRowCount(0);
        for (Product p : items) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategory(),
                    String.format("%.2f", p.getPrice()), p.getQuantity(),
                    String.format("%.2f", p.getSellingPrice()),
                    String.format("%.2f", p.getPriceWithTax()),
                    p.getExtraInfo()});
        }
        statusLabel.setText(String.format("Showing %d product(s)  |  Total stock value: %.2f",
                items.size(), manager.getTotalStockValue()));
    }

    private void clearForm() {
        idField.setText(""); nameField.setText(""); priceField.setText("");
        qtyField.setText(""); extraField.setText("");
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void loadSampleData() {
        manager.addProduct(new PerishableProduct("P001", "Milk 1L", 60, 40, LocalDate.now().plusDays(2)));
        manager.addProduct(new PerishableProduct("P002", "Cheese", 220, 15, LocalDate.now().plusDays(30)));
        manager.addProduct(new ElectronicProduct("E001", "Bluetooth Speaker", 1999, 12, 12));
        manager.addProduct(new ClothingProduct("C001", "Cotton T-Shirt", 499, 30, "M"));
    }
}
