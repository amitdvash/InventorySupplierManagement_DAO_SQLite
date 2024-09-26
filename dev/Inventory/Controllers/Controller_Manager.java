package dev.Inventory.Controllers;

import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;

import java.time.LocalDate;


public class Controller_Manager extends Controller_Worker
{
    static void ManagerMenu()
    {
        System.out.println("Registered as Manager successfully.");
        while(true)
        {
            System.out.println("===== Inventory Management System Menu - Manager menu =====");
            System.out.println("5. Generate Inventory Report (Manager)");
            System.out.println("6. Add Product (Manager)");
            System.out.println("7. Set a discount (Manager)");
            System.out.println("8. to the worker menu (Manager)");
            System.out.println("9. Exit");
            System.out.println("============================================");
            System.out.print("Select an option: ");
            String choice = scanner.next();
            scanner.nextLine();  // Consume newline
            switch (choice) {
                case "5":
                    generateInventoryReport();
                    break;
                case "6":
                    addProduct();
                    break;
                case "7":
                    setDiscount();
                    break;
                case "8":
                    WorkerMenu();
                    break;
                case "9":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }



    private static void setDiscount()
    {
        try {
            // Get discount details from the user
            System.out.print("Enter discount percentage: ");
            double discountPercentage = scanner.nextDouble();
            System.out.println("Enter the Start date (yyyy-mm-dd): ");
            String startString = scanner.next();
            LocalDate startDay = LocalDate.parse(startString);
            System.out.println("Enter the End date (yyyy-mm-dd): ");
            String endString = scanner.next();
            LocalDate endDay = LocalDate.parse(endString);

//        Discount discount = new Discount(discountPercentage, startDay, endDay);
            // Get the target for the discount (Product, Category, or Subcategory)
            System.out.println("Apply discount to: 1. Product  2. Category  3. Subcategory");
            String choice = scanner.next();
            scanner.nextLine();
            System.out.print("Enter the name: ");
            String name = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter the Category: ");
                    String Category = scanner.nextLine();
                    System.out.print("Enter the SubCategory: ");
                    String SubCategory = scanner.nextLine();
                    System.out.print("Enter the Size: ");
                    double Size = Double.parseDouble(scanner.nextLine());
//                Product product = managerController.inventory.getProduct(name, Category, SubCategory, Size);
                    applyDiscountToProduct(name, Category, SubCategory, Size, discountPercentage, startDay, endDay);
                    break;
                case "2":
                    applyDiscountToCategory(name, discountPercentage, startDay, endDay);
                    break;

                case "3":
                    applyDiscountToSubCategory(name, discountPercentage, startDay, endDay);
                    break;


                default:
                    System.out.println("Invalid choice.");
                    break; // Keep looping until a valid choice is made

            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }
    private static void generateInventoryReport()
    {

        try {
            System.out.println("enter filter : ");
            System.out.println("1. Category\n2. Sub-Category\n3.About to Finish\n4.About to Expire\n5. Expired\n6. All");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = scanner.next();
                    System.out.println("would you like to add a sub-category filter? (y/n)");
                    String subChoice = scanner.next();
                    if (subChoice.equals("y")) {
                        System.out.print("Enter sub-category: ");
                        String subCategory = scanner.next();
//                    List<Product> report = managerController.inventory.getProductsBySubCategory(subCategory);
                        System.out.println(inventory.getProductsByCategory(category).stream().filter(product -> product.getSub_category().equals(subCategory)).toList());
                    }
                    if (subChoice.equals("n")) {
//                    List<Product> report = managerController.inventory.getProductsByCategory(category);
                        System.out.println(inventory.getProductsByCategory(category));
                    }
//                List<Product> report = managerController.inventory.getProductsByCategory(category);
//                    System.out.println(inventory.getProductsByCategory(category));
                    break;
                case 2:
                    System.out.print("Enter sub-category: ");
                    String subCategory = scanner.next();
//                List<Product> report2 = managerController.inventory.getProductsBySubCategory(subCategory);
                    System.out.println(inventory.getProductsBySubCategory(subCategory));
                    break;
                case 3:
//                List<Product> report3 = managerController.inventory.getProductsByStatus(E_Product_Status.about_to_finish);
                    System.out.println(inventory.getProductsByStatus(E_Product_Status.about_to_finish));
                    break;
                case 4:
//                List<Item> report4 = managerController.inventory.getItemsByStatus(E_Item_Status.about_to_expire);
                    System.out.println(inventory.getItemsByStatus(E_Item_Status.about_to_expire));
                    break;
                case 5:
//                List<Item> report5 = managerController.inventory.getItemsByStatus(E_Item_Status.EXPIRED);
                    System.out.println(inventory.getItemsByStatus(E_Item_Status.EXPIRED));
                    break;
                case 6:
                    System.out.println(inventory);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }

    // Generate weekly reports for selected categories
    public void generateInventoryReport(String category) {
        var report = inventory.getProductsByName(category);
        System.out.println(report);
    }

    private static void addProduct() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter category: ");
            String category = scanner.nextLine();
            System.out.print("Enter sub-category: ");
            String subCategory = scanner.nextLine();
            System.out.print("Enter size: ");
            double size = scanner.nextDouble();
            System.out.print("Enter minimum quantity: ");
            int minQuantity = scanner.nextInt();

        Product newProduct = new Product(name, category, subCategory, size, minQuantity, null);
        productSQL.create(newProduct);
            add_product(name, category, subCategory, size, minQuantity);
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }

    public static void add_product(String name, String category, String subCategory, double size, int minQuantity)
    {
        inventory.addProduct(name, category, subCategory, size, minQuantity);
    }
    // Set a discount on a product

    public static void applyDiscountToProduct(String name, String Category, String SubCategory, double Size, double discountPercentage, LocalDate startDay, LocalDate endDay){
        inventory.applyDiscountToProduct(inventory.getProduct(name, Category, SubCategory, Size), discountPercentage , startDay , endDay);
    }

    public static void applyDiscountToCategory(String category, double discountPercentage, LocalDate startDay, LocalDate endDay){
        inventory.applyDiscountToCategory(category, discountPercentage, startDay, endDay);
    }

    public static void applyDiscountToSubCategory(String subcategory, double discountPercentage, LocalDate startDay, LocalDate endDay){
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

}