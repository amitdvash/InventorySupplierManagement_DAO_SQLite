package dev;

import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.DB.CreateTable;

import dev.Inventory.DB.SQLiteDB;

import java.sql.SQLException;
import java.time.LocalDate;

//import  dev.Inventory.Controllers.Controller_Menu.scanner;

public class main
{
    public static void main(String[] args) throws SQLException {
        // System.out.println("Hello World");
////        System.out.println(item1.toString());
//        Item item1 = new Item("P1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//        Item item2 = new Item("P2", 20, 30, "Manufacturer2", "Category2", "SubCategory2", 10, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Warehouse);
//////        System.out.println(item2.toString());
      /*   Product P1 = new Product ( "cola" , "drink" , "sparkling" , 300 ,4 , null);
        Product P2= new Product ( "cola" , "drink" , "sparkling" , 500 ,4 , null);
        Inventory inventory = Inventory.getInstance();
//        P1.addItem(item1);
//        inventory.addProduct(P1);
//        Discount discount = new Discount(10, LocalDate.now().minusDays(2), LocalDate.now().plusDays(10));inventory.addItem(item1);
//        inventory.applyDiscountToProduct(P1, discount);
//        System.out.println(inventory);
        inventory.addProduct(P1);
        inventory.addProduct(P2);
               Item item1 = new Item("cola", 10, 20, "Manufacturer1", "drink", "sparkling", 300, null, E_Item_Status.Available, E_Item_Place.Store);
               inventory.addItem(item1);
                Item item2 = new Item("cola", 10, 20, "Manufacturer1", "drink", "sparkling", 500, null, E_Item_Status.Available, E_Item_Place.Store);
                inventory.addItem(item2);
//                System.out.println(inventory);*/
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the start date (yyyy-mm-dd): ");
//        String startDate = scanner.next();
//        LocalDate Sdate = LocalDate.parse(startDate);
//        System.out.println("Enter the end date (yyyy-mm-dd): ");
//        String endDate = scanner.next();
//        LocalDate Edate = LocalDate.parse(endDate);
////        System.out.println(date.isBefore(LocalDate.now()));
//        Discount D = new Discount(10, Sdate, Edate);
//        System.out.println(D.isAvailable());
////
////
////
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
//        Inventory inventory = Inventory.getInstance();
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
//Discount discount = new Discount(10, LocalDate.now().minusDays(2), LocalDate.now().plusDays(10));inventory.addItem(item1);
//inventory.addProduct(P1);
//inventory.addProduct(P2);
//inventory.addItem(item1);
//inventory.addItem(item2);
////System.out.println(inventory.toString());
//inventory.applyDiscountToCategory("Category1", discount);
//System.out.println(inventory.toString());
//discount.setDiscountRate(20);
//inventory.applyDiscountToProduct(P2, discount);
//System.out.println(inventory.toString());
////for(Item item : inventory.getItemsByPlace(E_Item_Place.Warehouse))
////{
////    System.out.println(item.toString());
////}
////inventory.moveItemTo(item1, E_Item_Place.Warehouse);
////System.out.println(inventory.getItemsByPlace(E_Item_Place.Warehouse));
////System.out.println(inventory.toString());
        Item item1 = new Item("P1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
        Item item2 = new Item("P2", 20, 30, "Manufacturer2", "Category2", "SubCategory2", 10, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Warehouse);

        Product product1 = new Product("P1", "Category1", "SubCategory1", 10, 4, null);
        Product product2 = new Product("P2", "Category", "SubCategory1", 10, 4, null);

        CreateTable.initializeTables(SQLiteDB.connect());




//        inventory.addItemToProduct("P1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//        inventory.addItemToProduct("P1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//
//        inventory.addProduct("P1", "Category1", "SubCategory1", 10, 4);
//        inventory.addProduct("P2", "Category1", "SubCategory1", 10, 4);
//
//        //   inventory.addProduct("P2", "Category1", "SubCategory1", 10, 4);LocalDate.now();
//        inventory.addItemToProduct("P1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//        inventory.addItemToProduct("P2", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//
//        inventory.applyDiscountToSubCategory("SubCategory1",50,LocalDate.now(),LocalDate.now().plusDays(500));



    }
}

