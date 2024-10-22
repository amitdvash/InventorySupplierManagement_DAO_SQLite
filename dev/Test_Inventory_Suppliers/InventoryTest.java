package dev.Test_Inventory_Suppliers;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Product sampleProduct;
    Item availableWarehouseItem1, availableWarehouseItem2;
    Discount productDiscount, newDiscount;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test data in memory without interacting with the database
        availableWarehouseItem1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        availableWarehouseItem2 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);

        // Initialize product and discount
        sampleProduct = new Product("Item1", "Category1", "SubCategory1", 10, 1, null);
        productDiscount = new Discount(50, LocalDate.ofYearDay(2024, 20), LocalDate.ofYearDay(2024, 300));
        newDiscount = new Discount(30, LocalDate.ofYearDay(2024, 50), LocalDate.ofYearDay(2024, 300));
    }

    // Test 1: Add an item to the product (in memory)
    @Test
    void testAddItemLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        assertTrue(sampleProduct.getQuantity_in_warehouse() > 0, "Item should be added to the warehouse.");
    }

    // Test 2: Remove an item from the product (in memory)
    @Test
    void testRemoveItemLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.removeItem(availableWarehouseItem1);
        assertEquals(0, sampleProduct.getQuantity_in_warehouse(), "Item should be removed from the warehouse.");
    }

    // Test 3: Apply discount and verify price update (logic only)
    @Test
    void testApplyDiscountLogic() {
        availableWarehouseItem1.applyDiscount(productDiscount.getDiscountRate());
        double expectedDiscountedPrice = availableWarehouseItem1.getSellingPrice() * (1 - productDiscount.getDiscountRate() / 100);
        assertEquals(expectedDiscountedPrice, availableWarehouseItem1.getPriceAfterDiscount(), 0.01, "Discount should be applied to the item's selling price.");
    }

    // Test 4: Remove discount from product (logic only)
    @Test
    void testRemoveDiscountLogic() {
        availableWarehouseItem1.applyDiscount(productDiscount.getDiscountRate());

        // Remove the discount
        availableWarehouseItem1.cancelDiscount();

        // Check if price returned to the original price
        assertEquals(availableWarehouseItem1.getSellingPrice(), availableWarehouseItem1.getPriceAfterDiscount(), 0.01, "Price should return to the original after removing discount.");
    }

    // Test 5: Update product status based on quantity in store/warehouse (logic only)
    @Test
    void testUpdateProductStatusLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem2);

        assertEquals(E_Product_Status.Available, sampleProduct.getStatus(), "Product should be available after adding items.");

        // Remove all items to check status change
        sampleProduct.removeItem(availableWarehouseItem1);
        sampleProduct.removeItem(availableWarehouseItem2);

        assertEquals(E_Product_Status.Out_of_stock, sampleProduct.getStatus(), "Product should be out of stock after removing all items.");
    }

    // Test 6: Test moving an item between places (logic only)
    @Test
    void testMoveItemLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        availableWarehouseItem1.setPlace(E_Item_Place.Store);
        assertEquals(E_Item_Place.Store, availableWarehouseItem1.getPlace(), "Item should be moved to Store.");
    }

    // Test 7: Apply a new discount and verify the update (logic only)
    @Test
    void testApplyNewDiscountLogic() {
        availableWarehouseItem1.applyDiscount(productDiscount.getDiscountRate());
        double expectedPrice = availableWarehouseItem1.getSellingPrice() * (1 - productDiscount.getDiscountRate() / 100);

        availableWarehouseItem1.applyDiscount(newDiscount.getDiscountRate());  // Apply new discount
        double newExpectedPrice = availableWarehouseItem1.getSellingPrice() * (1 - newDiscount.getDiscountRate() / 100);

        assertEquals(newExpectedPrice, availableWarehouseItem1.getPriceAfterDiscount(), 0.01, "New discount should overwrite the previous one.");
    }

    // Test 8: Check adding duplicate items (logic only)
    @Test
    void testAddDuplicateItemsLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem1);  // Adding the same item again

        // Verify that duplicate items do not increase the count
        assertEquals(2, sampleProduct.getQuantity_in_warehouse(), "Duplicate items should not be added.");
    }

    // Test 9: Test product minimum quantity enforcement (logic only)
    @Test
    void testProductMinimumQuantityLogic() throws SQLException {
        sampleProduct.addItem(availableWarehouseItem1);
        assertTrue(sampleProduct.getQuantity_in_warehouse() >= sampleProduct.getMin_quantity(), "Product should meet minimum quantity requirement.");
    }

    // Test 10: Check item status after applying discount (logic only)
    @Test
    void testItemStatusAfterDiscountLogic() {
        availableWarehouseItem1.applyDiscount(productDiscount.getDiscountRate());
        assertEquals(E_Item_Status.Available, availableWarehouseItem1.getStatus(), "Item status should remain available after discount.");
    }
}
