package dev.Inventory.Controllers;

import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;

public class Controller_Inventory {

//    I_Inventory inventory;
//
//    public Controller_Worker(I_Inventory inventory)
//    {
//        this.inventory = inventory;
//    }
//
//    public void addProduct()
//    {
//        inventory.addProduct();
//    }
    protected Inventory inventory;

    // Constructor
    public Controller_Inventory() {
        // Fetch the inventory singleton instance
        this.inventory = Inventory.getInstance();
    }

    public void addItem(Item item) {
        inventory.addItem(item);
    }

    public void removeItem(Item item){
        inventory.removeItem(item);
    }

    public void moveItem(Item item, E_Item_Place new_palace){
        inventory.moveItemTo(item, new_palace);
    }

    public void viewProductDetails(Product product){
        System.out.println(inventory.getProductByName(product.getName()).toString());
    }

}
