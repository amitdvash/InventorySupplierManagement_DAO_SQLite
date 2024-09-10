package dev.Suppliers.Classes;

import java.util.List;
import java.util.Date;
import java.util.Map;

public class Agreement {
    private int agreementID;
    private List<Product> productList;
    private Date agreementDate;
    private Map<String, Map<Integer,Double>> discountDetails;
    private String supplyDays;

    public Agreement(int agreementID, List<Product> productList, Date agreementDate, Map<String, Map<Integer, Double>> discountDetails, String supplyDays) {
        this.agreementID = agreementID;
        this.productList = productList;
        this.agreementDate = agreementDate;
        this.discountDetails = discountDetails;
        this.supplyDays = supplyDays;
    }

    // Setters

    public void setAgreementID(int agreementID) {
        this.agreementID = agreementID;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setAgreementDate(Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public void setDiscountDetails(Map<String, Map<Integer, Double>> discountDetails) {
        this.discountDetails = discountDetails;
    }

    public void setSupplyDays(String supplyDays) {
        this.supplyDays = supplyDays;
    }

    // Getters
    public int getAgreementID() {
        return agreementID;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public Map<String, Map<Integer, Double>> getDiscountDetails() {
        return discountDetails;
    }

    public String getSupplyDays() {
        return supplyDays;
    }


    // Search for a product's catalog number in the discount details
    public Map<Integer, Double> findDiscountDetails(String catalogNumber) {
        if (discountDetails.containsKey(catalogNumber)) {
            return discountDetails.get(catalogNumber); // return discount conditions for the catalog number
        } else {
            return null; // product not found
        }
    }

    // Add a new catalog number with discount conditions to the discount details
    public void addDiscountDetails(String catalogNumber, Map<Integer, Double> discountConditions) {
        if (!discountDetails.containsKey(catalogNumber)) {
            discountDetails.put(catalogNumber, discountConditions);
        } else {
            System.out.println("The product is already listed in the discount document.");
        }
    }



}


