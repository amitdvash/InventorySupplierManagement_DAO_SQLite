package dev.Suppliers.Presentation;

import dev.ControllerInventorySupplier.Main;
import dev.Suppliers.Domain.ControllersManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class UI {

    private final ControllersManager controllersManager;
    private final HashMap<String, String> credentials;

    public UI(ControllersManager controllersManager) {
        this.controllersManager = controllersManager;
        credentials = new HashMap<>();
        credentials.put("senior", "1111");
        credentials.put("junior", "2222");
    }

    // Method to display the startup options
//    public void displayStartupOptions() {
//        Scanner scanner = new Scanner(System.in);
//
//        // Ask the user to choose how to start the system
//        int startOption = getValidatedStartChoice(scanner);
//        if (startOption == 1) {
//            controllersManager.uploadBasicInformation();
//            System.out.println("Basic information uploaded, System is ready.");
//        } else if (startOption == 2) {
//            controllersManager.uploadOnlyOrders();
//            System.out.println("Only orders uploaded, System is ready.");
//        } else if (startOption == 3) {
//            System.out.println("System is ready with no information.");
//        }
//    }

    // Method to handle the login and display the correct menu based on access level
    public void displayLoginAndMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Login process
        System.out.println("\n--- Login Required ---");
        String username = getValidatedUsername(scanner);
        String password = getValidatedPassword(scanner, username);

        // After successful login, display the appropriate menu
        if (username.equals("senior")) {
            displayFullMenu(scanner);  // Senior manager gets the full menu
        } else if (username.equals("junior")) {
            displayLimitedMenu(scanner);  // Junior manager gets the limited menu
        }
    }

    // Method to get and validate the user's start choice
//    private int getValidatedStartChoice(Scanner scanner) {
//        int startChoice = -1;
//        while (startChoice != 1 && startChoice != 2 && startChoice != 3) {
//            System.out.println("How would you like to start the system?");
//            System.out.println("1. Start with basic information");
//            System.out.println("2. Start with orders only");
//            System.out.println("3. Start with no information at all");
//            System.out.print("Please enter 1, 2 or 3: ");
//            String input = scanner.nextLine().trim();
//
//            try {
//                startChoice = Integer.parseInt(input);
//                if (startChoice != 1 && startChoice != 2 && startChoice != 3) {
//                    System.out.println("Invalid input. Please enter 1, 2 or 3.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a valid number (1, 2 or 3).");
//            }
//        }
//        return startChoice;
//    }

    // Method to get and validate the username
    private String getValidatedUsername(Scanner scanner) {
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine().trim();
            if (credentials.containsKey(username)) {
                return username;
            } else {
                System.out.println("Username does not exist. Please try again.");
            }
        }
    }

    // Method to get and validate the password
    private String getValidatedPassword(Scanner scanner, String username) {
        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine().trim();
            if (credentials.get(username).equals(password)) {
                return password;
            } else {
                System.out.println("Incorrect password. Please try again.");
            }
        }
    }

    // Method to display the full menu for senior managers
    private void displayFullMenu(Scanner scanner) throws SQLException {
        int choice;
        do {
            System.out.println("\n--- Supplier Management System ---");
            System.out.println("1. Open New Supplier Card");
            System.out.println("2. Delete Supplier Card");
            System.out.println("3. Add Product to Supplier");
            System.out.println("4. Remove Product from Supplier");
            System.out.println("5. Update Supplier Details");
            System.out.println("6. Update Products Discount Details");
            System.out.println("7. Print All Suppliers Cards");
            System.out.println("8. Print All Orders Reports");
            System.out.println("9. Open New Order");
            System.out.println("10. Update Constant Order");
            System.out.println("11. Print All Active Orders");
            System.out.println("0. Exit");

            choice = getValidatedMenuChoice(scanner);

            switch (choice) {
                case 1:
                    controllersManager.openNewSupplierCard();
                    break;
                case 2:
                    controllersManager.deleteSupplierCard();
                    break;
                case 3:
                    controllersManager.addProductToSupplier();
                    break;
                case 4:
                    controllersManager.deleteProductFromSupplier();
                    break;
                case 5:
                    controllersManager.updateSupplierFields();
                    break;
                case 6:
                    controllersManager.updateDiscountDetails();
                    break;
                case 7:
                    controllersManager.printAllSuppliers();
                    break;
                case 8:
                    controllersManager.printAllOrders();
                    break;
                case 9:
                    controllersManager.openNewOrder();
                    break;
                case 10:
                    controllersManager.updateConstantOrder();
                    break;
                case 11:
                    controllersManager.printActiveOrders();
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    Main.main(new String[]{});  // Call Main class to return to main menu
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }

    // Method to display the limited menu for junior managers
    private void displayLimitedMenu(Scanner scanner) throws SQLException {
        int choice;
        do {
            System.out.println("\n--- Supplier Management System (Junior Manager) ---");
            System.out.println("1. Open New Supplier Card");
            System.out.println("3. Add Product to Supplier");
            System.out.println("6. Update Products Discount Details");
            System.out.println("7. Print All Suppliers Cards");
            System.out.println("8. Print All Orders Reports");
            System.out.println("9. Open New Order");
            System.out.println("10. Update Constant Order");
            System.out.println("11. Print All Active Orders");
            System.out.println("0. Exit");

            choice = getValidatedMenuChoice(scanner);

            switch (choice) {
                case 1:
                    controllersManager.openNewSupplierCard();
                    break;
                case 3:
                    controllersManager.addProductToSupplier();
                    break;
                case 6:
                    controllersManager.updateDiscountDetails();
                    break;
                case 7:
                    controllersManager.printAllSuppliers();
                    break;
                case 8:
                    controllersManager.printAllOrders();
                    break;
                case 9:
                    controllersManager.openNewOrder();
                    break;
                case 10:
                    controllersManager.updateConstantOrder();
                    break;
                case 11:
                    controllersManager.printActiveOrders();
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    Main.main(new String[]{});  // Call Main class to return to main menu
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }

    // Method to get and validate the user's menu choice
    private int getValidatedMenuChoice(Scanner scanner) {
        int choice = -1;
        while (true) {
            System.out.print("Please enter your choice: ");
            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
