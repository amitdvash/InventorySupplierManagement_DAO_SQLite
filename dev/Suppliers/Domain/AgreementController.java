package dev.Suppliers.Domain;


import dev.Suppliers.DataBase.AgreementDTO;
import dev.Suppliers.DataBase.ProductDTO;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AgreementController {
    private AgreementDTO agreementDTO;
    private ProductDTO productDTO;

    public AgreementController(Connection connection) {
        this.agreementDTO = new AgreementDTO(connection);
        this.productDTO = new ProductDTO(connection);

    }

    public Agreement createAgreement(List<Product> productList, List<String> supplyDays, boolean selfSupply, int supplierID) {
        Agreement agreement = new Agreement(supplierID, productList, supplyDays, selfSupply);
        int agreementID = agreementDTO.create(agreement); // Create the agreement in the database and get the generated agreementID
        if (agreementID > 0) {
            agreement.setAgreementID(agreementID); // Set the agreementID in the Agreement object
            // Associate the products with the agreement in the database
            for (Product product : productList) {
                product.setAgreement(agreement);
                productDTO.create(product); // Insert product into database with agreementID
            }
        }
        return agreement;
    }

    // Update product's discount details within an agreement
    public void updateProductDiscountDetails(int agreementID, String productID, HashMap<Integer, Double> newDiscountDetails) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            agreementDTO.updateDiscountDetails(agreement, productID, newDiscountDetails);
            System.out.println("Updated discount details for product ID: " + productID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    // Add a discount to a product in an agreement
    public void addDiscountToProduct(int agreementID, String productID, int quantity, double discountPercent) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            agreementDTO.addDiscount(agreement, productID, quantity, discountPercent);
            System.out.println("Discount added for product ID: " + productID + ", Quantity: " + quantity + ", Discount: " + discountPercent + "%");
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    // Delete a discount from a product in an agreement
    public void deleteDiscountFromProduct(int agreementID, String productID, int quantity) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            agreementDTO.removeDiscount(agreement, productID, quantity);
            System.out.println("Discount removed for product ID: " + productID + ", Quantity: " + quantity);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    public void updateAgreement(int agreementID, List<Product> products, HashMap<String, HashMap<Integer, Double>> discountDetails, List<String> supplyDays) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            agreement.setProductList(products); // Update the list of products
            agreement.setSupplyDays(supplyDays); // Update the supply days

            // Update the discount details for each product in the agreement
            for (Product product : agreement.getProductList()) {
                HashMap<Integer, Double> newDiscountDetails = discountDetails.get(product.getCatalogID());
                if (newDiscountDetails != null) {
                    product.setDiscountDetails(newDiscountDetails); // Update the discount details for the product
                }
            }
            agreementDTO.update(agreement); // Update in the database
            System.out.println("Agreement updated: " + agreementID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    // Get an agreement by ID from the database
    public Agreement getAgreement(int agreementID) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            return agreement;
        } else {
            System.out.println("Agreement not found: " + agreementID);
            return null;
        }
    }

    // Get all agreements from the database
    public List<Agreement> getAllAgreements() {
        return agreementDTO.readAll(); // Get all agreements from the database
    }

    // Delete an agreement from the database
    public void deleteAgreement(int agreementID) {
        Agreement agreement = agreementDTO.read(agreementID);
        if (agreement != null) {
            agreementDTO.delete(agreementID); // Delete from the database
            System.out.println("Agreement deleted: " + agreementID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }
}
