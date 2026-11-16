# Inventory Management System (Java Swing)

A desktop inventory manager built with Java Swing that demonstrates core OOP concepts.

## OOP concepts used

| Concept | Where |
|---|---|
| **GUI** | `ui/MainFrame.java` (Swing: JFrame, JTable, buttons, dialogs) |
| **Constructors** | `Product` has two overloaded constructors (one chains to the other with `this(...)`); subclasses call `super(...)` |
| **Inheritance** | `PerishableProduct`, `ElectronicProduct`, `ClothingProduct` extend abstract class `Product` |
| **Polymorphism** | `getCategory()`, `getExtraInfo()`, `getTaxRate()`, `getSellingPrice()` behave differently per subclass; the manager and table only use the `Product` type |

## Features
- Add products (Perishable / Electronic / Clothing)
- Delete products, update quantity
- Search by ID, name or category
- Total stock value, selling price (auto 30% discount for perishables expiring within 3 days) and price with tax

## Project structure
```
src/inventory/
  Main.java
  model/    Product, PerishableProduct, ElectronicProduct, ClothingProduct
  service/  InventoryManager
  ui/       MainFrame
```

## Run
Requires JDK 17+.
```bash
mkdir out
javac -d out $(find src -name "*.java")
java -cp out inventory.Main
```
(Windows PowerShell: `javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`)
