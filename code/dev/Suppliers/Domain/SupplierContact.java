package dev.Suppliers.Domain;

public class SupplierContact {
    private String name;
    private String phoneNumber;
    private String email;

    public SupplierContact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email=email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public void printContactDetails() {
        //System.out.println("----- Contact Details -----");
        System.out.println("Contact Name: " + this.getName());
        System.out.println("Contact Email: " + this.getEmail());
        System.out.println("Contact Phone: " + this.getPhoneNumber());
        System.out.println("----------------------------");
    }
}
