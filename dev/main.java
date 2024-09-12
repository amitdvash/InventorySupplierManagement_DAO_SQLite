package dev;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Date;

public class main
{
    public static void main(String[] args)

    {
        System.out.println("Hello World");
        Item item1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.about_to_expire, E_Item_Place.Warehouse);
        Item item1_1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        Item item1_1_1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        Item item1_1_1_1_1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        Item item1_1_11 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);

        Item item2 = new Item("Item2", 20, 30, "Manufacturer2", "Category2", "SubCategory2", 10, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Warehouse);
//        System.out.println(item1.toString());
//        System.out.println(item2.toString());
         Product P1 = new Product ( "Item1" , "Category1" , "SubCategory1" , 10 ,2 , null);
        Product P2 = new Product ( "Item1" , "Category1" , "SubCategory1" , 10 ,2 , null);
        Discount discount=new Discount(50,LocalDate.ofYearDay(2024, 20),LocalDate.ofYearDay(2024, 40));
        //System.out.println(P1.equals(P2));
      //  System.out.println(P1.Matched_item_product(item1));
        P1.addItem(item1);
        P1.addItem(item1_1);
        P1.addItem(item1_1_1);
        P1.addItem(item1_1_1_1_1);
        P1.addItem(item1_1_11);
//        System.out.println(P1);
        P1.moveItemTo(item1, E_Item_Place.Store);
        P1.removeItem(item1_1);
        P1.setDiscount(discount);
        System.out.println(P1);
//         System.out.println(P1.toString());
//        P1.addItem(item1);
//        P1.addItem(item2);
//        System.out.println(P1.toString());
//        P1.moveItemTo(item1, E_Item_Place.Warehouse);
//        System.out.println(P1.toString());
//        P1.removeItem(item1);
//        System.out.println(P1.toString());
//        P1.removeItem(item2);
//        System.out.println(P1.toString());
//        System.out.println("\n\n\n\n\n");
        Inventory inventory = Inventory.getInstance();
//        inventory.addItem(item1);
//        System.out.println(inventory.toString());
//        inventory.addItem(item2);
//        System.out.println(inventory.toString());
//        inventory.addProduct(P1);
//        System.out.println(inventory.toString());
//        inventory.addItem(item1);
//        inventory.addItem(item2);
//        inventory.removeItem(item1);
//        inventory.removeItem(item1);
//        System.out.println(inventory.getProductsByStatus(E_Product_Status.about_to_finish));
//        System.out.println(inventory.toString());
//        System.out.println(inventory.getProductsBySubCategory("SubCategory2"));
//        inventory.moveItemTo(item1, E_Item_Place.Store);
//        System.out.println(inventory.toString());
//        inventory.moveItemTo(item1.getId(), E_Item_Place.Warehouse);
//        System.out.println(inventory.toString());
//       System.out.println(inventory.getProductByName("Item1").toString());


    }
}

