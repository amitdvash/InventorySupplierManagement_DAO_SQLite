package dev.Suppliers.Domain;

import dev.Suppliers.Domain.Exception.ExitException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class InputValidator {

    private final Scanner scanner = new Scanner(System.in);

    // Method to validate generic string input
    public String getValidatedInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        // Exit condition handling
        if (input.equalsIgnoreCase("exit")) {
            throw new ExitException("User requested exit.");
        }

        return input;
    }

    // Overloaded method to validate input with a custom validator
    public String getValidatedInput(String prompt, Predicate<String> validator, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            // Exit condition handling
            if (input.equalsIgnoreCase("exit")) {
                throw new ExitException("User requested exit.");
            }

            if (validator.test(input)) {
                return input;
            } else {
                System.out.println(errorMessage);
            }
        } while (true);
    }

    // Method to validate integers with a range
    public int getValidatedInt(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Exit condition handling
            if (input.equalsIgnoreCase("exit")) {
                throw new ExitException("User requested exit.");
            }

            try {
                value = Integer.parseInt(input);
                if (value >= 0) { // Positive numbers only
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a valid positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Method to validate double input for discount percentage or other cases
    public double getValidatedDouble(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Exit condition handling
            if (input.equalsIgnoreCase("exit")) {
                throw new ExitException("User requested exit.");
            }

            try {
                value = Double.parseDouble(input);
                if (value > 0) { // Positive numbers only
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    // Method to validate percentage input (e.g., for discount)
    public double getValidatedDiscountPercentage(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Exit condition handling
            if (input.equalsIgnoreCase("exit")) {
                throw new ExitException("User requested exit.");
            }

            try {
                value = Double.parseDouble(input);
                if (value >= 1 && value <= 100) { // Valid discount percentage range
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a percentage between 1 and 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid percentage (between 1 and 100).");
            }
        }
    }

    // Validate Supplier ID (must start with 'S' and followed by numbers)
    public boolean isValidSupplierID(String supplierID) {
        return supplierID.matches("S\\d+");
    }

    // Validate phone number (digits only)
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d+");  // Digits only
    }

    // Validate email format (basic)
    public boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".com") && email.length() > 5;  // Basic email validation
    }

    // Validate Company ID and Bank Account (digits only, no negative values)
    public boolean isValidCompanyOrBankAccount(String input) {
        return input.matches("\\d+") && !input.startsWith("-");  // Digits only, no negative
    }

    // Validate day of the week (monday, tuesday, etc.)
    public boolean isValidDayOfWeek(String day) {
        List<String> validDays = Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
        return validDays.contains(day.toLowerCase());
    }

    // Yes/No Input Validator
    public String getValidatedYesNoInput(String prompt) {
        return getValidatedInput(prompt, input -> input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"), "Please enter 'yes' or 'no'.");
    }
}
