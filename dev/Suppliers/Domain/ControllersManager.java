package dev.Suppliers.Domain;

import dev.Suppliers.Domain.Exception.ExitException;
import dev.Suppliers.Enums.PaymentMethod;

import java.util.*;

public class ControllersManager {

    private final SupplierController supplierController;
    private final ProductController productController;
    private final AgreementController agreementController;
    private final OrderController orderController;
    private final InputValidator inputValidator = new InputValidator();  // Use InputValidator for input validation

    public ControllersManager(SupplierController supplierController, ProductController productController, AgreementController agreementController, OrderController orderController) {
        this.supplierController = supplierController;
        this.productController = productController;
        this.agreementController = agreementController;
        this.orderController = orderController;
    }

    // 1. Open New Supplier Card
    public void openNewSupplierCard() {
        try {
            String name = inputValidator.getValidatedInput("Enter Supplier's Name: ");
            String phoneNumber = inputValidator.getValidatedInput("Enter Supplier's phone number: ", inputValidator::isValidPhoneNumber, "Phone number must contain only digits.");
            String email = inputValidator.getValidatedInput("Enter Supplier's email: ", inputValidator::isValidEmail, "Invalid email format.");
            String companyID = inputValidator.getValidatedInput("Enter Supplier's company ID: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Company ID format.");
            String bankAccount = inputValidator.getValidatedInput("Enter Supplier's bank account: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Bank account format.");
            PaymentMethod paymentMethod = getValidatedPaymentMethod();

            // Create Products for the Agreement
            List<Product> productList = new ArrayList<>();
            HashMap<String, HashMap<Integer, Double>> agreementDiscounts = new HashMap<>();
            boolean addMoreProducts;
            do {
                String productName = inputValidator.getValidatedInput("Enter Product's name: ");
                double price = inputValidator.getValidatedDouble("Enter Product's price: ");
                int expirationDays = inputValidator.getValidatedInt("Enter Product's expiration days: ");
                double weight = inputValidator.getValidatedDouble("Enter Product's weight: ");

                // Collect discount details for the product
                HashMap<Integer, Double> discountDetails = new HashMap<>();
                if (inputValidator.getValidatedYesNoInput("Are there any discount details? (yes/no)").equalsIgnoreCase("yes")) {
                    boolean addMoreDiscounts;
                    do {
                        int quantity = inputValidator.getValidatedInt("Enter minimum quantity for discount: ");
                        double discountPercent = inputValidator.getValidatedDiscountPercentage("Enter discount percent (between 1 and 100): ");
                        discountDetails.put(quantity, discountPercent);

                        addMoreDiscounts = inputValidator.getValidatedYesNoInput("Add another discount? (yes/no): ").equalsIgnoreCase("yes");
                    } while (addMoreDiscounts);
                }

                Product product = productController.createProduct(productName, discountDetails, price, expirationDays, weight, null);
                productList.add(product);
                agreementDiscounts.put(product.getCatalogID(), discountDetails);

                addMoreProducts = inputValidator.getValidatedYesNoInput("Add another product? (yes/no): ").equalsIgnoreCase("yes");
            } while (addMoreProducts);

            // Set up the agreement
            List<String> supplyDays = selectSupplyDays();
            boolean selfSupply = Boolean.parseBoolean(inputValidator.getValidatedYesNoInput("Is the supplier responsible for supplying products? (yes/no): "));

            Agreement agreement = agreementController.createAgreement(productList, agreementDiscounts, supplyDays, selfSupply);
            Supplier supplier = supplierController.createSupplier(companyID, bankAccount, paymentMethod, agreement, name, phoneNumber, email);

            // Link products and agreement
            agreement.setSupplier(supplier);
            productList.forEach(product -> product.setAgreement(agreement));

            System.out.println("Supplier successfully created.");
            supplier.printSupplierDetails();
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    // 2. Delete Supplier Card
    public void deleteSupplierCard() {
        try {
            if (supplierController.getSuppliers().isEmpty()) {
                System.out.println("No suppliers available.");
                return;
            }

            // Display all available suppliers
            System.out.println("Available suppliers:");
            supplierController.getSuppliers().forEach(supplier ->
                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));

            // Step 1: Get valid supplier ID
            Supplier supplier = null;
            while (supplier == null) {
                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
                supplier = supplierController.getSupplierById(supplierID);

                if (supplier == null) {
                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
                }
            }

            // Step 2: Confirm deletion
            String confirmDeletion = inputValidator.getValidatedYesNoInput("Are you sure you want to delete this supplier? (yes/no)");
            if (!confirmDeletion.equalsIgnoreCase("yes")) {
                System.out.println("Supplier deletion cancelled.");
                return;
            }

            // Step 3: Delete the supplier's agreement if it exists
            Agreement agreement = supplier.getSupplierAgreement();
            if (agreement != null) {
                agreementController.deleteAgreement(agreement.getAgreementID());
            }

            // Step 4: Delete supplier
            supplierController.deleteSupplier(supplier.getSupplierID());
            System.out.println("Supplier successfully deleted.");
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    public void addProductToSupplier() {
        try {
            if (supplierController.getSuppliers().isEmpty()) {
                System.out.println("No suppliers available.");
                return;
            }

            Supplier supplier = null;
            while (supplier == null) {
                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
                supplier = supplierController.getSupplierById(supplierID);

                if (supplier == null) {
                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
                }
            }

            if (supplier.getSupplierAgreement() == null) {
                System.out.println("Supplier does not have an agreement.");
                return;
            }

            // Collect product details
            String productName = inputValidator.getValidatedInput("Enter Product's name: ");
            double price = inputValidator.getValidatedDouble("Enter Product's price: ");
            int expirationDays = inputValidator.getValidatedInt("Enter Product's expiration days: ");
            double weight = inputValidator.getValidatedDouble("Enter Product's weight: ");

            HashMap<Integer, Double> discountDetails = new HashMap<>();
            if (inputValidator.getValidatedYesNoInput("Any discounts? (yes/no): ").equalsIgnoreCase("yes")) {
                boolean addMoreDiscounts;
                do {
                    int quantity = inputValidator.getValidatedInt("Enter minimum quantity for discount: ");
                    double discountPercent = inputValidator.getValidatedDiscountPercentage("Enter discount percent: ");
                    discountDetails.put(quantity, discountPercent);

                    addMoreDiscounts = inputValidator.getValidatedYesNoInput("Add another discount? (yes/no): ").equalsIgnoreCase("yes");
                } while (addMoreDiscounts);
            }

            // Create and add the product to the supplier
            Product product = productController.createProduct(productName, discountDetails, price, expirationDays, weight, null);
            supplier.getSupplierAgreement().addProduct(product);
            supplier.getSupplierAgreement().getDiscountDetails().put(product.getCatalogID(), discountDetails);

            System.out.println("Product added to supplier.");
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    public void deleteProductFromSupplier() {
        try {
            if (supplierController.getSuppliers().isEmpty()) {
                System.out.println("No suppliers available.");
                return;
            }

            // Display all available suppliers
            System.out.println("Available suppliers:");
            supplierController.getSuppliers().forEach(supplier ->
                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));

            // Step 1: Get valid supplier ID
            Supplier supplier = null;
            while (supplier == null) {
                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
                supplier = supplierController.getSupplierById(supplierID);

                if (supplier == null) {
                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
                }
            }

            if (supplier.getSupplierAgreement() == null) {
                System.out.println("Supplier does not have an agreement.");
                return;
            }

            // Step 2: Display all available products for the supplier
            Agreement agreement = supplier.getSupplierAgreement();
            List<Product> productList = agreement.getProductList();
            if (productList.isEmpty()) {
                System.out.println("No products available for this supplier.");
                return;
            }

            System.out.println("Available products for Supplier " + supplier.getContact().getName() + ":");
            for (Product product : productList) {
                System.out.println("Catalog ID: " + product.getCatalogID() + ", Product Name: " + product.getName());
            }

            // Step 3: Get a valid product catalog ID
            Product productToDelete = null;
            while (productToDelete == null) {
                String catalogID = inputValidator.getValidatedInput("Enter Product's catalog ID to delete: ");
                productToDelete = productList.stream()
                        .filter(product -> product.getCatalogID().equals(catalogID))
                        .findFirst().orElse(null);

                if (productToDelete == null) {
                    System.out.println("Product not found. Please provide a valid Product ID.");
                }
            }

            // Step 4: Remove the product from the supplier's agreement
            agreement.getProductList().remove(productToDelete);
            agreement.getDiscountDetails().remove(productToDelete.getCatalogID());

            System.out.println("Product successfully deleted.");
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    public void updateSupplierFields() {
        try {
            if (supplierController.getSuppliers().isEmpty()) {
                System.out.println("No suppliers available.");
                return;
            }

            // Display all available suppliers
            System.out.println("Available suppliers:");
            supplierController.getSuppliers().forEach(supplier ->
                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));

            // Step 1: Get valid supplier ID
            Supplier supplier = null;
            while (supplier == null) {
                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
                supplier = supplierController.getSupplierById(supplierID);

                if (supplier == null) {
                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
                }
            }

            // Step 2: Update supplier fields
            boolean keepUpdating = true;
            while (keepUpdating) {
                System.out.println("Which field would you like to update?");
                System.out.println("1. Company ID\n2. Bank Account\n3. Payment Method\n4. Contact Info\n5. Supplier Agreement\n6. Exit");
                int choice = inputValidator.getValidatedInt("Choose an option: ");

                switch (choice) {
                    case 1:
                        String newCompanyID = inputValidator.getValidatedInput("Enter new Company ID: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Company ID. Must contain only digits.");
                        supplier.setCompanyID(newCompanyID);
                        System.out.println("Company ID has been changed to: " + newCompanyID);
                        break;
                    case 2:
                        String newBankAccount = inputValidator.getValidatedInput("Enter new Bank Account: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Bank Account. Must contain only digits.");
                        supplier.setBankAccount(newBankAccount);
                        System.out.println("Bank Account has been changed to: " + newBankAccount);
                        break;
                    case 3:
                        PaymentMethod newPaymentMethod = getValidatedPaymentMethod();
                        supplier.setPaymentMethod(newPaymentMethod);
                        System.out.println("Payment Method has been changed to: " + newPaymentMethod);
                        break;
                    case 4:
                        String name = inputValidator.getValidatedInput("Enter new Name: ");
                        String phone = inputValidator.getValidatedInput("Enter new Phone: ", inputValidator::isValidPhoneNumber, "Invalid phone number. Must contain only digits.");
                        String email = inputValidator.getValidatedInput("Enter new Email: ", inputValidator::isValidEmail, "Invalid email format.");
                        supplier.setContact(new SupplierContact(name, phone, email));
                        System.out.println("Contact Info updated: Name: " + name + ", Phone: " + phone + ", Email: " + email);
                        break;
                    case 5:
                        updateSupplierAgreement(supplier.getSupplierAgreement());
                        break;
                    case 6:
                        keepUpdating = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }

            // Print the updated supplier information after the updates are done
            System.out.println("Supplier information after updates:");
            supplier.printSupplierDetails();
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    public void updateDiscountDetails() {
        try {
            if (supplierController.getSuppliers().isEmpty()) {
                System.out.println("No suppliers available.");
                return;
            }

            // Display all available suppliers
            System.out.println("Available suppliers:");
            supplierController.getSuppliers().forEach(supplier ->
                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));

            // Step 1: Get valid supplier ID
            Supplier supplier = null;
            while (supplier == null) {
                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'):", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
                supplier = supplierController.getSupplierById(supplierID);

                if (supplier == null) {
                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
                }
            }

            Agreement agreement = supplier.getSupplierAgreement();
            if (agreement == null || agreement.getProductList().isEmpty()) {
                System.out.println("No products available for this supplier.");
                return;
            }

            // Display all available products for the supplier
            System.out.println("Available products for Supplier " + supplier.getContact().getName() + ":");
            for (Product product : agreement.getProductList()) {
                System.out.println("Catalog ID: " + product.getCatalogID() + ", Product Name: " + product.getName());
            }

            // Step 2: Get valid Product ID
            Product product = null;
            while (product == null) {
                String productID = inputValidator.getValidatedInput("Enter Product ID: ");
                product = agreement.getProductList().stream()
                        .filter(p -> p.getCatalogID().equalsIgnoreCase(productID))
                        .findFirst().orElse(null);

                if (product == null) {
                    System.out.println("Product not found. Please provide a valid Product ID.");
                }
            }

            // Print existing discounts for the product before add/delete decision
            printProductDiscounts(product);

            // Step 3: Ask to add or delete discount
            String action = inputValidator.getValidatedInput("Do you want to add or delete a discount? (add/delete): ",
                    input -> input.equalsIgnoreCase("add") || input.equalsIgnoreCase("delete"),
                    "Please enter 'add' or 'delete'.");
            if (action.equalsIgnoreCase("exit")) return;  // Exit option

            if (action.equalsIgnoreCase("add")) {
                // Adding a discount
                int quantity = getValidatedQuantityInput();
                double discountPercent = inputValidator.getValidatedDiscountPercentage("Enter discount percent: ");
                agreementController.addDiscountToProduct(agreement, product.getCatalogID(), quantity, discountPercent);
                System.out.println("Discount added successfully.");
            } else if (action.equalsIgnoreCase("delete")) {
                if (product.getDiscountDetails().isEmpty()) {
                    System.out.println("No discounts available to delete.");
                    return;
                }
                // Show existing discounts before deletion
                printProductDiscounts(product);

                int quantity = getValidatedQuantityInput();
                if (product.getDiscountDetails().containsKey(quantity)) {
                    agreementController.deleteDiscountFromProduct(agreement, product.getCatalogID(), quantity);
                    System.out.println("Discount for quantity " + quantity + " deleted successfully.");
                } else {
                    System.out.println("No discount found for the entered quantity.");
                }
            }

            // Print updated discounts after modification
            printProductDiscounts(product);
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }

    // Helper method to print all discounts for a product
    private void printProductDiscounts(Product product) {
        HashMap<Integer, Double> discounts = product.getDiscountDetails();
        if (discounts.isEmpty()) {
            System.out.println("No discounts available for this product.");
        } else {
            System.out.println("Current discounts for Product " + product.getName() + ":");
            discounts.forEach((quantity, percent) ->
                    System.out.println("  - Buy " + quantity + " units or more: " + percent + "% discount")
            );
        }
    }

    // Helper method for validated quantity input with retry mechanism
    private int getValidatedQuantityInput() {
        int quantity = -1;
        while (quantity <= 0) {
            quantity = inputValidator.getValidatedInt("Enter quantity for discount: ");
            if (quantity <= 0) {
                System.out.println("Invalid quantity. Please enter a positive number.");
            }
        }
        return quantity;
    }

    // 7. Print All Suppliers
    public void printAllSuppliers() {
        if (supplierController.getSuppliers().isEmpty()){
            System.out.println("No existing suppliers in the system.");
            return;
        }
        supplierController.getSuppliers().forEach(Supplier::printSupplierDetails);
    }

    // 8. Print All Orders
    public void printAllOrders() {
        orderController.generateOrdersReport();
    }

    private PaymentMethod getValidatedPaymentMethod() {
        String paymentInput = inputValidator.getValidatedInput("Enter payment method (credit/cash/transfer): ").toLowerCase();
        switch (paymentInput) {
            case "credit":
                return PaymentMethod.CreditCard;
            case "cash":
                return PaymentMethod.Cash;
            case "transfer":
                return PaymentMethod.BankTransfer;
            default:
                System.out.println("Invalid payment method.");
                return getValidatedPaymentMethod();
        }
    }

    private List<String> selectSupplyDays() {
        List<String> supplyDays = new ArrayList<>();
        Set<String> validDays = new HashSet<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"));

        while (true) {
            String day = inputValidator.getValidatedInput("Enter supply day (type 'done' to finish): ").trim().toLowerCase();

            if (day.equals("done")) {
                break;  // Exit loop when user types "done"
            }

            if (!validDays.contains(day)) {
                System.out.println("Invalid day of the week. Please enter a valid day (e.g., 'monday').");
                continue;
            }

            if (supplyDays.contains(day)) {
                System.out.println("Day already entered. Please enter a different day.");
            } else {
                supplyDays.add(day);
                System.out.println(day.substring(0, 1).toUpperCase() + day.substring(1) + " added.");
            }
        }

        return supplyDays;
    }

    // 9. Update Supplier Agreement
    private void updateSupplierAgreement(Agreement agreement) {
        if (agreement == null) {
            System.out.println("Supplier has no agreement.");
            return;
        }

        System.out.println("Updating supplier's agreement.");
        boolean keepUpdating = true;
        while (keepUpdating) {
            System.out.println("1. Update Supply Days\n2. Update Self Supply\n3. Exit");
            int choice = inputValidator.getValidatedInt("Choose an option: ");

            switch (choice) {
                case 1:
                    List<String> supplyDays = selectSupplyDays();
                    agreement.setSupplyDays(supplyDays);
                    System.out.println("Supply days updated to: " + supplyDays);
                    break;
                case 2:
                    boolean selfSupply = Boolean.parseBoolean(inputValidator.getValidatedInput("Is the supplier responsible for supplying products? (true/false): ", input -> input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"), "Please enter 'true' or 'false'."));
                    agreement.setSelfSupply(selfSupply);
                    System.out.println("Self-supply field has been updated to: " + (selfSupply ? "Supplier responsible" : "Company responsible"));
                    break;
                case 3:
                    keepUpdating = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void uploadBasicInformation(){

        Supplier supplier1 = supplierController.createSupplier("S1", "Bank1", PaymentMethod.CreditCard, null, "Supplier One", "123456", "email1@example.com");

        // Creating discounts and products for Supplier 1
        HashMap<Integer, Double> product1Discounts = new HashMap<>();
        product1Discounts.put(10, 5.0);  // 5% discount for 10 or more units
        product1Discounts.put(20, 10.0); // 10% discount for 20 or more units

        HashMap<Integer, Double> product2Discounts = new HashMap<>();
        product2Discounts.put(5, 3.0);   // 3% discount for 5 or more units
        product2Discounts.put(15, 7.0);  // 7% discount for 15 or more units

        Product product1 = productController.createProduct("Product 1", product1Discounts, 10.0, 30, 2.0, null);
        Product product2 = productController.createProduct("Product 2", product2Discounts, 20.0, 50, 3.5, null);

        Agreement agreement1 = agreementController.createAgreement(new ArrayList<>(List.of(product1, product2)), new HashMap<>(), new ArrayList<>(List.of("Monday", "Wednesday")), true);

        supplier1.setSupplierAgreement(agreement1);  // Link agreement to supplier
        orderController.addSupplier(supplier1);  // Add supplier to OrderController

        // Example 2: Supplier 2 with Agreement and Products
        Supplier supplier2 = supplierController.createSupplier("S2", "Bank2", PaymentMethod.Cash, null, "Supplier Two", "654321", "email2@example.com");

        // Creating discounts and products for Supplier 2
        HashMap<Integer, Double> product3Discounts = new HashMap<>();
        product3Discounts.put(8, 4.0);   // 4% discount for 8 or more units
        product3Discounts.put(12, 8.0);  // 8% discount for 12 or more units

        HashMap<Integer, Double> product4Discounts = new HashMap<>();
        product4Discounts.put(10, 6.0);  // 6% discount for 10 or more units
        product4Discounts.put(20, 12.0); // 12% discount for 20 or more units

        Product product3 = productController.createProduct("Product 3", product3Discounts, 15.0, 40, 5.0, null);
        Product product4 = productController.createProduct("Product 4", product4Discounts, 25.0, 60, 1.5, null);


        Agreement agreement2 = agreementController.createAgreement(new ArrayList<>(List.of(product3, product4)), new HashMap<>(), new ArrayList<>(List.of("Tuesday", "Thursday")), false);

        supplier2.setSupplierAgreement(agreement2);  // Link agreement to supplier
        orderController.addSupplier(supplier2);  // Add supplier to OrderController

        // Example Orders
        System.out.println("Creating example orders...");

        // Creating orders for supplier 1
        HashMap<Product, Integer> order1Products = new HashMap<>();
        order1Products.put(product1, 15);  // 15 units of Product 1
        order1Products.put(product2, 7);   // 7 units of Product 2
        Order order1 = new Order(supplier1.getSupplierID(), new java.util.Date(), order1Products);
        orderController.addOrder(order1);

        // Creating orders for supplier 2
        HashMap<Product, Integer> order2Products = new HashMap<>();
        order2Products.put(product3, 9);   // 9 units of Product 3
        order2Products.put(product4, 12);  // 12 units of Product 4
        Order order2 = new Order(supplier2.getSupplierID(), new java.util.Date(), order2Products);
        orderController.addOrder(order2);
    }

    public void uploadOnlyOrders(){
        //Order: String supplierID, Date orderDate, HashMap<Product, Integer> productQuantityMap
        //Product: String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement
        //Agreement: List<Product> productList, HashMap<String, HashMap<Integer, Double>> discountDetails, List<String> supplyDays, boolean selfSupply) {

        String SupplierID1 = "SP1";
        String SupplierID2 = "SP2";
        Date date = new java.util.Date(); // Order date for all orders

        // Create the product-quantity maps for each order
        HashMap<Product, Integer> productQuantityMap1 = new HashMap<>();
        HashMap<Product, Integer> productQuantityMap2 = new HashMap<>();
        HashMap<Product, Integer> productQuantityMap3 = new HashMap<>();
        HashMap<Product, Integer> productQuantityMap4 = new HashMap<>();

        // Define discounts for the products for each supplier
        HashMap<Integer, Double> bananaDiscountSupplier1= new HashMap<>();
        HashMap<Integer, Double> tableDiscountSupplier1 = new HashMap<>();
        HashMap<Integer, Double> tableDiscountSupplier2 = new HashMap<>();
        HashMap<Integer, Double> noteBookDiscountSupplier1 = new HashMap<>();
        HashMap<Integer, Double> noteBookDiscountSupplier2 = new HashMap<>();
        HashMap<Integer, Double> phoneDiscountSupplier2 = new HashMap<>();

        // Adding discount values for products
        bananaDiscountSupplier1.put(10, 5.0);
        bananaDiscountSupplier1.put(15, 10.0);
        bananaDiscountSupplier1.put(20, 12.0);

        tableDiscountSupplier1.put(2, 25.0);
        tableDiscountSupplier1.put(4, 50.0);

        tableDiscountSupplier2.put(3, 28.0);
        tableDiscountSupplier2.put(5, 54.0);

        noteBookDiscountSupplier1.put(10, 15.0);
        noteBookDiscountSupplier1.put(20, 30.0);

        noteBookDiscountSupplier2.put(10, 20.0);
        noteBookDiscountSupplier2.put(25, 28.0);

        phoneDiscountSupplier2.put(2, 8.0);

        // Create products
        Product banana = new Product("Banana", bananaDiscountSupplier1, 3, 7, 1, null); // No agreement yet
        Product tableSupplier1 = new Product("Table", tableDiscountSupplier1, 400,  3650, 8, null); // Agreement will be assigned later
        Product tableSupplier2 = new Product("Table", tableDiscountSupplier2, 400,  3650, 8, null); // Agreement will be assigned later
        Product noteBookSupplier1 = new Product("Notebook", noteBookDiscountSupplier1, 5, 180, 1.5, null); // Agreement will be assigned later
        Product noteBookSupplier2 = new Product("Notebook", noteBookDiscountSupplier2, 5, 180, 1.5, null); // Agreement will be assigned later
        Product phone = new Product("Phone", phoneDiscountSupplier2, 2500, 1000, 2, null); // Agreement will be assigned later

        // Create agreements for suppliers
        List<Product> productList1 = new ArrayList<>(List.of(banana, tableSupplier1, noteBookSupplier1));
        List<Product> productList2 = new ArrayList<>(List.of(tableSupplier2, noteBookSupplier2, phone));

        Agreement agreement1 = new Agreement(productList1, new HashMap<>(), List.of("Monday", "Wednesday"), true); // Self-supply
        Agreement agreement2 = new Agreement(productList2, new HashMap<>(), List.of("Tuesday", "Thursday"), false); // Supplier-supply

        // Assign the agreements to the products
        banana.setAgreement(agreement1);
        tableSupplier1.setAgreement(agreement1);
        noteBookSupplier1.setAgreement(agreement1);

        tableSupplier2.setAgreement(agreement2);
        noteBookSupplier2.setAgreement(agreement2);
        phone.setAgreement(agreement2);

        // Add product quantities to the maps for the orders
        productQuantityMap1.put(banana, 12); // Order 1, 12 bananas
        productQuantityMap1.put(tableSupplier1, 3); // Order 1, 3 tables

        productQuantityMap2.put(noteBookSupplier1, 15); // Order 2, 15 notebooks

        productQuantityMap3.put(tableSupplier2, 4); // Order 3, 4 tables
        productQuantityMap3.put(phone, 1); // Order 3, 1 phone

        productQuantityMap4.put(noteBookSupplier2, 20); // Order 4, 20 notebooks

        // Create orders
        Order order1 = new Order(SupplierID1, date, productQuantityMap1);
        Order order2 = new Order(SupplierID1, date, productQuantityMap2);
        Order order3 = new Order(SupplierID2, date, productQuantityMap3);
        Order order4 = new Order(SupplierID2, date, productQuantityMap4);

        // Add the orders to a list for demonstration purposes
        List<Order> orders = Arrays.asList(order1, order2, order3, order4);

        for(Order order: orders){
            orderController.addOrder(order);
        }
    }
}
