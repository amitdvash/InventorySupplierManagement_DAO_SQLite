package dev.Inventory.Controllers;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;

import java.time.LocalDate;


public class Controller_Manager extends Controller_Worker
{



    // Generate weekly reports for selected categories
    public void generateInventoryReport(String category) {
        var report = inventory.getProductsByName(category);
        System.out.println(report);
    }

    public void add_product(String name, String category, String subCategory, double size, int minQuantity)
    {
        inventory.addProduct(name, category, subCategory, size, minQuantity);
    }
    // Set a discount on a product

    public void applyDiscountToProduct(String name, String Category, String SubCategory, double Size, double discountPercentage, LocalDate startDay, LocalDate endDay){
        inventory.applyDiscountToProduct(inventory.getProduct(name, Category, SubCategory, Size), discountPercentage , startDay , endDay);
    }

     public void applyDiscountToCategory(String category, double discountPercentage, LocalDate startDay, LocalDate endDay){
        inventory.applyDiscountToCategory(category, discountPercentage, startDay, endDay);
     }

    public void applyDiscountToSubCategory(String subcategory, double discountPercentage, LocalDate startDay, LocalDate endDay){
        inventory.applyDiscountToSubCategory(subcategory, discountPercentage, startDay, endDay);
    }

    public Product findOrProduct(String name, String category, String subCategory, double size) {
        return inventory.findOrProduct(name, category, subCategory, size);

    }


    public boolean registerManager(String name1, String password)
    {
        if (password.equals("1111"))
            return true;
        else
            return false;
//            return inventory.registerManager(name1, password);
    }
    public boolean registerWorker(String name1, String password)
    {
        if (password.equals("1111"))
            return true;
        else
            return false;
//            return inventory.registerWorker(name1, password);
    }
}
