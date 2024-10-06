
package dev.Inventory.Controllers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import dev.ControllerInventorySupplier.Main;
import dev.Inventory.Classes.Inventory;

//---------------------------------------------------------------------
//To Take the following classes from the Inventory package
import dev.Inventory.Data.SystemInitializer;
import dev.Inventory.DB.CreateTable;
import dev.Inventory.DB.SQLiteDB;
import dev.Suppliers.Domain.ControllersManager;
//---------------------------------------------------------------------


public class Controller_Menu
{
    private static Controller_Manager managerController = new Controller_Manager();
    private static Controller_Worker workerController = new Controller_Worker();
    protected static Scanner scanner = new Scanner(System.in);
    protected static Connection sql_Connection;


    protected static Inventory inventory;



    public void setInventory(Inventory inventory) {
        Controller_Menu.inventory = inventory;
    }
    public Inventory getInventory() {
        return inventory;
    }



    public static Connection getSqlConnection(){
        return Controller_Menu.sql_Connection;
    }

    public static void runProgram() throws SQLException {
//        initializeInventorySystem();
        Controller_Menu.dataMenu();
        Controller_Menu.registerMenu();
    }




    static void dataMenu() throws SQLException {
        System.out.println("===== Data Menu =====");
        System.out.println("1. Initialize with Predefined Data");
        System.out.println("2. Start with Empty System");
        System.out.println("3. Exit");
        System.out.println("=========================");
        System.out.print("Select an option: ");

        String choice = scanner.next();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case "1":
                System.out.println("Initializing system with predefined data...");
                SystemInitializer.initializeSystem(inventory);  // Call to the initializer with predefined data
                break;
            case "2":
                System.out.println("Starting with an empty system...");
                // No data initialization, system will be empty
                break;
            case "4":
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again.");
                dataMenu();  // Recursive call to show the menu again if invalid option
        }
    }



    static void registerMenu() throws SQLException {
        while(true)
        {
            System.out.println("===== Register Menu =====");
            System.out.println("1. Register as Manager");
            System.out.println("2. Register as Worker");
            System.out.println("3. Exit");
            System.out.println("=========================");
            System.out.print("Select an option: ");
            String choice = scanner.next();
            scanner.nextLine();  // Consume newline
            switch (choice) {
                case "1":
                    System.out.print("Enter your name: ");
                    String name1 = scanner.nextLine();
                    System.out.print("Enter your password:(1111) ");
                    String password = scanner.nextLine();
                    if (managerController.registerManager(name1 , password)) {
                        managerController.ManagerMenu();
                    }
                    else
                    {
                        System.out.println("Registration failed. Please try again.");
                    }
                    break;
                case "2":
                    System.out.print("Enter your name: ");
                    String name2 = scanner.nextLine();
                    System.out.print("Enter your password:(2222) ");
                    String password2 = scanner.nextLine();
                    if (workerController.registerWorker(name2,password2)) {
                        workerController.WorkerMenu();
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                    break;
                case "3":
                    System.out.println("Exiting...");
                    Main.main(new String[]{});  // Call Main class to return to main menu
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to initialize the connections and objects
    public static void initializeInventorySystem() {
        try {
            // Establish the SQLite connection
            sql_Connection = SQLiteDB.connect();
            System.out.println("Database connection established successfully.");

            // Initialize the Inventory object
            inventory = new Inventory(sql_Connection);
            System.out.println("Inventory initialized successfully.");

            CreateTable.initializeTables(sql_Connection);

            System.out.println("initializeTables was tables successfully");

        } catch (SQLException e) {
            // If there's an error during initialization, throw a runtime exception
            throw new RuntimeException("Failed to initialize inventory system", e);
        }
    }








}
