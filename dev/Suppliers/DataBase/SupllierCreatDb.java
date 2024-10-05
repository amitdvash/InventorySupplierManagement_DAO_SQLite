package dev.Suppliers.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static dev.Suppliers.DataBase.DatabaseConnection.connect;

public class SupllierCreatDb {




    public static void createTables() {
        String suppliersTable = "CREATE TABLE IF NOT EXISTS Suppliers (" +
                "supplierID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "phone VARCHAR(50) NOT NULL," +
                "email VARCHAR(255)," +
                "companyID VARCHAR(50)," +
                "bankAccount VARCHAR(50)," +
                "paymentMethod VARCHAR(50)" +
                ");";
        String agreementsTable = "CREATE TABLE IF NOT EXISTS Agreements (" +
                "agreementID INTEGER PRIMARY KEY AUTOINCREMENT," + // Changed SERIAL to INTEGER with AUTOINCREMENT for SQLite
                "supplierID INTEGER NOT NULL," +
//                "productList TEXT," +  // Added productList column
//                "discountDetails TEXT," +  // Added discountDetails column// need to dal
//                "supplyDays TEXT," +  // Added supplyDays column
                "selfSupply INTEGER CHECK (selfSupply IN (0, 1))," +
                "FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID) ON DELETE CASCADE" +
                ");";

        String productsTable = "CREATE TABLE IF NOT EXISTS ProductsSuppliers (" +
                "catalogID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "price NUMERIC(10, 2)," +
                "expirationDays INTEGER," +
                "weight NUMERIC(10, 2)," +
                "agreementID INTEGER REFERENCES Agreements(agreementID) ON DELETE CASCADE" +
                ");";

        String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                "orderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "supplierID INTEGER," +
                "orderDate DATE NOT NULL," +
                "deliveryDay VARCHAR(20) NOT NULL," +
                "isConstantDelivery BOOLEAN DEFAULT FALSE," +
                "deliveryDate DATE," +
                "priceBeforeDiscount NUMERIC(10, 2)," +
                "discountAmount NUMERIC(10, 2)," +
                "priceAfterDiscount NUMERIC(10, 2)" +
                ");";

        String orderProductsTable = "CREATE TABLE IF NOT EXISTS orderProducts (" +
                "orderID INTEGER NOT NULL," +
                "catalogID INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price NUMERIC(10, 2) NOT NULL," +
                "discount NUMERIC(10, 2) DEFAULT 0," +
                "PRIMARY KEY (orderID, catalogID)," +
                "FOREIGN KEY (orderID) REFERENCES Orders(orderID)," +
                "FOREIGN KEY (catalogID) REFERENCES ProductsSuppliers(catalogID)" +
                ");";

        String productDiscountsTable = "CREATE TABLE IF NOT EXISTS productDiscounts (" +
                "catalogID INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "discount NUMERIC(10, 2) NOT NULL," +
                "PRIMARY KEY (catalogID, quantity)," +
                "FOREIGN KEY (catalogID) REFERENCES ProductsSuppliers(catalogID) ON DELETE CASCADE" +
                ");";

        String supplyDaysTable = "CREATE TABLE IF NOT EXISTS SupplyDays (" +
                "dayName VARCHAR(20) PRIMARY KEY" +
                ");";

        String agreementSupplyDaysTable = "CREATE TABLE IF NOT EXISTS AgreementSupplyDays (" +
                "agreementID INTEGER NOT NULL," +
                "dayName VARCHAR(20) NOT NULL," +
                "PRIMARY KEY (agreementID, dayName)," +
                "FOREIGN KEY (agreementID) REFERENCES Agreements(agreementID) ON DELETE CASCADE," +
                "FOREIGN KEY (dayName) REFERENCES SupplyDays(dayName) ON DELETE CASCADE" +
                ");";


        String ordersOnTheWayTable = "CREATE TABLE IF NOT EXISTS OrdersOnTheWay (" +
                "orderID INTEGER NOT NULL," +
                "catalogID INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "deliveryDate DATE," +
                "PRIMARY KEY (orderID, catalogID)," +
                "FOREIGN KEY (orderID) REFERENCES Orders(orderID)" +
                ");";


        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Create each table
            stmt.execute(suppliersTable);
            stmt.execute(agreementsTable);
            stmt.execute(productsTable);
            stmt.execute(ordersTable);
            stmt.execute(orderProductsTable);
            stmt.execute(productDiscountsTable);
            stmt.execute(supplyDaysTable);
            stmt.execute(agreementSupplyDaysTable);
            stmt.execute(ordersOnTheWayTable);


            System.out.println("Tables created successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
