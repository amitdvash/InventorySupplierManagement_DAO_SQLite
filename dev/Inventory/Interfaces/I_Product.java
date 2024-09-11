package dev.Inventory.Interfaces;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Product_Status;

import java.util.HashMap;
import java.util.List;

public interface I_Product
{
    // Getter for items
    HashMap<Integer, Item> getItems();

    // Setter for items
    void setItems(HashMap<Integer, Item> items);

    // Getter for quantity in store
    int getQuantity_in_store();

    // Setter for quantity in store
    void setQuantity_in_store(int quantity_in_store);

    // Getter for quantity in warehouse
    int getQuantity_in_warehouse();

    // Setter for quantity in warehouse
    void setQuantity_in_warehouse(int quantity_in_warehouse);

    // Getter for minimum quantity
    int getMin_quantity();

    // Setter for minimum quantity
    void setMin_quantity(int min_quantity);

    // Getter for product name
    String getName();

    // Setter for product name
    void setName(String name);

    // Getter for product category
    String getCategory();

    // Setter for product category
    void setCategory(String category);

    // Getter for product sub-category
    String getSub_category();

    // Setter for product sub-category
    void setSub_category(String sub_category);

    // Getter for product size
    double getSize();

    // Setter for product size
    void setSize(double size);

    // Getter for discount
    Discount getDiscount();

    // Setter for discount
    void setDiscount(Discount discount);

    // Getter for product status
    E_Product_Status getStatus();

    // Setter for product status
    void setStatus(E_Product_Status status);

    // Check if two products are equal
    boolean equals(Product product);

    // Check if the item matches the product
    boolean Matched_item_product(Item item);

    // Add an item to the product
    void addItem(Item item);

    // Remove an item from the product
    void removeItem(Item item);

    // Update product status based on quantity
    void updateStatus();

    // Update quantity after moving the item
    void updateQuantityAfterMove(E_Item_Place before, E_Item_Place after);

    // Move item to a new place
    void moveItemTo(Item item, E_Item_Place place);

    // Override toString to print product details
    @Override
    String toString();
}
