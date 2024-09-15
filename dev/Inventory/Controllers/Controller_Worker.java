package dev.Inventory.Controllers;

import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.time.LocalDate;

//to be implemented
//for limit access to the Worker only
public class Controller_Worker {

    protected Inventory inventory;

    // Constructor
    public Controller_Worker() {
        // Fetch the inventory singleton instance
        this.inventory = Inventory.getInstance();
    }

    public void addItem(String name, double costPrice, double sellingPrice, String manufacturer, String category, String subCategory, double size, LocalDate expiry, E_Item_Status Status, E_Item_Place place) {
     inventory.addItem(name, costPrice, sellingPrice, manufacturer, category, subCategory, size, expiry, Status, place);
    }

    public void removeItem(Item item){
        inventory.removeItem(item);
    }

    public void moveItem(Item item, E_Item_Place new_palace){
        inventory.moveItemTo(item, new_palace);
    }

//    public void viewProductDetails(Product product){
//        System.out.println(inventory.getProduct(product).toString());
//    }

    // Generate weekly reports for selected categories
//    public void generateInventoryReport(String category) {
//        var report = inventory.getProductByName(category);
//        System.out.println(report);
//    }

    public Item findItem(String name, String category, String subCategory, double size, E_Item_Place place) {
        return inventory.findItem(name, category, subCategory , size, place);
    }

//    public Product find_product_string(String name){
//        return inventory.getProductByName(name);
//    }

}
