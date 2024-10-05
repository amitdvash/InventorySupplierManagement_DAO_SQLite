package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.*;
import dev.Suppliers.Presentation.UI;
//import dev.Suppliers.Presentation.UI;

import java.sql.Connection;
import java.sql.SQLException;

public class main_supplier {
    public static void main() throws SQLException {


        Connection connection = DatabaseConnection.connect();
        // Initialize the controllers

        SupplierController supplierController = new SupplierController(connection);
        ProductController productController = new ProductController(connection);
        AgreementController agreementController = new AgreementController(connection);
        OrderController orderController = new OrderController(connection);


        // Initialize the ControllersManager
        ControllersManager controllersManager = new ControllersManager(supplierController, productController, agreementController, orderController);
        //controllersManager.openNewSupplierCard();
        //controllersManager.deleteSupplierCard();
        //controllersManager.addProductToSupplier();
        //controllersManager.deleteProductFromSupplier();
        //controllersManager.updateSupplierFields();
        //controllersManager.updateDiscountDetails();
        //controllersManager.openNewOrder();
        //controllersManager.updateConstantOrder();
        //controllersManager.printActiveOrders();
        // Initialize the UI
        UI ui = new UI(controllersManager);

        // First, ask the user how to start the system (basic info, orders, or no info)
        //ui.displayStartupOptions();

        // Then, prompt the user for login and show the corresponding menu based on access level
        ui.displayLoginAndMenu();
    }
}