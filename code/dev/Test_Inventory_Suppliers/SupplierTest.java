package dev.Test_Inventory_Suppliers;

import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        SupplierContact contact = new SupplierContact("Supplier One", "123456", "email@example.com");
        supplier = new Supplier(123, "001", "001", PaymentMethod.CreditCard, null, contact);
    }

    // Test 1: Test getting company ID
    @Test
    void testGetCompanyID() {
        assertEquals("001", supplier.getCompanyID());
    }

    // Test 2: Test setting and getting company ID
    @Test
    void testSetCompanyID() {
        supplier.setCompanyID("002");
        assertEquals("002", supplier.getCompanyID());
    }

    // Test 3: Test getting bank account
    @Test
    void testGetBankAccount() {
        assertEquals("001", supplier.getBankAccount());
    }

    // Test 4: Test setting and getting bank account
    @Test
    void testSetBankAccount() {
        supplier.setBankAccount("789");
        assertEquals("789", supplier.getBankAccount());
    }

    // Test 5: Test getting payment method
    @Test
    void testGetPaymentMethod() {
        assertEquals(PaymentMethod.CreditCard, supplier.getPaymentMethod());
    }

    // Test 6: Test setting and getting payment method
    @Test
    void testSetPaymentMethod() {
        supplier.setPaymentMethod(PaymentMethod.Cash);
        assertEquals(PaymentMethod.Cash, supplier.getPaymentMethod());
    }

    // Test 7: Test getting contact name
    @Test
    void testGetContactName() {
        assertEquals("Supplier One", supplier.getContact().getName());
    }

    // Test 8: Test setting and getting contact info
    @Test
    void testSetContactInfo() {
        SupplierContact newContact = new SupplierContact("Supplier Two", "987654", "newemail@example.com");
        supplier.setContact(newContact);
        assertEquals("Supplier Two", supplier.getContact().getName());
        assertEquals("987654", supplier.getContact().getPhoneNumber());
        assertEquals("newemail@example.com", supplier.getContact().getEmail());
    }

    // Test 9: Test updating contact phone number
    @Test
    void testUpdateContactPhoneNumber() {
        supplier.getContact().setPhoneNumber("987654321");
        assertEquals("987654321", supplier.getContact().getPhoneNumber());
    }

    // Test 10: Test updating supplier's contact email
    @Test
    void testUpdateContactEmail() {
        supplier.getContact().setEmail("updatedemail@example.com");
        assertEquals("updatedemail@example.com", supplier.getContact().getEmail());
    }
}
