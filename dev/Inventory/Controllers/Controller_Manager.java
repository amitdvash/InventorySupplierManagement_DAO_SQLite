

package dev.Inventory.Controllers;

import dev.Inventory.Classes.Product;
import dev.Inventory.ClassesDTO.DiscountDTO;
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
            System.out.println("4. Apply order");
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
                case "4":
                    createOrder();
                    break;
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




    private static void createOrder() {
        inventory.createOrder();
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
            // Check if start date is before end date
            if (startDay.isAfter(endDay)) {
                System.out.println("Error: Start date cannot be after the end date.");
                return;  // Exit method if the dates are invalid
            }

            // Check if today's date is between the start day and the end day
            LocalDate today = LocalDate.now();
            if (today.isBefore(startDay) || today.isAfter(endDay)) {
                System.out.println("Error: Today's date (" + today + ") is not within the discount period (" +
                        startDay + " to " + endDay + ").");
                return;  // Exit method if today's date is not within the discount period
            }

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
                    inventory.applyDiscountToProduct(name ,Category,SubCategory,Size,discountPercentage, startDay, endDay);
                    break;
                case "2":
                    inventory.applyDiscountToCategory(name,discountPercentage,startDay,endDay);
                    break;

                case "3":
                    inventory.applyDiscountToSubCategory(name, discountPercentage, startDay, endDay);
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
                    inventory.generateCategoryReport(category);
                    break;
                case 2:
                    System.out.print("Enter sub-category: ");
                    String subCategory = scanner.next();
                    inventory.generateSubCategoryReport(subCategory);
                    break;
                case 3:
                    inventory.generateAboutToFinishReport();
                    break;
                case 4:
                    inventory.generateAboutToExpireReport();
                    break;
                case 5:
                    inventory.generateExpiredReport();

                    break;
                case 6:
                    inventory.generateAllProductsReport();
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

            inventory.addProduct(name, category, subCategory, size, minQuantity);
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
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
