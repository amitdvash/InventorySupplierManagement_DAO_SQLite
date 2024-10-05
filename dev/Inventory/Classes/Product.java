
package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Product_Status;

import java.sql.SQLException;

public class Product {

    private int id;  // Add an ID for the product, coming from the database
    private String name;
    private String category;
    private String sub_category;
    private double size;
    private int min_quantity;
    private int quantity_in_store;
    private int quantity_in_warehouse;
    private Discount discount;
    private E_Product_Status status;
    private int quantity;


    // Constructor for creating a new product
    public Product(String name, String category, String sub_category, double size, int min_quantity, Discount discount) throws SQLException {
        this.name = name;
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
        this.min_quantity = min_quantity;
        this.discount = discount;
        this.status = E_Product_Status.Out_of_stock;  // Default status
        this.quantity_in_store = 0;
        this.quantity_in_warehouse = 0;
    }


    // Full constructor with all fields
    public Product(String name, String category, String sub_category, double size, int min_quantity,
                   int quantity,int id) {
        this.name = name;
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
        this.min_quantity = min_quantity;
        this.id = id;
        this.quantity=quantity;

    }



    // Getters and Setters

    public int getQuantity() {
        return quantity;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(int min_quantity) {
        this.min_quantity = min_quantity;
    }

    public int getQuantity_in_store() {
        return quantity_in_store;
    }

    public void setQuantity_in_store(int quantity_in_store) {
        this.quantity_in_store = quantity_in_store;
    }

    public int getQuantity_in_warehouse() {
        return quantity_in_warehouse;
    }

    public void setQuantity_in_warehouse(int quantity_in_warehouse) {
        this.quantity_in_warehouse = quantity_in_warehouse;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public E_Product_Status getStatus() {
        return status;
    }

    public void setStatus(E_Product_Status status) {
        this.status = status;
    }

    // Update product status based on item quantities
    public void updateStatus() {
        if (quantity_in_store + quantity_in_warehouse > 0) {
            if (quantity_in_store + quantity_in_warehouse <= min_quantity) {
                status = E_Product_Status.about_to_finish;
            } else {
                status = E_Product_Status.Available;
            }
        } else {
            status = E_Product_Status.Out_of_stock;
        }
    }


    // Handle adding an item using the database
    public void addItem(Item item) throws SQLException {
        if (!matchesItem(item)) {
            throw new IllegalArgumentException(item.getName() + " does not match the product " + this.name);
        }


        // Update quantities
        if (item.getPlace() == E_Item_Place.Store) {
            quantity_in_store++;
        } else if (item.getPlace() == E_Item_Place.Warehouse) {
            quantity_in_warehouse++;
        }

        updateStatus();
    }

    // Handle removing an item using the database
    public void removeItem(Item item) throws SQLException {
        // Remove item via database

        // Update quantities
        if (item.getPlace() == E_Item_Place.Store) {
            quantity_in_store--;
        } else if (item.getPlace() == E_Item_Place.Warehouse) {
            quantity_in_warehouse--;
        }

        updateStatus();

    }








    // Check if an item matches the product details
    public boolean matchesItem(Item item) {
        return item.getName().equals(this.name) &&
                item.getCategory().equals(this.category) &&
                item.getSubCategory().equals(this.sub_category) &&
                item.getSize() == this.size;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", sub_category='" + sub_category + '\'' +
                ", size=" + size +
                ", min_quantity=" + min_quantity +
                ", quantity_in_store=" + quantity_in_store +
                ", quantity_in_warehouse=" + quantity_in_warehouse +
                ", discount=" + discount +
                ", status=" + status +
                '}';
    }
}
