package dev.Suppliers.Domain;

import dev.Suppliers.Enums.PaymentMethod;

public class Supplier {
    private int supplierID; // Changed to integer to match the database
    private String companyID;
    private String bankAccount;
    private PaymentMethod paymentMethod;
    private Agreement supplierAgreement; // This can be handled separately in DTO
    private SupplierContact contact; // This can be handled separately in DTO

    // Constructor
    public Supplier(int supplierID, String companyID, String bankAccount, PaymentMethod paymentMethod, Agreement supplierAgreement, SupplierContact contact) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.supplierAgreement = supplierAgreement; // Set the Agreement object if available
        this.contact = contact; // Set the SupplierContact object if available
    }

    // Getters and Setters
    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
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
        System.out.println("Supplier ID: " + supplierID); // Now just printing the integer ID
        System.out.println("Company ID: " + companyID);
        System.out.println("Bank Account: " + bankAccount);
        System.out.println("Payment Method: " + paymentMethod);

        // Print Contact Information
        if (contact != null) {
            contact.printContactDetails();
        } else {
            System.out.println("  No contact information available.");
        }

        // Print Agreement Details
        System.out.println("Supplier's Agreement Details:");
        if (supplierAgreement != null) {
            supplierAgreement.printAgreementDetails();
        } else {
            System.out.println("  No agreement available.");
        }
        System.out.println("----------------------------");
    }
}
