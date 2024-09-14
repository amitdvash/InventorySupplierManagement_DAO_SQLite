package dev;

import dev.Suppliers.Domain.*;
import dev.Suppliers.Presentation.UI;

public class Main {
    public static void main(String[] args) {

        // Initialize the controllers
        SupplierController supplierController = new SupplierController();
        ProductController productController = new ProductController();
        AgreementController agreementController = new AgreementController();
        OrderController orderController = new OrderController();

        // Initialize the ControllersManager
        ControllersManager controllersManager = new ControllersManager(supplierController, productController, agreementController, orderController);

        // Initialize the UI
        UI ui = new UI(controllersManager);

        // First, ask the user how to start the system (basic info, orders, or no info)
        ui.displayStartupOptions();

        // Then, prompt the user for login and show the corresponding menu based on access level
        ui.displayLoginAndMenu();
    }
}
