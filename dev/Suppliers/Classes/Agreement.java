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
}


