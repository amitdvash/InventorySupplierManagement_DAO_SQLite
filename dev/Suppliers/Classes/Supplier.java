package dev.Suppliers.Classes;

import java.util.List;

public class Supplier {
    private String supplierID;
    private String companyID;
    private String bankAccount;
    private String paymentMethod;
    private List<Product> productList;
    private Agreement supplierAgreement;
    private SupplierContact contact; // New field for SupplierContact

    public Supplier(String supplierID, String companyID, String bankAccount, String paymentMethod, List<Product> productList, Agreement supplierAgreement, SupplierContact contact) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.productList = productList;
        this.supplierAgreement = supplierAgreement;
        this.contact = contact; // Assign contact during initialization
    }

    // Getters and Setters
    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }

    public String getCompanyID() { return companyID; }
    public void setCompanyID(String companyID) { this.companyID = companyID; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<Product> getProductList() { return productList; }
    public void setProductList(List<Product> productList) { this.productList = productList; }

    public Agreement getSupplierAgreement() { return supplierAgreement; }
    public void setSupplierAgreement(Agreement supplierAgreement) { this.supplierAgreement = supplierAgreement; }

    public SupplierContact getContact() { return contact; } // Getter for contact
    public void setContact(SupplierContact contact) { this.contact = contact; } // Setter for contact
}
