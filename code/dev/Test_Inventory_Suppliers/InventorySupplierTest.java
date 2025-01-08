package dev.Test_Inventory_Suppliers;

import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InventorySupplierTest {

    private Supplier supplier;
    private Product product;
    private Item item;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize supplier and product
        SupplierContact contact = new SupplierContact("Supplier One", "123456", "email@example.com");
        supplier = new Supplier(123, "001", "001", PaymentMethod.CreditCard, null, contact);

        // Initialize product and item
        product = new Product("Test1", "Category1", "SubCategory1", 5.0, 2, null);
        item = new Item("Test1", 10.0, 15.0, "Manufacturer", "Category1", "SubCategory1", 5.0, null, E_Item_Status.Available, E_Item_Place.Store);
    }

    // Test 1: Test getting company ID (Supplier related)
    @Test
    void testGetCompanyID() {
        assertEquals("001", supplier.getCompanyID());
    }

    // Test 2: Test setting and getting company ID (Supplier related)
    @Test
    void testSetCompanyID() {
        supplier.setCompanyID("002");
        assertEquals("002", supplier.getCompanyID());
    }

    // Test 3: Test adding an item to a product (Inventory related)
    @Test
    void testAddItemToProduct() throws SQLException {
        product.addItem(item);
        assertEquals(1, product.getQuantity_in_store() + product.getQuantity_in_warehouse(), "Item should be added to the product.");
    }

    // Test 4: Test removing an item from a product (Inventory related)
    @Test
    void testRemoveItemFromProduct() throws SQLException {
        product.addItem(item);
        product.removeItem(item);
        assertEquals(0, product.getQuantity_in_store() + product.getQuantity_in_warehouse(), "Item should be removed from the product.");
    }

    // Test 5: Test setting and getting supplier contact information (Supplier related)
    @Test
    void testSetAndGetSupplierContact() {
        SupplierContact newContact = new SupplierContact("Supplier Two", "987654", "newemail@example.com");
        supplier.setContact(newContact);
        assertEquals("Supplier Two", supplier.getContact().getName());
        assertEquals("987654", supplier.getContact().getPhoneNumber());
        assertEquals("newemail@example.com", supplier.getContact().getEmail());
    }

    // Test 6: Test applying discount to an item (Inventory related)
    @Test
    void testApplyDiscountToItem() {
        item.applyDiscount(10);
        assertEquals(13.5, item.getPriceAfterDiscount(), 0.01, "Discount should be applied to the item.");
    }

    // Test 7: Test updating contact phone number (Supplier related)
    @Test
    void testUpdateSupplierContactPhoneNumber() {
        supplier.getContact().setPhoneNumber("987654321");
        assertEquals("987654321", supplier.getContact().getPhoneNumber());
    }

    // Test 8: Test updating supplier's payment method (Supplier related)
    @Test
    void testUpdatePaymentMethod() {
        supplier.setPaymentMethod(PaymentMethod.Cash);
        assertEquals(PaymentMethod.Cash, supplier.getPaymentMethod());
    }

    @Test
    void testUpdateItemStatusAfterExpiry() {
        // Set the expiry date to 30 days in the past (i.e., the item should be expired)
        item.setExpiryDate(LocalDate.now().minusDays(30));
        item.updateStatus();

        // The status should be updated to EXPIRED
        assertEquals(E_Item_Status.EXPIRED, item.getStatus(), "Item status should be 'EXPIRED' when the expiry date is in the past.");
    }


    // Test 10: Test updating supplier's bank account (Supplier related)
    @Test
    void testUpdateSupplierBankAccount() {
        supplier.setBankAccount("789456");
        assertEquals("789456", supplier.getBankAccount());
    }
}
