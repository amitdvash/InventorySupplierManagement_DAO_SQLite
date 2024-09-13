package dev.Suppliers.Domain;

import dev.Suppliers.Enums.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ControllersManager {

    private final SupplierController supplierController;
    private final ProductController productController;
    private final AgreementController agreementController;

    public ControllersManager(SupplierController supplierController, ProductController productController, AgreementController agreementController) {
        this.supplierController = supplierController;
        this.productController = productController;
        this.agreementController = agreementController;
    }

    public void openNewSupplierCard() {
        Scanner scanner = new Scanner(System.in);

        // 1. Ask for Supplier details with validation
        String name = getValidatedInput(scanner, "Enter Supplier's Name:", input -> !input.isEmpty(), "Name cannot be empty, please reply a valid answer");

        String phoneNumber = getValidatedInput(scanner, "Enter Supplier's phone number:", this::isValidPhoneNumber, "Phone number must contain only positive numbers, please reply a valid answer");

        String email = getValidatedInput(scanner, "Enter Supplier's email:", this::isValidEmail, "Invalid email address, please reply a valid answer");

        String companyID = getValidatedInput(scanner, "Enter Supplier's company ID:", this::isPositiveNumber, "Company ID must contain only positive numbers, please reply a valid answer");

        String bankAccount = getValidatedInput(scanner, "Enter Supplier's bank account:", this::isPositiveNumber, "Bank account must contain only positive numbers, please reply a valid answer");

        // Payment method selection using string input (credit, cash, transfer)
        PaymentMethod paymentMethod = getValidatedPaymentMethod(scanner);

        // 2. Create Products
        List<Product> productList = new ArrayList<>();
        HashMap<String, HashMap<Integer, Double>> agreementDiscounts = new HashMap<>();
        String addMoreProducts;
        do {

            // Use next() for simple single-word input
            String productName = getValidatedInput(scanner, "Enter Product's name:", input -> !input.isEmpty(), "Product's name cannot be empty, please reply a valid answer");

            double price = getValidatedDouble(scanner, "Enter Product's price:", input -> input > 0, "Price must be positive, please reply a valid answer");

            int expirationDays = getValidatedInt(scanner, "Enter Product's expiration days:", 1, Integer.MAX_VALUE, "Expiration days must be a positive number, please reply a valid answer");

            double weight = getValidatedDouble(scanner, "Enter Product's weight:", input -> input > 0, "Weight must be positive, please reply a valid answer");

            // Ask if there are discount details for the product
            HashMap<Integer, Double> discountDetails = new HashMap<>();
            String hasDiscountDetails = getValidatedInput(scanner, "Are there any discount details for the product? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'");

            if (hasDiscountDetails.equalsIgnoreCase("yes")) {
                while (true) {
                    int quantity = getValidatedInt(scanner, "Enter the minimum quantity for the discount:", 1, Integer.MAX_VALUE, "Quantity must be a positive number.");
                    double discountPercent = getValidatedDouble(scanner, "Enter the discount percent (e.g., 10 for 10%):", input -> input > 0, "Discount percent must be positive.");

                    discountDetails.put(quantity, discountPercent);

                    String addMoreDiscounts = getValidatedInput(scanner, "Do you want to add another discount detail? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'.");

                    if (addMoreDiscounts.equalsIgnoreCase("no")) {
                        break;
                    }
                }
            }

            // Create Product (Agreement is null for now, to be set later)
            Product product = productController.createProduct(productName, discountDetails, price, expirationDays, weight, null);
            productList.add(product);

            // Add product's discount details to the agreement's discount map
            agreementDiscounts.put(product.getCatalogID(), discountDetails);

            addMoreProducts = getValidatedInput(scanner, "Do you want to add more products? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'");
        } while (addMoreProducts.equalsIgnoreCase("yes"));

        // 3. Supply Days selection using switch case
        List<String> supplyDays = selectSupplyDays(scanner);

        // 4. Create Agreement
        String selfSupplyInput = getValidatedInput(scanner, "Is the supplier responsible for supplying products? (true/false):", input -> input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"), "Please enter 'true' or 'false'");
        boolean selfSupply = Boolean.parseBoolean(selfSupplyInput);

        Agreement agreement = agreementController.createAgreement(productList, agreementDiscounts, supplyDays, selfSupply);

        // 5. Create Supplier using the agreement
        Supplier supplier = supplierController.createSupplier(companyID, bankAccount, paymentMethod, agreement, name, phoneNumber, email);

        // Associate Supplier with the Agreement and Products
        agreement.setSupplier(supplier);  // Link supplier to agreement
        for (Product product : productList) {
            product.setAgreement(agreement);  // Link agreement to products
        }

        System.out.println("Supplier successfully created with the following details:");
        supplier.printSupplierDetails();
    }


    public void deleteSupplierCard() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Check if there are any suppliers in the system
        if (supplierController.getSuppliers().isEmpty()) {
            System.out.println("No suppliers available in the system.");
            return;
        }

        // Step 2: Find the supplier by Supplier ID
        Supplier supplier = null;
        while (supplier == null) {
            String supplierID = getValidatedSupplierID(scanner, "Enter Supplier's ID (e.g., S1):", "Supplier ID must start with 'S' followed by numbers.");
            supplier = supplierController.getSupplierById(supplierID);

            if (supplier == null) {
                System.out.println("Supplier not found. Please provide a valid Supplier ID.");
            }
        }

        // Step 3: Confirm deletion
        String confirmDeletion = getValidatedInput(scanner, "Are you sure you want to delete Supplier " + supplier.getContact().getName() + "? This action cannot be undone. (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'.");
        if (!confirmDeletion.equalsIgnoreCase("yes")) {
            System.out.println("Supplier deletion cancelled.");
            return;
        }

        // Step 4: Delete the agreement first
        Agreement agreement = supplier.getSupplierAgreement();
        if (agreement != null) {
            agreementController.deleteAgreement(agreement.getAgreementID());
        }

        // Step 5: Delete the supplier from the SupplierController
        supplierController.deleteSupplier(supplier.getSupplierID());

        System.out.println("Supplier " + supplier.getContact().getName() + " and associated agreement successfully deleted.");
    }

    public void addProductToSupplier() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Check if there are any suppliers in the system
        if (supplierController.getSuppliers().isEmpty()) {
            System.out.println("No suppliers available in the system, I suggest you create a supplier first.");
            return;
        }

        // Step 2: Find the supplier by Supplier ID
        Supplier supplier = null;
        while (supplier == null) {
            String supplierID = getValidatedSupplierID(scanner, "Enter Supplier's ID (e.g., S1):", "Supplier ID must start with 'S' followed by numbers.");
            supplier = supplierController.getSupplierById(supplierID);

            if (supplier == null) {
                System.out.println("Supplier not found. Please provide a valid Supplier ID.");
            }
        }

        // Step 3: Collect product details
        String productName = getValidatedInput(scanner, "Enter Product's name:", input -> !input.isEmpty(), "Product's name cannot be empty.");

        double price = getValidatedDouble(scanner, "Enter Product's price:", input -> input > 0, "Price must be positive.");

        int expirationDays = getValidatedInt(scanner, "Enter Product's expiration days:", 1, Integer.MAX_VALUE, "Expiration days must be a positive number.");

        double weight = getValidatedDouble(scanner, "Enter Product's weight:", input -> input > 0, "Weight must be positive.");

        // Step 4: Collect discount details for the product
        HashMap<Integer, Double> discountDetails = new HashMap<>();
        String hasDiscountDetails = getValidatedInput(scanner, "Are there any discount details for the product? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'.");

        if (hasDiscountDetails.equalsIgnoreCase("yes")) {
            while (true) {
                int quantity = getValidatedInt(scanner, "Enter the minimum quantity for the discount:", 1, Integer.MAX_VALUE, "Quantity must be a positive number.");
                double discountPercent = getValidatedDouble(scanner, "Enter the discount percent (e.g., 10 for 10%):", input -> input > 0, "Discount percent must be positive.");

                discountDetails.put(quantity, discountPercent);

                String addMoreDiscounts = getValidatedInput(scanner, "Do you want to add another discount detail? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'.");

                if (addMoreDiscounts.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }

        // Step 5: Create the new product (without the agreement for now)
        Product product = productController.createProduct(productName, discountDetails, price, expirationDays, weight, null);

        // Step 6: Add the product to the Supplier's product list
        //supplier.addProduct(product);

        // Step 7: Add the product to the corresponding Agreement's product list
        Agreement agreement = supplier.getSupplierAgreement();  // Assuming the Supplier has a method to get its Agreement
        agreement.addProduct(product);

        // Step 8: Add the product's discount details to the Agreement's discount HashMap
        agreement.getDiscountDetails().put(product.getCatalogID(), discountDetails);

        // Link the product to the agreement
        product.setAgreement(agreement);

        System.out.println("Product successfully added to Supplier " + supplier.getContact().getName());
    }



    public void deleteProductFromSupplier() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Check if there are any suppliers in the system
        if (supplierController.getSuppliers().isEmpty()) {
            System.out.println("No suppliers available in the system.");
            return;
        }

        // Step 2: Find the supplier by Supplier ID
        Supplier supplier = null;
        while (supplier == null) {
            String supplierID = getValidatedSupplierID(scanner, "Enter Supplier's ID (e.g., S1):", "Supplier ID must start with 'S' followed by numbers.");
            supplier = supplierController.getSupplierById(supplierID);

            if (supplier == null) {
                System.out.println("Supplier not found. Please provide a valid Supplier ID.");
            }
        }

        // Step 3: Get the product catalog ID to delete
        Agreement agreement = supplier.getSupplierAgreement();  // Get the supplier's agreement
        if (agreement == null) {
            System.out.println("This supplier has no agreement with any products.");
            return;
        }

        String catalogID = getValidatedInput(scanner, "Enter Product's catalog ID to delete:", input -> !input.isEmpty(), "Catalog ID cannot be empty.");

        // Step 4: Find the product in the agreement's product list by catalog ID
        Product productToDelete = null;
        for (Product product : agreement.getProductList()) {
            if (product.getCatalogID().equalsIgnoreCase(catalogID)) {
                productToDelete = product;
                break;
            }
        }

        if (productToDelete == null) {
            System.out.println("Product not found in the supplier's agreement.");
            return;
        }

        // Step 5: Delete the product from the agreement's product list
        agreement.getProductList().remove(productToDelete);

        // Step 6: Delete the product's discount details from the agreement's discount HashMap
        agreement.getDiscountDetails().remove(productToDelete.getCatalogID());

        System.out.println("Product with Catalog ID " + catalogID + " successfully deleted from Supplier " + supplier.getContact().getName());
    }

    public void printAllSuppliers(){
        for (Supplier supplier : supplierController.getSuppliers()){
            supplier.printSupplierDetails();
        }

    }
    // New validation method for supplier ID
    private String getValidatedSupplierID(Scanner scanner, String prompt, String errorMessage) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim(); // Remove any extra spaces

            // Validate that the input starts with 'S' and is followed by digits
            if (input.matches("S\\d+")) {
                return input;  // Input is valid, return it
            } else {
                System.out.println(errorMessage);  // Invalid, show error message and ask again
            }
        } while (true);  // Loop until valid input is provided
    }

// New method to get payment method as a string input (credit, cash, transfer)
    private PaymentMethod getValidatedPaymentMethod(Scanner scanner) {
        String input;
        do {
            System.out.println("Choose Supplier's Payment Method (credit, cash, transfer):");
            input = scanner.next().trim().toLowerCase();  // Convert input to lowercase and remove extra spaces
            if (input.equals("credit")) {
                return PaymentMethod.CreditCard;
            } else if (input.equals("cash")) {
                return PaymentMethod.Cash;
            } else if (input.equals("transfer")) {
                return PaymentMethod.BankTransfer;
            } else {
                System.out.println("Invalid input. Please enter 'credit', 'cash', or 'transfer'.");
            }
        } while (true);  // Keep asking until valid input is provided
    }


    // Utility methods for input validation
// For short input like "true/false", we use `next()` instead of `nextLine()`.
    private String getValidatedInput(Scanner scanner, String prompt, InputValidator<String> validator, String errorMessage) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.next();  // Use next() for single-word input to avoid issues
            if (!validator.isValid(input)) {
                System.out.println(errorMessage);
            }
        } while (!validator.isValid(input));
        return input;
    }

    private int getValidatedInt(Scanner scanner, String prompt, int minValue, int maxValue) {
        return getValidatedInt(scanner, prompt, minValue, maxValue, "Please enter a valid number");
    }

    private int getValidatedInt(Scanner scanner, String prompt, int minValue, int maxValue, String errorMessage) {
        int value;
        do {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                scanner.nextLine();  // Clear the buffer after valid input
                if (value >= minValue && value <= maxValue) {
                    return value;  // Return the valid input
                } else {
                    System.out.println(errorMessage);
                }
            } else {
                System.out.println(errorMessage);
                scanner.next();  // Clear invalid input
            }
        } while (true);  // Loop until a valid input is entered
    }

    private double getValidatedDouble(Scanner scanner, String prompt, InputValidator<Double> validator, String errorMessage) {
        double value;
        do {
            System.out.println(prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                scanner.nextLine();  // Clear the buffer after valid input
                if (validator.isValid(value)) {
                    return value;  // Return the valid input
                } else {
                    System.out.println(errorMessage);
                }
            } else {
                System.out.println(errorMessage);
                scanner.next();  // Clear invalid input
            }
        } while (true);  // Loop until a valid input is entered
    }

    // Validation logic
    private boolean isValidPhoneNumber(String input) {
        return input.matches("\\d+");
    }

    private boolean isValidEmail(String input) {
        return input.contains("@") && input.contains(".com");
    }

    private boolean isPositiveNumber(String input) {
        return input.matches("\\d+");
    }

    // Switch case logic for selecting supply days
    private List<String> selectSupplyDays(Scanner scanner) {
        List<String> supplyDays = new ArrayList<>();
        while (true) {
            System.out.println("Enter Supply Day (type 'done' when finished):");
            String day = scanner.next();
            if (day.equalsIgnoreCase("done")) {
                break;
            }
            day = day.toLowerCase();
            switch (day) {
                case "monday":
                case "tuesday":
                case "wednesday":
                case "thursday":
                case "friday":
                case "saturday":
                case "sunday":
                    if (supplyDays.contains(day)) {
                        System.out.println("Already picked, please choose a different day.");
                    } else {
                        supplyDays.add(day);
                    }
                    break;
                default:
                    System.out.println("Wrong input, please enter a valid day of the week.");
            }
        }
        return supplyDays;
    }

    // Interface for input validation logic
    private interface InputValidator<T> {
        boolean isValid(T input);
    }
}
