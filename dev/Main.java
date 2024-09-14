package dev;

import dev.Suppliers.Domain.*;
import dev.Suppliers.Presentation.UI;

public class Main
{
    public static void main(String[] args)
    {

        SupplierController supplierController = new SupplierController();
        ProductController productController = new ProductController();
        AgreementController agreementController = new AgreementController();
        OrderController orderController = new OrderController();

        // Initialize the ControllersManager
        ControllersManager controllersManager = new ControllersManager(supplierController, productController, agreementController,orderController);

        // Initialize the UI
        UI ui = new UI(controllersManager);

        // Start the UI and display the menu
        ui.displayMenu();


    }
}

