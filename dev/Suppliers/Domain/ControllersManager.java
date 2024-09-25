package dev.Suppliers.Domain;

import dev.Suppliers.Domain.Exception.ExitException;
import dev.Suppliers.Enums.PaymentMethod;

import java.util.*;
import java.util.stream.Collectors;

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
            // Step 1: Gather Supplier Information
            String name = inputValidator.getValidatedInput("Enter Supplier's Name: ");
            String phoneNumber = inputValidator.getValidatedInput("Enter Supplier's phone number: ", inputValidator::isValidPhoneNumber, "Phone number must contain only digits.");
            String email = inputValidator.getValidatedInput("Enter Supplier's email: ", inputValidator::isValidEmail, "Invalid email format.");
            String companyID = inputValidator.getValidatedInput("Enter Supplier's company ID: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Company ID format.");
            String bankAccount = inputValidator.getValidatedInput("Enter Supplier's bank account: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Bank account format.");
            PaymentMethod paymentMethod = getValidatedPaymentMethod();

            // Step 2: Collect Products Information Locally (without saving in DB)
            List<Product> productList = new ArrayList<>();
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

                // Create the product locally and store it in the list (not in the database yet)
                Product product = new Product(0, productName, discountDetails, price, expirationDays, weight, null);
                productList.add(product);

                addMoreProducts = inputValidator.getValidatedYesNoInput("Add another product? (yes/no): ").equalsIgnoreCase("yes");
            } while (addMoreProducts);



            // Step 4: Set up the agreement with supply days and self-supply option
            List<String> supplyDays = selectSupplyDays();
            boolean selfSupply = Boolean.parseBoolean(inputValidator.getValidatedYesNoInput("Is the supplier responsible for supplying products? (yes/no): "));

            // Step 3: Create Supplier (get the generated supplierID)
            Supplier supplier = supplierController.createSupplier(companyID, bankAccount, String.valueOf(paymentMethod), null, name, phoneNumber, email);
            int supplierID = supplier.getSupplierID();

            // Create the agreement with the product list and supplierID
            Agreement agreement = agreementController.createAgreement(productList, supplyDays, selfSupply,supplierID);
            int agreementID = agreement.getAgreementID();

            // Step 5: Update the supplier in the database with the agreementID
            supplierController.updateSupplierAgreement(supplierID, agreementID);

            // Step 6: Create each product in the database with agreementID
            for (Product product : productList) {
                product.setAgreement(agreement);
                productController.createProduct(product.getName(), product.getDiscountDetails(), product.getPrice(), product.getExpirationDays(), product.getWeight(), agreement);
            }

            System.out.println("Supplier successfully created.");
            supplier.printSupplierDetails();
        } catch (ExitException e) {
            System.out.println("Action cancelled.");
        }
    }
    // 2. Delete Supplier Card
//    public void deleteSupplierCard() {
//        try {
//            if (supplierController.getSuppliers().isEmpty()) {
//                System.out.println("No suppliers available.");
//                return;
//            }
//
//            // Display all available suppliers
//            System.out.println("Available suppliers:");
//            supplierController.getSuppliers().forEach(supplier ->
//                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));
//
//            // Step 1: Get valid supplier ID
//            Supplier supplier = null;
//            while (supplier == null) {
//                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
//                supplier = supplierController.getSupplierById(supplierID);
//
//                if (supplier == null) {
//                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
//                }
//            }
//
//            // Step 2: Confirm deletion
//            String confirmDeletion = inputValidator.getValidatedYesNoInput("Are you sure you want to delete this supplier? (yes/no)");
//            if (!confirmDeletion.equalsIgnoreCase("yes")) {
//                System.out.println("Supplier deletion cancelled.");
//                return;
//            }
//
//            // Step 3: Remove supplier from all products in ProductController
//            Agreement agreement = supplier.getSupplierAgreement();
//            if (agreement != null) {
//                for (Product product : agreement.getProductList()) {
//                    productController.removeSupplierFromProduct(product.getName(), supplier);
//                }
//            }
//
//            // Step 4: Delete the supplier's agreement if it exists
//            if (agreement != null) {
//                agreementController.deleteAgreement(agreement.getAgreementID());
//            }
//
//            // Step 5: Delete supplier
//            supplierController.deleteSupplier(supplier.getSupplierID());
//            System.out.println("Supplier successfully deleted.");
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }

//    public void addProductToSupplier() {
//        try {
//            if (supplierController.getSuppliers().isEmpty()) {
//                System.out.println("No suppliers available.");
//                return;
//            }
//
//            Supplier supplier = null;
//            while (supplier == null) {
//                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
//                supplier = supplierController.getSupplierById(supplierID);
//
//                if (supplier == null) {
//                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
//                }
//            }
//
//            if (supplier.getSupplierAgreement() == null) {
//                System.out.println("Supplier does not have an agreement.");
//                return;
//            }
//
//            // Collect product details
//            String productName = inputValidator.getValidatedInput("Enter Product's name: ");
//            double price = inputValidator.getValidatedDouble("Enter Product's price: ");
//            int expirationDays = inputValidator.getValidatedInt("Enter Product's expiration days: ");
//            double weight = inputValidator.getValidatedDouble("Enter Product's weight: ");
//
//            HashMap<Integer, Double> discountDetails = new HashMap<>();
//            if (inputValidator.getValidatedYesNoInput("Any discounts? (yes/no): ").equalsIgnoreCase("yes")) {
//                boolean addMoreDiscounts;
//                do {
//                    int quantity = inputValidator.getValidatedInt("Enter minimum quantity for discount: ");
//                    double discountPercent = inputValidator.getValidatedDiscountPercentage("Enter discount percent: ");
//                    discountDetails.put(quantity, discountPercent);
//
//                    addMoreDiscounts = inputValidator.getValidatedYesNoInput("Add another discount? (yes/no): ").equalsIgnoreCase("yes");
//                } while (addMoreDiscounts);
//            }
//
//            // Create and add the product to the supplier's agreement
//            Product product = productController.createProduct(productName, discountDetails, price, expirationDays, weight, null);
//            supplier.getSupplierAgreement().addProduct(product);
//            supplier.getSupplierAgreement().getDiscountDetails().put(product.getCatalogID(), discountDetails);
//
//            // Add the product and supplier to the productSupplierMap in ProductController
//            productController.addSupplierForProduct(productName, supplier);
//
//            System.out.println("Product added to supplier.");
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }
//
//    public void deleteProductFromSupplier() {
//        try {
//            if (supplierController.getSuppliers().isEmpty()) {
//                System.out.println("No suppliers available.");
//                return;
//            }
//
//            // Display all available suppliers
//            System.out.println("Available suppliers:");
//            supplierController.getSuppliers().forEach(supplier ->
//                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));
//
//            // Step 1: Get valid supplier ID
//            Supplier supplier = null;
//            while (supplier == null) {
//                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
//                supplier = supplierController.getSupplierById(supplierID);
//
//                if (supplier == null) {
//                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
//                }
//            }
//
//            if (supplier.getSupplierAgreement() == null) {
//                System.out.println("Supplier does not have an agreement.");
//                return;
//            }
//
//            // Step 2: Display all available products for the supplier
//            Agreement agreement = supplier.getSupplierAgreement();
//            List<Product> productList = agreement.getProductList();
//            if (productList.isEmpty()) {
//                System.out.println("No products available for this supplier.");
//                return;
//            }
//
//            System.out.println("Available products for Supplier " + supplier.getContact().getName() + ":");
//            for (Product product : productList) {
//                System.out.println("Catalog ID: " + product.getCatalogID() + ", Product Name: " + product.getName());
//            }
//
//            // Step 3: Get a valid product catalog ID
//            Product productToDelete = null;
//            while (productToDelete == null) {
//                String catalogID = inputValidator.getValidatedInput("Enter Product's catalog ID to delete: ");
//                productToDelete = productList.stream()
//                        .filter(product -> product.getCatalogID().equals(catalogID))
//                        .findFirst().orElse(null);
//
//                if (productToDelete == null) {
//                    System.out.println("Product not found. Please provide a valid Product ID.");
//                }
//            }
//
//            // Remove the product and supplier from the productSupplierMap in ProductController
//            productController.removeSupplierFromProduct(productToDelete.getName(), supplier);
//
//            // Step 4: Remove the product from the supplier's agreement
//            agreement.getProductList().remove(productToDelete);
//            agreement.getDiscountDetails().remove(productToDelete.getCatalogID());
//
//            System.out.println("Product successfully deleted.");
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }
//
//    public void updateSupplierFields() {
//        try {
//            if (supplierController.getSuppliers().isEmpty()) {
//                System.out.println("No suppliers available.");
//                return;
//            }
//
//            // Display all available suppliers
//            System.out.println("Available suppliers:");
//            supplierController.getSuppliers().forEach(supplier ->
//                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));
//
//            // Step 1: Get valid supplier ID
//            Supplier supplier = null;
//            while (supplier == null) {
//                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'): ", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
//                supplier = supplierController.getSupplierById(supplierID);
//
//                if (supplier == null) {
//                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
//                }
//            }
//
//            // Step 2: Update supplier fields
//            boolean keepUpdating = true;
//            while (keepUpdating) {
//                System.out.println("Which field would you like to update?");
//                System.out.println("1. Company ID\n2. Bank Account\n3. Payment Method\n4. Contact Info\n5. Supplier Agreement\n6. Exit");
//                int choice = inputValidator.getValidatedInt("Choose an option: ");
//
//                switch (choice) {
//                    case 1:
//                        String newCompanyID = inputValidator.getValidatedInput("Enter new Company ID: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Company ID. Must contain only digits.");
//                        supplier.setCompanyID(newCompanyID);
//                        System.out.println("Company ID has been changed to: " + newCompanyID);
//                        break;
//                    case 2:
//                        String newBankAccount = inputValidator.getValidatedInput("Enter new Bank Account: ", inputValidator::isValidCompanyOrBankAccount, "Invalid Bank Account. Must contain only digits.");
//                        supplier.setBankAccount(newBankAccount);
//                        System.out.println("Bank Account has been changed to: " + newBankAccount);
//                        break;
//                    case 3:
//                        PaymentMethod newPaymentMethod = getValidatedPaymentMethod();
//                        supplier.setPaymentMethod(newPaymentMethod);
//                        System.out.println("Payment Method has been changed to: " + newPaymentMethod);
//                        break;
//                    case 4:
//                        String name = inputValidator.getValidatedInput("Enter new Name: ");
//                        String phone = inputValidator.getValidatedInput("Enter new Phone: ", inputValidator::isValidPhoneNumber, "Invalid phone number. Must contain only digits.");
//                        String email = inputValidator.getValidatedInput("Enter new Email: ", inputValidator::isValidEmail, "Invalid email format.");
//                        supplier.setContact(new SupplierContact(name, phone, email));
//                        System.out.println("Contact Info updated: Name: " + name + ", Phone: " + phone + ", Email: " + email);
//                        break;
//                    case 5:
//                        updateSupplierAgreement(supplier.getSupplierAgreement());
//                        break;
//                    case 6:
//                        keepUpdating = false;
//                        break;
//                    default:
//                        System.out.println("Invalid option.");
//                }
//            }
//
//            // Print the updated supplier information after the updates are done
//            System.out.println("Supplier information after updates:");
//            supplier.printSupplierDetails();
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }
//
//    public void updateDiscountDetails() {
//        try {
//            if (supplierController.getSuppliers().isEmpty()) {
//                System.out.println("No suppliers available.");
//                return;
//            }
//
//            // Display all available suppliers
//            System.out.println("Available suppliers:");
//            supplierController.getSuppliers().forEach(supplier ->
//                    System.out.println("Supplier ID: " + supplier.getSupplierID() + ", Supplier Name: " + supplier.getContact().getName()));
//
//            // Step 1: Get valid supplier ID
//            Supplier supplier = null;
//            while (supplier == null) {
//                String supplierID = inputValidator.getValidatedInput("Enter Supplier ID (starting with 'S'):", inputValidator::isValidSupplierID, "Invalid Supplier ID. Please enter again.");
//                supplier = supplierController.getSupplierById(supplierID);
//
//                if (supplier == null) {
//                    System.out.println("Supplier not found. Please provide a valid Supplier ID.");
//                }
//            }
//
//            Agreement agreement = supplier.getSupplierAgreement();
//            if (agreement == null || agreement.getProductList().isEmpty()) {
//                System.out.println("No products available for this supplier.");
//                return;
//            }
//
//            // Display all available products for the supplier
//            System.out.println("Available products for Supplier " + supplier.getContact().getName() + ":");
//            for (Product product : agreement.getProductList()) {
//                System.out.println("Catalog ID: " + product.getCatalogID() + ", Product Name: " + product.getName());
//            }
//
//            // Step 2: Get valid Product ID
//            Product product = null;
//            while (product == null) {
//                String productID = inputValidator.getValidatedInput("Enter Product ID: ");
//                product = agreement.getProductList().stream()
//                        .filter(p -> p.getCatalogID().equalsIgnoreCase(productID))
//                        .findFirst().orElse(null);
//
//                if (product == null) {
//                    System.out.println("Product not found. Please provide a valid Product ID.");
//                }
//            }
//
//            // Print existing discounts for the product before add/delete decision
//            printProductDiscounts(product);
//
//            // Step 3: Ask to add or delete discount
//            String action = inputValidator.getValidatedInput("Do you want to add or delete a discount? (add/delete): ",
//                    input -> input.equalsIgnoreCase("add") || input.equalsIgnoreCase("delete"),
//                    "Please enter 'add' or 'delete'.");
//            if (action.equalsIgnoreCase("exit")) return;  // Exit option
//
//            if (action.equalsIgnoreCase("add")) {
//                // Adding a discount
//                int quantity = getValidatedQuantityInput();
//                double discountPercent = inputValidator.getValidatedDiscountPercentage("Enter discount percent: ");
//                agreementController.addDiscountToProduct(agreement, product.getCatalogID(), quantity, discountPercent);
//                System.out.println("Discount added successfully.");
//            } else if (action.equalsIgnoreCase("delete")) {
//                if (product.getDiscountDetails().isEmpty()) {
//                    System.out.println("No discounts available to delete.");
//                    return;
//                }
//                // Show existing discounts before deletion
//                printProductDiscounts(product);
//
//                int quantity = getValidatedQuantityInput();
//                if (product.getDiscountDetails().containsKey(quantity)) {
//                    agreementController.deleteDiscountFromProduct(agreement, product.getCatalogID(), quantity);
//                    System.out.println("Discount for quantity " + quantity + " deleted successfully.");
//                } else {
//                    System.out.println("No discount found for the entered quantity.");
//                }
//            }
//
//            // Print updated discounts after modification
//            printProductDiscounts(product);
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }

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


//    public void openNewOrder() {
//        try {
//            // Step 1: Choose order type
//            String orderType = inputValidator.getValidatedInput(
//                    "Choose the type of order:\n1. Shortage of products (one-time)\n2. Periodic product order (constant delivery)\nEnter 1 or 2: ",
//                    input -> input.equals("1") || input.equals("2"),
//                    "Invalid choice. Please enter 1 or 2."
//            );
//
//            boolean isConstantDelivery = orderType.equals("2");
//
//            // Step 2: Display all products available in the system (names only)
//            List<String> productNames = productController.getAllProductNames();
//            if (productNames.isEmpty()) {
//                System.out.println("No products available in the system.");
//                return;
//            }
//
//            System.out.println("Available products:");
//            productNames.forEach(System.out::println); // Print only product names
//
//            // Step 3: Select products and quantities
//            HashMap<Supplier, HashMap<Product, Integer>> supplierProductMap = new HashMap<>();
//            boolean addMoreProducts = true;
//
//            do {
//                String productName = inputValidator.getValidatedInput("Enter Product's name to order: ");
//
//                // Check if the product exists using getProductSupplierMap()
//                if (!productController.getProductSupplierMap().containsKey(productName)) {
//                    System.out.println("Product not found. Please enter a valid product name.");
//                    continue;
//                }
//
//                int quantity = inputValidator.getValidatedInt("Enter quantity to order: ");
//
//                // Find the cheapest supplier for the given product and quantity
//                Supplier cheapestSupplier = productController.findCheapestSupplier(productName, quantity);
//
//                if (cheapestSupplier == null) {
//                    System.out.println("No supplier found for the selected product and quantity.");
//                    continue;
//                }
//
//                // Get the product instance from the supplier's agreement
//                Product selectedProduct = cheapestSupplier.getSupplierAgreement().getProductByName(productName);
//
//                // Add the product and quantity to the supplier's product map
//                supplierProductMap.computeIfAbsent(cheapestSupplier, k -> new HashMap<>())
//                        .merge(selectedProduct, quantity, Integer::sum);
//
//                addMoreProducts = inputValidator.getValidatedYesNoInput("Add another product? (yes/no): ").equalsIgnoreCase("yes");
//            } while (addMoreProducts);
//
//            // Step 4: Create orders for each supplier in the map
//            for (Supplier supplier : supplierProductMap.keySet()) {
//                HashMap<Product, Integer> productQuantityMap = supplierProductMap.get(supplier);
//                orderController.createOrder(supplier, productQuantityMap, isConstantDelivery);
//            }
//
//            System.out.println("Orders successfully created.");
//
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }


//    public void updateConstantOrder() {
//        try {
//            // Step 1: Display all constant orders
//            List<Order> constantOrders = orderController.getActiveOrders().stream()
//                    .filter(Order::isConstantDelivery)
//                    .toList();
//
//            if (constantOrders.isEmpty()) {
//                System.out.println("No constant orders available.");
//                return;
//            }
//
//            System.out.println("Available constant orders:");
//            for (Order order : constantOrders) {
//                order.printOrderDetails();
//            }
//
//            // Step 2: Select an order to update
//            Order orderToUpdate = null;
//            while (orderToUpdate == null) {
//                String orderID = inputValidator.getValidatedInput("Enter Order ID to update: ");
//                orderToUpdate = constantOrders.stream()
//                        .filter(order -> order.getOrderID().equals(orderID))
//                        .findFirst()
//                        .orElse(null);
//
//                if (orderToUpdate == null) {
//                    System.out.println("Order not found. Please provide a valid Order ID.");
//                }
//            }
//
//            // Step 3: Provide update options
//            String updateOption = inputValidator.getValidatedInput(
//                    "Choose an option to update the order:\n1. Turn into non-constant order\n2. Update product list\nEnter 1 or 2: ",
//                    input -> input.equals("1") || input.equals("2"),
//                    "Invalid choice. Please enter 1 or 2."
//            );
//
//            if (updateOption.equals("1")) {
//                // Turn order into non-constant and exit
//                orderController.turnConstantOrderToRegular(orderToUpdate.getOrderID());
//                System.out.println("Order has been changed to a non-constant order.");
//                return;
//            } else if (updateOption.equals("2")) {
//                // Check if the order is arriving tomorrow
//
//                Calendar calendar = Calendar.getInstance();
//                int todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get current day of the week (e.g., Sunday = 1, Monday = 2, ...)
//
//                calendar.setTime(orderToUpdate.getDeliveryDate());
//                int deliveryDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get delivery day of the week
//
//                if ((todayDayOfWeek + 1) % 7 == deliveryDayOfWeek) { // Check if tomorrow is the delivery day
//                    System.out.println("It is not possible to update the order a day before its arrival.");
//                    return;
//                }
//
//                // Display available products from the supplier
//                Supplier supplier = orderToUpdate.getSupplier();
//                List<Product> availableProducts = supplier.getSupplierAgreement().getProductList();
//                System.out.println("Available products from Supplier " + supplier.getContact().getName() + ":");
//                for (Product product : availableProducts) {
//                    System.out.println("Product Name: " + product.getName());
//                }
//
//                // Create a new product-quantity map
//                HashMap<Product, Integer> newProductQuantityMap = new HashMap<>();
//                boolean addMoreProducts = true;
//                do {
//                    String productName = inputValidator.getValidatedInput("Enter Product's name to order: ");
//                    Product selectedProduct = availableProducts.stream()
//                            .filter(product -> product.getName().equalsIgnoreCase(productName))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (selectedProduct == null) {
//                        System.out.println("Product not found. Please enter a valid product name.");
//                        continue;
//                    }
//
//                    int quantity = inputValidator.getValidatedInt("Enter quantity to order: ");
//                    newProductQuantityMap.put(selectedProduct, quantity);
//
//                    addMoreProducts = inputValidator.getValidatedYesNoInput("Add another product? (yes/no): ").equalsIgnoreCase("yes");
//                } while (addMoreProducts);
//
//                // Update the order with the new product list
//                orderController.updateOrder(orderToUpdate.getOrderID(), newProductQuantityMap);
//                System.out.println("Order product list has been successfully updated.");
//            }
//        } catch (ExitException e) {
//            System.out.println("Action cancelled.");
//        }
//    }


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

    // Updated uploadBasicInformation method
//    public void uploadBasicInformation() {
//        Supplier supplier1 = supplierController.createSupplier("S1", "Bank1", PaymentMethod.CreditCard, null, "Supplier One", "123456", "email1@example.com");
//
//        // Creating discounts and products for Supplier 1
//        HashMap<Integer, Double> product1Discounts = new HashMap<>();
//        product1Discounts.put(10, 5.0);  // 5% discount for 10 or more units
//        product1Discounts.put(20, 10.0); // 10% discount for 20 or more units
//
//        HashMap<Integer, Double> product2Discounts = new HashMap<>();
//        product2Discounts.put(5, 3.0);   // 3% discount for 5 or more units
//        product2Discounts.put(15, 7.0);  // 7% discount for 15 or more units
//
//        Product product1 = productController.createProduct("Product 1", product1Discounts, 10.0, 30, 2.0, null);
//        Product product2 = productController.createProduct("Product 2", product2Discounts, 20.0, 50, 3.5, null);
//
//        Agreement agreement1 = agreementController.createAgreement(new ArrayList<>(List.of(product1, product2)), new HashMap<>(), new ArrayList<>(List.of("Monday", "Wednesday")), true);
//
//        supplier1.setSupplierAgreement(agreement1);  // Link agreement to supplier
//        productController.addSupplierForProduct("Product 1", supplier1); // Add supplier to product
//        productController.addSupplierForProduct("Product 2", supplier1);
//
//        // Example Orders for Supplier 1
//        HashMap<Product, Integer> order1Products = new HashMap<>();
//        order1Products.put(product1, 15);  // 15 units of Product 1
//        order1Products.put(product2, 7);   // 7 units of Product 2
//        orderController.createOrder(supplier1, order1Products, true);  // Create one-time order
//
//        Supplier supplier2 = supplierController.createSupplier("S2", "Bank2", PaymentMethod.Cash, null, "Supplier Two", "654321", "email2@example.com");
//
//        // Creating discounts and products for Supplier 2
//        HashMap<Integer, Double> product3Discounts = new HashMap<>();
//        product3Discounts.put(8, 4.0);   // 4% discount for 8 or more units
//        product3Discounts.put(12, 8.0);  // 8% discount for 12 or more units
//
//        HashMap<Integer, Double> product4Discounts = new HashMap<>();
//        product4Discounts.put(10, 6.0);  // 6% discount for 10 or more units
//        product4Discounts.put(20, 12.0); // 12% discount for 20 or more units
//
//        Product product3 = productController.createProduct("Product 3", product3Discounts, 15.0, 40, 5.0, null);
//        Product product4 = productController.createProduct("Product 4", product4Discounts, 25.0, 60, 1.5, null);
//
//        Agreement agreement2 = agreementController.createAgreement(new ArrayList<>(List.of(product3, product4)), new HashMap<>(), new ArrayList<>(List.of("Tuesday", "Thursday")), false);
//
//        supplier2.setSupplierAgreement(agreement2);  // Link agreement to supplier
//        productController.addSupplierForProduct("Product 3", supplier2);
//        productController.addSupplierForProduct("Product 4", supplier2);
//
//        // Example Orders for Supplier 2
//        HashMap<Product, Integer> order2Products = new HashMap<>();
//        order2Products.put(product3, 9);   // 9 units of Product 3
//        order2Products.put(product4, 12);  // 12 units of Product 4
//        orderController.createOrder(supplier2, order2Products, true);  // Create one-time order
//    }
//    // Updated uploadOnlyOrders method
//    public void uploadOnlyOrders() {
//        // Create orders using only the product and supplier objects in the orders
//        Date date = new Date(); // Order date for all orders
//
//        Supplier tempSupplier1 = new Supplier("S1", "Bank1", PaymentMethod.CreditCard, null, null); // Temporary Supplier
//        Supplier tempSupplier2 = new Supplier("S2", "Bank2", PaymentMethod.Cash, null, null); // Temporary Supplier
//
//        // Create products
//        Product tempProduct1 = new Product("Banana", new HashMap<>(), 3, 7, 1, null);
//        Product tempProduct2 = new Product("Table", new HashMap<>(), 400, 365, 8, null);
//        Product tempProduct3 = new Product("Notebook", new HashMap<>(), 5, 180, 1.5, null);
//        Product tempProduct4 = new Product("Phone", new HashMap<>(), 2500, 1000, 2, null);
//
//        // Create order 1 for temporary supplier 1
//        HashMap<Product, Integer> productQuantityMap1 = new HashMap<>();
//        productQuantityMap1.put(tempProduct1, 12);
//        productQuantityMap1.put(tempProduct2, 3);
//        orderController.createOrder(tempSupplier1, productQuantityMap1, false); // One-time order
//
//        // Create order 2 for temporary supplier 1
//        HashMap<Product, Integer> productQuantityMap2 = new HashMap<>();
//        productQuantityMap2.put(tempProduct3, 15);
//        orderController.createOrder(tempSupplier1, productQuantityMap2, true); // One-time order
//
//        // Create order 3 for temporary supplier 2
//        HashMap<Product, Integer> productQuantityMap3 = new HashMap<>();
//        productQuantityMap3.put(tempProduct2, 4);
//        productQuantityMap3.put(tempProduct4, 1);
//        orderController.createOrder(tempSupplier2, productQuantityMap3, false); // One-time order
//
//        // Create order 4 for temporary supplier 2
//        HashMap<Product, Integer> productQuantityMap4 = new HashMap<>();
//        productQuantityMap4.put(tempProduct3, 20);
//        orderController.createOrder(tempSupplier2, productQuantityMap4, true); // One-time order
//    }

    // Method to print all active orders
    public void printActiveOrders() {
        List<Order> activeOrders = orderController.getActiveOrders();
        for (Order order : activeOrders) {
            order.printOrderDetails();
        }
    }
}
