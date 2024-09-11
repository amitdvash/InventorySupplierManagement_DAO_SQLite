package dev.Inventory.Interfaces;

import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Product_Status;

import java.util.List;

public interface I_Inventory {


    // Adding an item to the inventory
    public void addItem(Item item);

    // Removing an item from the inventory
    public void removeItem(Item item);

    // Adding a product to the inventory
    public void addProduct(Product product);

    // Retrieving a product by name
    public Product getProductByName(String name);

    // Retrieving a list of products by status
    public List<Product> getProductsByStatus(E_Product_Status status);

    // Retrieving a list of products by category
    public List<Product> getProductsByCategory(String category);

    // Retrieving a list of products by sub-category
    public List<Product> getProductsBySubCategory(String subcategory);

    // Moving an item to a different place in the inventory
    public void moveItemTo(Item item, E_Item_Place place);

    // Override of the `toString()` method to print out the inventory details
    @Override
    public String toString();
}
