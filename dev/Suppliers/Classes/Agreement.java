package dev.Suppliers.Classes;

import java.util.HashMap;
import java.util.List;
import java.util.Date;

public class Agreement {
    private int agreementID;
    private List<Product> productList;
    private Date agreementDate;
    private HashMap<String, HashMap<Integer, Double>> discountDetails;
    private String supplyDays;
    private boolean selfSupply;
    private String paymentType;

    public Agreement(int agreementID, List<Product> productList, Date agreementDate, HashMap<String, HashMap<Integer, Double>> discountDetails, String supplyDays, boolean selfSupply, String paymentType) {
        this.agreementID = agreementID;
        this.productList = productList;
        this.agreementDate = agreementDate;
        this.discountDetails = discountDetails;
        this.supplyDays = supplyDays;
        this.selfSupply = selfSupply;
        this.paymentType = paymentType;
    }

    // Getters and Setters
    public int getAgreementID() { return agreementID; }
    public void setAgreementID(int agreementID) { this.agreementID = agreementID; }

    public List<Product> getProductList() { return productList; }
    public void setProductList(List<Product> productList) { this.productList = productList; }

    public Date getAgreementDate() { return agreementDate; }
    public void setAgreementDate(Date agreementDate) { this.agreementDate = agreementDate; }

    public HashMap<String, HashMap<Integer, Double>> getDiscountDetails() { return discountDetails; }
    public void setDiscountDetails(HashMap<String, HashMap<Integer, Double>> discountDetails) { this.discountDetails = discountDetails; }

    public String getSupplyDays() { return supplyDays; }
    public void setSupplyDays(String supplyDays) { this.supplyDays = supplyDays; }

    public boolean isSelfSupply() { return selfSupply; }
    public void setSelfSupply(boolean selfSupply) { this.selfSupply = selfSupply; }

    public String getPaymentType() { return this.paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    // Find discount details for a specific product by catalogID
    public HashMap<Integer, Double> findDiscountDetails(String catalogID) {
        return discountDetails.getOrDefault(catalogID, null);
    }



    // Add new discount details for a product by catalogID
    public void addDiscountDetails(String catalogID, HashMap<Integer, Double> newDiscountDetails) {
        if (!discountDetails.containsKey(catalogID)) {
            discountDetails.put(catalogID, newDiscountDetails);
            System.out.println("Discount added for catalogID: " + catalogID);
        } else {
            System.out.println("Discount for product already exists in agreement.");
        }
    }

    // Update discount details when changed in Product (Synchronization)
    public void updateDiscountDetails(String catalogID, HashMap<Integer, Double> discountConditions) {
        discountDetails.put(catalogID, discountConditions);
        System.out.println("Discounts synchronized in agreement for catalogID: " + catalogID);
    }
}
