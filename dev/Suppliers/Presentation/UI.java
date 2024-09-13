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
            System.out.println("4. Delete Product from Supplier");
            System.out.println("5. Print All Suppliers");
            System.out.println("6. Update Supplier Field");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Clear the buffer after reading the int

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
                    controllersManager.printAllSuppliers();
                    break;
                case 6:
                    controllersManager.updateSupplierFields();
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }
}