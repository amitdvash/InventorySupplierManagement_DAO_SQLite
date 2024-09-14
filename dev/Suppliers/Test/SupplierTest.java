package dev.Suppliers.Test;

import dev.Suppliers.Domain.Agreement;
import dev.Suppliers.Domain.Product;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        SupplierContact contact = new SupplierContact("Supplier One", "123456", "email@example.com");
        Agreement agreement = new Agreement(new ArrayList<>(), new HashMap<>(), Arrays.asList("Monday"), true);
        supplier = new Supplier("123", "001", PaymentMethod.CreditCard, agreement, contact);  // Updated constructor with agreement
    }

    @Test
    void testGetCompanyID() {
        assertEquals("123", supplier.getCompanyID());
    }

    @Test
    void testSetCompanyID() {
        supplier.setCompanyID("456");
        assertEquals("456", supplier.getCompanyID());
    }

    @Test
    void testGetBankAccount() {
        assertEquals("001", supplier.getBankAccount());
    }

    @Test
    void testSetBankAccount() {
        supplier.setBankAccount("789");
        assertEquals("789", supplier.getBankAccount());
    }

    @Test
    void testGetPaymentMethod() {
        assertEquals(PaymentMethod.CreditCard, supplier.getPaymentMethod());
    }

    @Test
    void testSetPaymentMethod() {
        supplier.setPaymentMethod(PaymentMethod.Cash);
        assertEquals(PaymentMethod.Cash, supplier.getPaymentMethod());
    }

    @Test
    void testGetContactName() {
        assertEquals("Supplier One", supplier.getContact().getName());
    }

    @Test
    void testSetContactInfo() {
        SupplierContact newContact = new SupplierContact("Supplier Two", "987654", "newemail@example.com");
        supplier.setContact(newContact);
        assertEquals("Supplier Two", supplier.getContact().getName());
        assertEquals("987654", supplier.getContact().getPhoneNumber());
        assertEquals("newemail@example.com", supplier.getContact().getEmail());
    }

    @Test
    void testSetAndGetAgreement() {
        Agreement agreement = new Agreement(new ArrayList<>(), new HashMap<>(), Arrays.asList("Tuesday"), false);
        supplier.setSupplierAgreement(agreement);
        assertEquals(agreement, supplier.getSupplierAgreement());
    }

    @Test
    void testAddProductToAgreement() {
        Product product = new Product("Catalog1",  new HashMap<>(), 10.0, 30, 2.0, null);
        supplier.getSupplierAgreement().addProduct(product);
        assertTrue(supplier.getSupplierAgreement().getProductList().contains(product));
    }
}
