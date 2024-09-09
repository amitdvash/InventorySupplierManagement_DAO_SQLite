package dev.Suppliers.Classes;

import java.util.List;

public class Supplier {
    private String supplierID;
    private String companyID;
    private String bankAccount;
    private String paymentMethod;
    private List<Product> productList;
    private Agreement supplierAgreement;

    public Supplier(String supplierID, String companyID, String bankAccount, String paymentMethod, List<Product> productList, Agreement supplierAgreement) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.productList = productList;
        this.supplierAgreement = supplierAgreement;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setSupplierAgreement(Agreement supplierAgreement) {
        this.supplierAgreement = supplierAgreement;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Agreement getSupplierAgreement() {
        return supplierAgreement;
    }


}

