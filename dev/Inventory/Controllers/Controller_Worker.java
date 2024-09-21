package dev.Inventory.Controllers;
import dev.Inventory.Classes.Item;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.time.LocalDate;

public class Controller_Worker extends Controller_Menu
{


//    private static Scanner scanner = new Scanner(System.in);

    // Constructor
    protected static void WorkerMenu()
    {
        System.out.println("Registered as Worker successfully.");
        while(true)
        {
            System.out.println("===== Inventory Management System Menu - Worker menu =====");
            System.out.println("1. Add Item (Worker)");
            System.out.println("2. Remove Item (Worker)");
            System.out.println("3. Move Item (Worker)");
            System.out.println("4. View Product Details (Worker)");
            System.out.println("9. Exit");
            System.out.println("============================================");
            System.out.print("Select an option: ");
            String choice = scanner.next();
            scanner.nextLine();  // Consume newline
            switch (choice) {
                case "1":
                    addItem();
                    break;
                case "2":
                    removeItem();
                    break;
                case "3":
                    moveItem();
                    break;
                case "4":
                    viewProductDetails();
                    break;
                case "9":
                    System.out.println("Exiting...");;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private static void addItem() {
        try {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine();
            System.out.print("Enter category: ");
            String category = scanner.next();
            System.out.print("Enter sub-category: ");
            String subCategory = scanner.next();
            System.out.print("Enter size: ");
            double size = scanner.nextDouble();
            System.out.print("Enter cost price: ");
            double costPrice = scanner.nextDouble();
            System.out.print("Enter selling price: ");
            double sellingPrice = scanner.nextDouble();
            System.out.print("Enter manufacturer: ");
            String manufacturer = scanner.next();
            System.out.print("Enter item place (1: Store, 2: Warehouse): ");
            int placeChoice = scanner.nextInt();
            System.out.println("Enter the expiry date (yyyy-mm-dd): ");
            String expiryDate = scanner.next();
            LocalDate expiry = LocalDate.parse(expiryDate);
            E_Item_Place place = placeChoice == 1 ? E_Item_Place.Store : E_Item_Place.Warehouse;
//        Item newItem = new Item(name, costPrice, sellingPrice, manufacturer, category, subCategory, size, expiry, E_Item_Status.Available, place);
            addItem(name, costPrice, sellingPrice, manufacturer, category, subCategory, size, expiry, E_Item_Status.Available, place);
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }

    public static void addItem(String name, double costPrice, double sellingPrice, String manufacturer, String category, String subCategory, double size, LocalDate expiry, E_Item_Status Status, E_Item_Place place) {
     inventory.addItem(name, costPrice, sellingPrice, manufacturer, category, subCategory, size, expiry, Status, place);
    }


    private static void removeItem() {
        try {
            // Get input from the user for category, sub-category, size, and place
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the Category: ");
            String category = scanner.nextLine();
            System.out.print("Enter the Sub-Category: ");
            String subCategory = scanner.nextLine();
            System.out.print("Enter the size: ");
            double size = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter item place (1: Store, 2: Warehouse): ");
            int placeChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline after nextInt
            // Convert the user's choice into the appropriate enum value for place
            E_Item_Place place = placeChoice == 1 ? E_Item_Place.Store : E_Item_Place.Warehouse;
//        Item item_to_remove = managerController.findItem(name, category, subCategory, size, place);
            if (findItem(name, category, subCategory, size, place) == null) {
                System.out.println("the item dont exists");
                return;
            }
            removeItem(findItem(name, category, subCategory, size, place));
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }
    public static void removeItem(Item item){
        inventory.removeItem(item);
    }


    private static void moveItem() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the Category: ");
            String category = scanner.nextLine();
            System.out.print("Enter the Sub-Category: ");
            String subCategory = scanner.nextLine();
            System.out.print("Enter the size: ");
            double size = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter where you want to move the item (1: Warehouse ,2: Store): ");
            int placeChoice = scanner.nextInt();
            E_Item_Place place_item, place_to_move;
            if (placeChoice == 1) {
                place_item = E_Item_Place.Store;
                place_to_move = E_Item_Place.Warehouse;
            } else {
                place_item = E_Item_Place.Warehouse;
                place_to_move = E_Item_Place.Store;
            }
//        Item item_to_move = managerController.findItem(name, category, subCategory, size, place_item);
            if (findItem(name, category, subCategory, size, place_item) == null) {
                System.out.println("the item dont exists");
                return;
            }
            moveItem(findItem(name, category, subCategory, size, place_item), place_to_move);
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }
    }

    public static void moveItem(Item item, E_Item_Place new_palace){
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

    private static void viewProductDetails() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the Category: ");
            String category = scanner.nextLine();
            System.out.print("Enter the Sub-Category: ");
            String subCategory = scanner.nextLine();
            System.out.print("Enter the size: ");
            double size = Double.parseDouble(scanner.nextLine());
//        Product product = managerController.findOrProduct(name, category, subCategory, size);
            if (inventory.findOrProduct(name, category, subCategory, size) == null) {
                System.out.println("the product dont exists");
                return;
            }
            System.out.println(inventory.findOrProduct(name, category, subCategory, size));
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Please try again.");
        }

    }

    public static Item findItem(String name, String category, String subCategory, double size, E_Item_Place place) {
        return inventory.findItem(name, category, subCategory , size, place);
    }

    public boolean registerWorker(String name1, String password)
    {
        if (password.equals("1111"))
            return true;
        else
            return false;
//            return inventory.registerWorker(name1, password);
    }

//    public Product find_product_string(String name){
//        return inventory.getProductByName(name);
//    }

}
