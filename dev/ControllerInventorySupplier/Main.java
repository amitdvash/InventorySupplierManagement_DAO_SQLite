package dev.ControllerInventorySupplier;

import dev.Inventory.Controllers.main_Controller;
import dev.Suppliers.DataBase.DatabaseConnection;
import dev.Suppliers.DataBase.SupllierCreatDb;
import dev.Suppliers.DataBase.main_supplier;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {  //
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome! Would you like to manage Inventory or Supplier?");
        System.out.println("Type '1' for Inventory or '2' for Supplier:");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("You have selected Inventory.");
                main_Controller.main();  //
                break;
            case 2:
                System.out.println("You have selected Supplier.");
                DatabaseConnection.connect();
                SupllierCreatDb.createTables();
                DatabaseConnection.main();
                main_supplier.main();  //
                break;
            default:
                System.out.println("Invalid choice! Please run the program again.");
        }

        scanner.close();
    }
}
