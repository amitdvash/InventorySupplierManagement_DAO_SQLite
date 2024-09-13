package dev.Suppliers.Domain;

import dev.Suppliers.Enums.PaymentMethod;

public class Supplier {
    private static int runningIndex = 1;  // Static running index
    private String supplierID;
    private String companyID;
    private String bankAccount;
    private PaymentMethod paymentMethod;
    //private List<Product> productList;
    private Agreement supplierAgreement;
    private SupplierContact contact;

    // Constructor - ID is auto-assigned
    public Supplier(String companyID, String bankAccount, PaymentMethod paymentMethod    /* , List<Product> productList */, Agreement supplierAgreement, SupplierContact contact) {
        this.supplierID = "S" + runningIndex++;  // Auto-generate supplierID
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        //this.productList = productList;
        this.supplierAgreement = supplierAgreement;
        this.contact = contact;

    }

    // Getters and Setters
    public String getSupplierID() {
        return supplierID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

//    public List<Product> getProductList() {
//        return productList;
//    }
//
//    public void setProductList(List<Product> productList) {
//        this.productList = productList;
//    }
//    public void addProduct(Product product){
//        productList.add(product);
//    }

    public Agreement getSupplierAgreement() {
        return supplierAgreement;
    }

    public void setSupplierAgreement(Agreement supplierAgreement) {
        this.supplierAgreement = supplierAgreement;
    }

    public SupplierContact getContact() {
        return contact;
    }

    public void setContact(SupplierContact contact) {
        this.contact = contact;
    }

    public void printSupplierDetails() {
        System.out.println("----- Supplier Details -----");
        //System.out.println("Contact Information:");
        if (contact != null) {
            contact.printContactDetails();
        } else {
            System.out.println("  No contact information available.");
        }
        System.out.println("Supplier ID: " + supplierID);
        System.out.println("Company ID: " + companyID);
        System.out.println("Bank Account: " + bankAccount);
        System.out.println("Payment Method: " + paymentMethod);

//        System.out.println("Products supplied by this supplier:");
//        if (productList != null && !productList.isEmpty()) {
//            for (Product product : productList) {
//                System.out.println("  - Product Name: " + product.getName() + ", Catalog ID: " + product.getCatalogID() + ", Price: " + product.getPrice());
//            }
//        } else {
//            System.out.println("  No products available.");
//        }

        System.out.println("Supplier's Agreement Details:");
        if (supplierAgreement != null) {
            supplierAgreement.printAgreementDetails();
        } else {
            System.out.println("  No agreement available.");
        }


        System.out.println("----------------------------");
    }
}
