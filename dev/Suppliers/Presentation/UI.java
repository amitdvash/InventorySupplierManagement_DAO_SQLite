package dev.Suppliers.Presentation;

import dev.Suppliers.Domain.ControllersManager;

import java.util.Scanner;

public class UI {

    private final ControllersManager controllersManager;

    public UI(ControllersManager controllersManager) {
        this.controllersManager = controllersManager;
    }

    // Method to display the menu and interact with the user
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println("0. Exit");

            // Get a valid integer choice from the user
            choice = getValidatedMenuChoice(scanner);

            // Perform the selected action
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
                case 0:
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }

    // Method to get and validate the user's menu choice (integer input)
    private int getValidatedMenuChoice(Scanner scanner) {
        int choice = -1;
        while (true) {
            System.out.print("Please enter your choice: ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
                return choice;  // Return valid integer choice
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
