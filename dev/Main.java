package dev;

import dev.Suppliers.Classes.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main
{
    public static void main(String[] args)
    {

        // 1. Create the Controllers
        SupplierController supplierController = new SupplierController();
        ProductController productController = new ProductController();
        AgreementController agreementController = new AgreementController();
        OrderController orderController = new OrderController();

        // 2. Add Suppliers with Contact Information
        SupplierContact contact1 = new SupplierContact("John Doe", "john@example.com", "123456789");
        Supplier supplier1 = new Supplier("S001", "C001", "123-456", "Bank Transfer", new ArrayList<>(), null, contact1);
        supplierController.addSupplier(supplier1);

        SupplierContact contact2 = new SupplierContact("Jane Doe", "jane@example.com", "987654321");
        Supplier supplier2 = new Supplier("S002", "C002", "789-101", "Credit Card", new ArrayList<>(), null, contact2);
        supplierController.addSupplier(supplier2);

        // 3. Add Products for Suppliers
        HashMap<Integer, Double> discountProduct1 = new HashMap<>();
        discountProduct1.put(10, 5.0);  // 5% discount for 10 units

        Product product1 = new Product("P001", "CAT001", "Laptop", discountProduct1, 1000.0, 365, 2.5, null);
        productController.addProduct(product1);

        HashMap<Integer, Double> discountProduct2 = new HashMap<>();
        discountProduct2.put(20, 10.0);  // 10% discount for 20 units
        discountProduct2.put(30, 15.0);  // 10% discount for 20 units
        Product product2 = new Product("P002", "CAT002", "Smartphone", discountProduct2, 500.0, 365, 1.0, null);
        productController.addProduct(product2);

        // 4. Create Agreements for Suppliers
        List<Product> supplier1Products = new ArrayList<>();
        supplier1Products.add(product1);
        Agreement agreement1 = new Agreement(1, supplier1Products, new Date(), new HashMap<>(), "Monday", true, "Credit");
        agreement1.addDiscountDetails("CAT001", discountProduct1);
        agreement1.setPaymentType("Credit Card");
        supplierController.addAgreementToSupplier("S001", agreement1);

        List<Product> supplier2Products = new ArrayList<>();
        supplier2Products.add(product2);
        Agreement agreement2 = new Agreement(2, supplier2Products, new Date(), new HashMap<>(), "Friday", false, "Cash");
        agreement2.addDiscountDetails("CAT002", discountProduct2);
        agreement2.setPaymentType("Bank Transfer");
        supplierController.addAgreementToSupplier("S002", agreement2);

        // 5. Create Orders and Add Them to OrderController
        List<Product> order1Products = new ArrayList<>();
        order1Products.add(product1);

        Order order1 = new Order("O001", "S001", new Date(), order1Products);
        orderController.addOrder(order1);

        List<Product> order2Products = new ArrayList<>();
        order2Products.add(product2);

        Order order2 = new Order("O002", "S002", new Date(), order2Products);
        orderController.addOrder(order2);

        // 6. Generate Order Report
        System.out.println("\nGenerating Orders Report...");
        orderController.generateOrdersReport();
    }
}

