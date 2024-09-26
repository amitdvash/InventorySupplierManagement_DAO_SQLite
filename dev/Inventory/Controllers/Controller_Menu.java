package dev.Inventory.Controllers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import dev.Inventory.Classes.Inventory;

//---------------------------------------------------------------------
//To Take the following classes from the Inventory package
import dev.Inventory.Data.SystemInitializer;
import dev.Inventory.SqlLite.CreateTable;
import dev.Inventory.SqlLite.Item_SQL;
import dev.Inventory.SqlLite.ProductSQL;
import dev.Inventory.SqlLite.SQLiteDB;
//---------------------------------------------------------------------


public class Controller_Menu
{
    private static Controller_Manager managerController = new Controller_Manager();
    private static Controller_Worker workerController = new Controller_Worker();
    protected static Scanner scanner = new Scanner(System.in);

    protected static Connection sql_Connection;
    protected static Item_SQL item_sql;
    protected static ProductSQL productSQL;

    protected static Inventory inventory = new Inventory();

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
        initializeSQLConnection();
        Controller_Menu.dataMenu();
        Controller_Menu.registerMenu();
    }

    private static void initializeSQLConnection() throws SQLException {
        sql_Connection = SQLiteDB.connect();
        CreateTable.initializeTables();
        item_sql = new Item_SQL(sql_Connection);
        productSQL = new ProductSQL(sql_Connection);
    }


    static void dataMenu() {
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



    static void registerMenu()
    {
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
                    System.out.print("Enter your password:(1111) ");
                    String password2 = scanner.nextLine();
                    if (workerController.registerWorker(name2,password2)) {
                        workerController.WorkerMenu();
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                    break;
                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }










}