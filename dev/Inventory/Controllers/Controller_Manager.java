package dev.Inventory.Controllers;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;


public class Controller_Manager extends Controller_Worker
{



    // Generate weekly reports for selected categories
    public void generateInventoryReport(String category) {
        var report = inventory.getProductByName(category);
        System.out.println(report);
    }

    public void add_product(Product product){
        inventory.addProduct(product);
    }
    // Set a discount on a product

    public void applyDiscountToProduct(Product product, Discount discount){
        inventory.applyDiscountToProduct(product, discount);
    }

     public void applyDiscountToCategory(String category, Discount discount){
        inventory.applyDiscountToCategory(category, discount);
     }

    public void applyDiscountToSubCategory(String subcategory, Discount discount){
        inventory.applyDiscountToSubCategory(subcategory, discount);
    }

    public Product findOrProduct(String name, String category, String subCategory, double size) {
        return inventory.findOrProduct(name, category, subCategory, size);

    }




}
