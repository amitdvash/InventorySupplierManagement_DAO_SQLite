package dev;

import dev.Suppliers.Domain.*;
import dev.Suppliers.Enums.PaymentMethod;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main
{
    public static void main(String[] args)
    {

        SupplierController supplierController = new SupplierController();
        ProductController productController = new ProductController();
        AgreementController agreementController = new AgreementController();

        // 2. Create ControllersManager
        ControllersManager controllersManager = new ControllersManager(supplierController, productController, agreementController);

        // 3. Start testing OpenNewSupplierCard method
        System.out.println("Starting test for OpenNewSupplierCard...");

        // Call the method
        controllersManager.OpenNewSupplierCard();

        // Print details of all suppliers created
        System.out.println("\nAll suppliers created:");
        for (Supplier supplier : supplierController.getSuppliers()) {
            supplier.printSupplierDetails();
        }
    }
}

