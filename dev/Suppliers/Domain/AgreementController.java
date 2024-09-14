package dev.Suppliers.Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AgreementController {
    private List<Agreement> agreements = new ArrayList<>();

    // Create a new agreement
    public Agreement createAgreement(List<Product> productList, HashMap<String, HashMap<Integer, Double>> discountDetails,
                                     List<String> supplyDays, boolean selfSupply) {
        Agreement agreement = new Agreement(productList, discountDetails, supplyDays, selfSupply);
        agreements.add(agreement);
        System.out.println("Agreement added: " + agreement.getAgreementID());
        return agreement;
    }

    // Update product's discount details within an agreement
    public void updateProductDiscountDetails(Agreement agreement, String productID, HashMap<Integer, Double> newDiscountDetails) {
        HashMap<String, HashMap<Integer, Double>> currentDiscountDetails = agreement.getDiscountDetails();
        currentDiscountDetails.put(productID, newDiscountDetails);
        agreement.setDiscountDetails(currentDiscountDetails);
        System.out.println("Updated discount details for product ID: " + productID);
    }

    // Add a discount to a product in an agreement
// Add a discount to a product in an agreement
    public void addDiscountToProduct(Agreement agreement, String productID, int quantity, double discountPercent) {
        // Step 1: Find the product in the agreement's product list
        Product product = agreement.getProductList().stream()
                .filter(p -> p.getCatalogID().equalsIgnoreCase(productID))
                .findFirst().orElse(null);

        if (product == null) {
            System.out.println("Product not found in the agreement.");
            return;
        }

        // Step 2: Add the discount to the product's own discountDetails field
        HashMap<Integer, Double> productDiscounts = product.getDiscountDetails();
        productDiscounts.put(quantity, discountPercent);

        // Step 3: Add or update the discount in the agreement's discountDetails map
        HashMap<Integer, Double> agreementDiscounts = agreement.getDiscountDetails().get(productID);
        if (agreementDiscounts == null) {
            agreementDiscounts = new HashMap<>();
        }
        agreementDiscounts.put(quantity, discountPercent);

        agreement.getDiscountDetails().put(productID, agreementDiscounts);

        System.out.println("Discount added for product ID: " + productID + ", Quantity: " + quantity + ", Discount: " + discountPercent + "%");
    }


    // Delete a discount from a product in an agreement
// Delete a discount from a product in an agreement
    public void deleteDiscountFromProduct(Agreement agreement, String productID, int quantity) {
        // Step 1: Find the product in the agreement's product list
        Product product = agreement.getProductList().stream()
                .filter(p -> p.getCatalogID().equalsIgnoreCase(productID))
                .findFirst().orElse(null);

        if (product == null) {
            System.out.println("Product not found in the agreement.");
            return;
        }

        // Step 2: Remove the discount from the product's own discountDetails field
        HashMap<Integer, Double> productDiscounts = product.getDiscountDetails();
        if (productDiscounts != null && productDiscounts.containsKey(quantity)) {
            productDiscounts.remove(quantity);
            if (productDiscounts.isEmpty()) {
                System.out.println("All discounts removed from product: " + productID);
            } else {
                System.out.println("Discount for quantity " + quantity + " removed from product: " + productID);
            }
        } else {
            System.out.println("No discount found for the specified quantity on product: " + productID);
            return;
        }

        // Step 3: Remove the discount from the agreement's discountDetails map
        HashMap<Integer, Double> agreementDiscounts = agreement.getDiscountDetails().get(productID);
        if (agreementDiscounts != null) {
            agreementDiscounts.remove(quantity);
            if (agreementDiscounts.isEmpty()) {
                agreement.getDiscountDetails().remove(productID);  // Remove the product from agreement map if no discounts remain
            }
            System.out.println("Discount for quantity " + quantity + " removed from agreement for product ID: " + productID);
        } else {
            System.out.println("No discounts found for product ID in the agreement.");
        }
    }


    // Update agreement details
    public void updateAgreement(int agreementID, List<Product> products, Date agreementDate, HashMap<String, HashMap<Integer, Double>> discountDetails, List<String> supplyDays) {
        Agreement agreement = getAgreement(agreementID);
        if (agreement != null) {
            agreement.setProductList(products);
            agreement.setDiscountDetails(discountDetails);
            agreement.setSupplyDays(supplyDays);
            System.out.println("Agreement updated: " + agreementID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    // Get an agreement by ID
    public Agreement getAgreement(int agreementID) {
        for (Agreement agreement : agreements) {
            if (agreement.getAgreementID() == agreementID) {
                return agreement;
            }
        }
        System.out.println("Agreement not found: " + agreementID);
        return null;
    }

    // Get all agreements
    public List<Agreement> getAllAgreements() {
        return agreements;
    }


    public void deleteAgreement(int agreementID) {
        Agreement agreementToDelete = getAgreement(agreementID);
        if (agreementToDelete != null) {
            agreements.remove(agreementToDelete);
            System.out.println("Agreement deleted: " + agreementID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }


}


