package dev.Inventory.Data;

import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.time.LocalDate;

public class SystemInitializer {

    public static void initializeSystem() {
        // Retrieve the singleton instance of Inventory
        Inventory inventory = Inventory.getInstance();

        // Initialize sample products and items
        initializeProductsAndItems(inventory);

        // Additional initialization logic can go here...
        System.out.println("System initialized with sample data.");
    }

    private static void initializeProductsAndItems(Inventory inventory) {
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
        inventory.addItem(colaItem1.getName(), colaItem1.getCost_price(), colaItem1.getSelling_price(), colaItem1.getManufacturer(), colaItem1.getCategory(), colaItem1.getSub_category(), colaItem1.getSize(), colaItem1.getExpiry_date(), colaItem1.getStatus(), colaItem1.getPlace());
        inventory.addItem(colaItem2.getName(), colaItem2.getCost_price(), colaItem2.getSelling_price(), colaItem2.getManufacturer(), colaItem2.getCategory(), colaItem2.getSub_category(), colaItem2.getSize(), colaItem2.getExpiry_date(), colaItem2.getStatus(), colaItem2.getPlace());
        inventory.addItem(colaItem3.getName(), colaItem3.getCost_price(), colaItem3.getSelling_price(), colaItem3.getManufacturer(), colaItem3.getCategory(), colaItem3.getSub_category(), colaItem3.getSize(), colaItem3.getExpiry_date(), colaItem3.getStatus(), colaItem3.getPlace());

        // Add 3 items to the Chips product
        Item chipsItem1 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().plusDays(10), E_Item_Status.Available, E_Item_Place.Store);
        Item chipsItem2 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Warehouse);
        Item chipsItem3 = new Item("Chips", 5.0, 10.0, "Manufacturer2", "Snack", "Salty", 200, LocalDate.now().plusDays(20), E_Item_Status.Available, E_Item_Place.Store);
        inventory.addItem(chipsItem1.getName(), chipsItem1.getCost_price(), chipsItem1.getSelling_price(), chipsItem1.getManufacturer(), chipsItem1.getCategory(), chipsItem1.getSub_category(), chipsItem1.getSize(), chipsItem1.getExpiry_date(), chipsItem1.getStatus(), chipsItem1.getPlace());
        inventory.addItem(chipsItem2.getName(), chipsItem2.getCost_price(), chipsItem2.getSelling_price(), chipsItem2.getManufacturer(), chipsItem2.getCategory(), chipsItem2.getSub_category(), chipsItem2.getSize(), chipsItem2.getExpiry_date(), chipsItem2.getStatus(), chipsItem2.getPlace());
        inventory.addItem(chipsItem3.getName(), chipsItem3.getCost_price(), chipsItem3.getSelling_price(), chipsItem3.getManufacturer(), chipsItem3.getCategory(), chipsItem3.getSub_category(), chipsItem3.getSize(), chipsItem3.getExpiry_date(), chipsItem3.getStatus(), chipsItem3.getPlace());

        // Add 3 items to the Milk product
        Item milkItem1 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(7), E_Item_Status.Available, E_Item_Place.Store);
        Item milkItem2 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(10), E_Item_Status.Available, E_Item_Place.Warehouse);
        Item milkItem3 = new Item("Milk", 3.0, 5.0, "Manufacturer3", "Dairy", "Whole", 1000, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Store);
        inventory.addItem(milkItem1.getName(), milkItem1.getCost_price(), milkItem1.getSelling_price(), milkItem1.getManufacturer(), milkItem1.getCategory(), milkItem1.getSub_category(), milkItem1.getSize(), milkItem1.getExpiry_date(), milkItem1.getStatus(), milkItem1.getPlace());
        inventory.addItem(milkItem2.getName(), milkItem2.getCost_price(), milkItem2.getSelling_price(), milkItem2.getManufacturer(), milkItem2.getCategory(), milkItem2.getSub_category(), milkItem2.getSize(), milkItem2.getExpiry_date(), milkItem2.getStatus(), milkItem2.getPlace());
        inventory.addItem(milkItem3.getName(), milkItem3.getCost_price(), milkItem3.getSelling_price(), milkItem3.getManufacturer(), milkItem3.getCategory(), milkItem3.getSub_category(), milkItem3.getSize(), milkItem3.getExpiry_date(), milkItem3.getStatus(), milkItem3.getPlace());
    }
}
