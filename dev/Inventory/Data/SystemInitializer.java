
package dev.Inventory.Data;

import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class SystemInitializer {


    private Connection connection;

    public static void initializeSystem(Inventory inventory) throws SQLException {


        initializeProductsAndItems(inventory);

        System.out.println("System initialized with sample data.");
    }

    private static void initializeProductsAndItems(Inventory inventory) throws SQLException {
        // Example products
        Product cola = new Product("Cola", "Drink", "Sparkling", 500, 10, null);
        Product chips = new Product("Chips", "Snack", "Salty", 200, 5, null);
        Product milk = new Product("Milk", "Dairy", "Whole", 1000, 8, null);



        // Add products to inventory
        inventory.addProduct(cola.getName(), cola.getCategory(), cola.getSub_category(), cola.getSize(), cola.getMin_quantity());
        inventory.addProduct(chips.getName(), chips.getCategory(), chips.getSub_category(), chips.getSize(), chips.getMin_quantity());
        inventory.addProduct(milk.getName(), milk.getCategory(), milk.getSub_category(), milk.getSize(), milk.getMin_quantity());





        // Add 3 items to the Cola product
        Item colaItem1 = new Item("Cola", 10.0, 15.0, "Manufacturer1", "Drink", "Sparkling", 500, LocalDate.now().plusDays(10), E_Item_Status.Available, E_Item_Place.Store);
        Item colaItem2 = new Item("Cola", 10.0, 15.0, "Manufacturer1", "Drink", "Sparkling", 500, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Warehouse);
        Item colaItem3 = new Item("Cola", 10.0, 15.0, "Manufacturer1", "Drink", "Sparkling", 500, LocalDate.now().plusDays(20), E_Item_Status.Available, E_Item_Place.Store);

// Ensure the category and sub-category match the product details
        inventory.addItemToProduct(colaItem1.getName(), colaItem1.getCostPrice(), colaItem1.getSellingPrice(), colaItem1.getManufacturer(), colaItem1.getCategory(), colaItem1.getSubCategory(), colaItem1.getSize(), colaItem1.getExpiryDate(), colaItem1.getStatus(), colaItem1.getPlace());
        inventory.addItemToProduct(colaItem2.getName(), colaItem2.getCostPrice(), colaItem2.getSellingPrice(), colaItem2.getManufacturer(), colaItem2.getCategory(), colaItem2.getSubCategory(), colaItem2.getSize(), colaItem2.getExpiryDate(), colaItem2.getStatus(), colaItem2.getPlace());
        inventory.addItemToProduct(colaItem3.getName(), colaItem3.getCostPrice(), colaItem3.getSellingPrice(), colaItem3.getManufacturer(), colaItem3.getCategory(), colaItem3.getSubCategory(), colaItem3.getSize(), colaItem3.getExpiryDate(), colaItem3.getStatus(), colaItem3.getPlace());

        // Add 3 items to the Chips product
        Item chipsItem1 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().minusDays(23), E_Item_Status.Available, E_Item_Place.Store);
        Item chipsItem2 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Warehouse);
        Item chipsItem3 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().plusDays(20), E_Item_Status.Available, E_Item_Place.Store);
        inventory.addItemToProduct(chipsItem1.getName(), chipsItem1.getCostPrice(), chipsItem1.getSellingPrice(), chipsItem1.getManufacturer(), chipsItem1.getCategory(), chipsItem1.getSubCategory(), chipsItem1.getSize(), chipsItem1.getExpiryDate(), chipsItem1.getStatus(), chipsItem1.getPlace());
        inventory.addItemToProduct(chipsItem2.getName(), chipsItem2.getCostPrice(), chipsItem2.getSellingPrice(), chipsItem2.getManufacturer(), chipsItem2.getCategory(), chipsItem2.getSubCategory(), chipsItem2.getSize(), chipsItem2.getExpiryDate(), chipsItem2.getStatus(), chipsItem2.getPlace());
        inventory.addItemToProduct(chipsItem3.getName(), chipsItem3.getCostPrice(), chipsItem3.getSellingPrice(), chipsItem3.getManufacturer(), chipsItem3.getCategory(), chipsItem3.getSubCategory(), chipsItem3.getSize(), chipsItem3.getExpiryDate(), chipsItem3.getStatus(), chipsItem3.getPlace());

        // Add 3 items to the Milk product
        Item milkItem1 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(7), E_Item_Status.Available, E_Item_Place.Store);
        Item milkItem2 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(10), E_Item_Status.Available, E_Item_Place.Warehouse);
        Item milkItem3 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Store);
        inventory.addItemToProduct(milkItem1.getName(), milkItem1.getCostPrice(), milkItem1.getSellingPrice(), milkItem1.getManufacturer(), milkItem1.getCategory(), milkItem1.getSubCategory(), milkItem1.getSize(), milkItem1.getExpiryDate(), milkItem1.getStatus(), milkItem1.getPlace());
        inventory.addItemToProduct(milkItem2.getName(), milkItem2.getCostPrice(), milkItem2.getSellingPrice(), milkItem2.getManufacturer(), milkItem2.getCategory(), milkItem2.getSubCategory(), milkItem2.getSize(), milkItem2.getExpiryDate(), milkItem2.getStatus(), milkItem2.getPlace());
        inventory.addItemToProduct(milkItem3.getName(), milkItem3.getCostPrice(), milkItem3.getSellingPrice(), milkItem3.getManufacturer(), milkItem3.getCategory(), milkItem3.getSubCategory(), milkItem3.getSize(), milkItem3.getExpiryDate(), milkItem3.getStatus(), milkItem3.getPlace());
    }
}
