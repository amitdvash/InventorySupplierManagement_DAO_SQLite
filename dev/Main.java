package dev;

import dev.Suppliers.Domain.*;
import dev.Suppliers.Presentation.UI;

import java.util.List;


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
















//        SupplierController supplierController = new SupplierController();
//        ProductController productController = new ProductController();
//        AgreementController agreementController = new AgreementController();
//        OrderController orderController = new OrderController();
//        // 2. Create ControllersManager
//        ControllersManager controllersManager = new ControllersManager(supplierController, productController, agreementController);
//
////         3. Start testing OpenNewSupplierCard method
//        System.out.println("Starting test for OpenNewSupplierCard...");
//
//        // Call the method
//
//        controllersManager.openNewSupplierCard();
//        controllersManager.openNewSupplierCard();
//        //controllersManager.OpenNewSupplierCard();
////        controllersManager.addProductToSupplier();
////        controllersManager.addProductToSupplier();
//        controllersManager.deleteProductFromSupplier();
//        controllersManager.printAllSuppliers();
//        controllersManager.deleteSupplierCard();

        // Print details of all suppliers created
//        System.out.println("\nAll suppliers created:");
//        for (Supplier supplier : supplierController.getSuppliers()) {
//            supplier.printSupplierDetails();
//        }
        // Create example suppliers with agreements and products
//        System.out.println("Creating example suppliers with agreements and products...");
//
//        // Example 1: Supplier 1 with Agreement and Products
//        Supplier supplier1 = supplierController.createSupplier("S1", "Bank1", PaymentMethod.CreditCard, null, "Supplier One", "123456", "email1@example.com");
//
//        // Creating discount for Product 1
//        HashMap<Integer, Double> product1Discounts = new HashMap<>();
//        product1Discounts.put(10, 5.0);  // 5% discount for 10 or more units
//        product1Discounts.put(20, 10.0); // 10% discount for 20 or more units
//
//        // Creating discount for Product 2
//        HashMap<Integer, Double> product2Discounts = new HashMap<>();
//        product2Discounts.put(5, 3.0);   // 3% discount for 5 or more units
//        product2Discounts.put(15, 7.0);  // 7% discount for 15 or more units
//
//        Product product1 = productController.createProduct("Product 1", product1Discounts, 10.0, 30, 2.0, null);
//        Product product2 = productController.createProduct("Product 2", product2Discounts, 20.0, 50, 3.5, null);
//
//        Agreement agreement1 = agreementController.createAgreement(List.of(product1, product2), new HashMap<>(), List.of("Monday", "Wednesday"), true);
//        supplier1.setSupplierAgreement(agreement1);  // Link agreement to supplier
//        orderController.addSupplier(supplier1);  // Add supplier to OrderController
//
//        // Example 2: Supplier 2 with Agreement and Products
//        Supplier supplier2 = supplierController.createSupplier("S2", "Bank2", PaymentMethod.Cash, null, "Supplier Two", "654321", "email2@example.com");
//
//        // Creating discount for Product 3
//        HashMap<Integer, Double> product3Discounts = new HashMap<>();
//        product3Discounts.put(8, 4.0);   // 4% discount for 8 or more units
//        product3Discounts.put(12, 8.0);  // 8% discount for 12 or more units
//
//        // Creating discount for Product 4
//        HashMap<Integer, Double> product4Discounts = new HashMap<>();
//        product4Discounts.put(10, 6.0);  // 6% discount for 10 or more units
//        product4Discounts.put(20, 12.0); // 12% discount for 20 or more units
//
//        Product product3 = productController.createProduct("Product 3", product3Discounts, 15.0, 40, 5.0, null);
//        Product product4 = productController.createProduct("Product 4", product4Discounts, 25.0, 60, 1.5, null);
//
//        Agreement agreement2 = agreementController.createAgreement(List.of(product3, product4), new HashMap<>(), List.of("Tuesday", "Thursday"), false);
//        supplier2.setSupplierAgreement(agreement2);  // Link agreement to supplier
//        orderController.addSupplier(supplier2);  // Add supplier to OrderController
//
//        // Example Orders
//        System.out.println("Creating example orders...");
//
//        // Creating orders for supplier 1
//        List<Product> order1Products = List.of(product1, product2);
//        Order order1 = new Order(supplier1.getSupplierID(), new java.util.Date(), order1Products);
//        orderController.addOrder(order1);
//
//        // Creating orders for supplier 2
//        List<Product> order2Products = List.of(product3, product4);
//        Order order2 = new Order(supplier2.getSupplierID(), new java.util.Date(), order2Products);
//        orderController.addOrder(order2);
//
//        // Generate the orders report
//        System.out.println("Generating orders report...");
//        orderController.generateOrdersReport();
//        controllersManager.printAllSuppliers();
    }
}

