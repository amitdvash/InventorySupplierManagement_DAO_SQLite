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

    public void OpenNewSupplierCard() {
        Scanner scanner = new Scanner(System.in);

        // 1. Ask for Supplier details with validation
        String name = getValidatedInput(scanner, "Enter Supplier's Name:", input -> !input.isEmpty(), "Name cannot be empty");

        String phoneNumber = getValidatedInput(scanner, "Enter Supplier's phone number:", this::isValidPhoneNumber, "Phone number must contain only positive numbers");

        String email = getValidatedInput(scanner, "Enter Supplier's email:", this::isValidEmail, "Email must contain at least one '@' character");

        String companyID = getValidatedInput(scanner, "Enter Supplier's company ID:", this::isPositiveNumber, "Company ID must contain only positive numbers");

        String bankAccount = getValidatedInput(scanner, "Enter Supplier's bank account:", this::isPositiveNumber, "Bank account must contain only positive numbers");

        int paymentMethodChoice = getValidatedInt(scanner, "Choose Supplier's Payment Method (1 for CreditCard, 2 for Cash, 3 for BankTransfer):", 1, 3);

        PaymentMethod paymentMethod = getPaymentMethod(paymentMethodChoice);

        // 2. Create Products
        List<Product> productList = new ArrayList<>();
        HashMap<String, HashMap<Integer, Double>> agreementDiscounts = new HashMap<>();
        String addMoreProducts;
        do {
            scanner.nextLine();  // Consume newline

            String productName = getValidatedInput(scanner, "Enter Product's name:", input -> !input.isEmpty(), "Product's name cannot be empty");

            double price = getValidatedDouble(scanner, "Enter Product's price:", input -> input > 0, "Price must be positive");

            int expirationDays = getValidatedInt(scanner, "Enter Product's expiration days:", 1, Integer.MAX_VALUE, "Expiration days must be a positive number (cannot start with 0)");

            double weight = getValidatedDouble(scanner, "Enter Product's weight:", input -> input > 0, "Weight must be positive (cannot be 0)");

            // Ask if there are discount details for the product
            HashMap<Integer, Double> discountDetails = new HashMap<>();
            String hasDiscountDetails = getValidatedInput(scanner, "Are there any discount details for the product? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'");

            if (hasDiscountDetails.equalsIgnoreCase("yes")) {
                while (true) {
                    int quantity = getValidatedInt(scanner, "Enter the minimum quantity for the discount:", 1, Integer.MAX_VALUE, "Quantity must be a positive number");

                    double discountPercent = getValidatedDouble(scanner, "Enter the discount percent (e.g., 10 for 10%):", input -> input > 0, "Discount percent must be positive");

                    discountDetails.put(quantity, discountPercent);

                    String addMoreDiscounts = getValidatedInput(scanner, "Do you want to add another discount detail? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'");

                    if (addMoreDiscounts.equalsIgnoreCase("no")) {
                        break;
                    }
                }
            }

            // Create Product (Agreement is null for now, to be set later)
            Product product = productController.addProduct(productName, discountDetails, price, expirationDays, weight, null);
            productList.add(product);

            // Add product's discount details to the agreement's discount map
            agreementDiscounts.put(product.getCatalogID(), discountDetails);

            addMoreProducts = getValidatedInput(scanner, "Do you want to add more products? (yes/no)", input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'");
        } while (addMoreProducts.equalsIgnoreCase("yes"));

        // 3. Supply Days selection using switch case
        List<String> supplyDays = selectSupplyDays(scanner);

        // 4. Create Agreement
        boolean selfSupply = Boolean.parseBoolean(getValidatedInput(scanner, "Is the supplier responsible for supplying products? (true/false):", input -> input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"), "Please enter 'true' or 'false'"));

        Agreement agreement = agreementController.addAgreement(productList, agreementDiscounts, supplyDays, selfSupply);

        // 5. Create Supplier using the agreement
        Supplier supplier = supplierController.addSupplier(companyID, bankAccount, paymentMethod, productList, agreement, name, phoneNumber, email);

        // Associate Supplier with the Agreement and Products
        agreement.setSupplier(supplier);  // Link supplier to agreement
        for (Product product : productList) {
            product.setAgreement(agreement);  // Link agreement to products
        }

        System.out.println("Supplier successfully created with the following details:");
        supplier.printSupplierDetails();
    }

    // Utility methods for input validation
    private String getValidatedInput(Scanner scanner, String prompt, InputValidator<String> validator, String errorMessage) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine();
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
            while (!scanner.hasNextInt()) {
                System.out.println(errorMessage);
                scanner.next(); // clear invalid input
            }
            value = scanner.nextInt();
            if (value < minValue || value > maxValue) {
                System.out.println(errorMessage);
            }
        } while (value < minValue || value > maxValue);
        return value;
    }

    private double getValidatedDouble(Scanner scanner, String prompt, InputValidator<Double> validator, String errorMessage) {
        double value;
        do {
            System.out.println(prompt);
            while (!scanner.hasNextDouble()) {
                System.out.println(errorMessage);
                scanner.next(); // clear invalid input
            }
            value = scanner.nextDouble();
            if (!validator.isValid(value)) {
                System.out.println(errorMessage);
            }
        } while (!validator.isValid(value));
        return value;
    }

    // Validation logic
    private boolean isValidPhoneNumber(String input) {
        return input.matches("\\d+");
    }

    private boolean isValidEmail(String input) {
        return input.contains("@");
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

            switch (day.toLowerCase()) {
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

    // Payment Method selection
    private PaymentMethod getPaymentMethod(int choice) {
        switch (choice) {
            case 1:
                return PaymentMethod.CreditCard;
            case 2:
                return PaymentMethod.Cash;
            case 3:
                return PaymentMethod.BankTransfer;
            default:
                throw new IllegalArgumentException("Invalid Payment Method Choice");
        }
    }

    // Interface for input validation logic
    private interface InputValidator<T> {
        boolean isValid(T input);
    }
}
