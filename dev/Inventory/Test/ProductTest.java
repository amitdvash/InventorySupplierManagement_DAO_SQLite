//package dev.Inventory.Test;
//
//import dev.Inventory.Classes.Discount;
//import dev.Inventory.Classes.Item;
//import dev.Inventory.Classes.Product;
//import dev.Inventory.Enums.E_Item_Place;
//import dev.Inventory.Enums.E_Item_Status;
//import dev.Inventory.Enums.E_Product_Status;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ProductTest {
//
//    Product sampleProduct;
//    Item expiringWarehouseItem, availableWarehouseItem1, availableWarehouseItem2, availableWarehouseItem3, availableWarehouseItem4, differentProductItem;
//    Discount productDiscount, newDiscount;
//
//    @BeforeEach
//    void setUp() throws SQLException {
//        // Initializing items for the product
//        expiringWarehouseItem = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.about_to_expire, E_Item_Place.Warehouse);
//        availableWarehouseItem1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
//        availableWarehouseItem2 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
//        availableWarehouseItem3 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
//        availableWarehouseItem4 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
//        differentProductItem = new Item("Item2", 20, 30, "Manufacturer2", "Category2", "SubCategory2", 10, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Warehouse);
//
//        // Initializing the product and discount
//        sampleProduct = new Product("Item1", "Category1", "SubCategory1", 10, 2, null);
//        productDiscount = new Discount(50, LocalDate.ofYearDay(2024, 20), LocalDate.ofYearDay(2024, 300));
//        newDiscount = new Discount(30, LocalDate.ofYearDay(2024, 50), LocalDate.ofYearDay(2024, 300));
//    }
//
//    // Test Case 1: Adding an Item to Product
//    @Test
//    void testAddItem() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        assertTrue(sampleProduct.getItems().containsKey(availableWarehouseItem1.getId()), "Item should be added.");
//    }
//
//    // Test Case 2: Removing an Item from Product
//    @Test
//    void testRemoveItem() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.removeItem(availableWarehouseItem1);
//        assertFalse(sampleProduct.getItems().containsKey(availableWarehouseItem1.getId()), "Item should be removed.");
//    }
//
//    // Test Case 3: Checking Quantity in Warehouse
//    @Test
//    void testTotalQuantityInWarehouse() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem2);
//        assertEquals(2, sampleProduct.getQuantity_in_warehouse(), "Total quantity in warehouse should be 2.");
//    }
//
//    // Test Case 4: Checking Quantity in Store
//    @Test
//    void testTotalQuantityInStore() {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.moveItemTo(availableWarehouseItem1, E_Item_Place.Store);
//        assertEquals(1, sampleProduct.getQuantity_in_store(), "Total quantity in store should be 1.");
//    }
//
//    // Test Case 5: Applying Discount and Verifying Price Update
//    @Test
//    void testApplyDiscountToProduct() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.setDiscount(productDiscount);
//        double expectedPrice = availableWarehouseItem1.getSelling_price() * (1 - productDiscount.getDiscountRate() / 100);
//        assertEquals(expectedPrice, availableWarehouseItem1.getPrice_after_discount(), 0.01, "Discount should be applied.");
//    }
//
//    // Test Case 6: Removing Discount
//    @Test
//    void testRemoveDiscountFromProduct() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.setDiscount(productDiscount);
//        sampleProduct.setDiscount(null);
//        assertEquals(availableWarehouseItem1.getSelling_price(), availableWarehouseItem1.getPrice_after_discount(), 0.01, "Price should return to original.");
//    }
//
//    // Test Case 7: Matching Item with Product Attributes
//    @Test
//    void testMatchingItemWithProductAttributes() {
//        assertTrue(sampleProduct.Matched_item_product(availableWarehouseItem1), "Item should match product.");
//        assertFalse(sampleProduct.Matched_item_product(differentProductItem), "Item should not match product.");
//    }
//
//    // Test Case 8: Expiring Item Handling
//    @Test
//    void testExpiringItemHandling() throws SQLException {
//        sampleProduct.addItem(expiringWarehouseItem);
//        assertEquals(E_Item_Status.about_to_expire, expiringWarehouseItem.getStatus(), "Item should be marked as about to expire.");
//    }
//
//    // Test Case 9: Out-of-Stock Behavior
//    @Test
//    void testOutOfStockBehavior() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.removeItem(availableWarehouseItem1);
//        assertEquals(E_Product_Status.Out_of_stock, sampleProduct.getStatus(), "Product should be out of stock.");
//    }
//
//    // Test Case 10: Updating Product Status Based on Item Expiry
//    @Test
//    void testUpdateProductStatusBasedOnItemExpiry() throws SQLException {
//        sampleProduct.addItem(expiringWarehouseItem);
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.updateStatus();
//        assertEquals(E_Product_Status.about_to_finish, sampleProduct.getStatus(), "Product status should be updated.");
//    }
//
//    // Test Case 11: Testing Adding Multiple Items
//    @Test
//    void testAddingMultipleItems() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem2);
//        sampleProduct.addItem(availableWarehouseItem3);
//        assertEquals(3, sampleProduct.getItems().size(), "There should be 3 items added.");
//    }
//
//    // Test Case 12: Test Quantity Calculation After Move
//    @Test
//    void testQuantityAfterMovingItems() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem2);
//        sampleProduct.moveItemTo(availableWarehouseItem1, E_Item_Place.Store);
//        assertEquals(1, sampleProduct.getQuantity_in_store(), "One item should be moved to store.");
//        assertEquals(1, sampleProduct.getQuantity_in_warehouse(), "One item should remain in the warehouse.");
//    }
//
//    // Test Case 13: Test Product Availability Status
//    @Test
//    void testProductAvailabilityStatus() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        assertEquals(E_Product_Status.Available, sampleProduct.getStatus(), "Product should be available.");
//    }
//
//    // Test Case 14: Apply a New Discount to Product
//    @Test
//    void testApplyNewDiscount() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.setDiscount(productDiscount);
//        sampleProduct.setDiscount(newDiscount);
//        double expectedPrice = availableWarehouseItem1.getSellingPrice() * (1 - newDiscount.getDiscountRate() / 100);
//        assertEquals(expectedPrice, availableWarehouseItem1.getPriceAfterDiscount(), 0.01, "New discount should be applied.");
//    }
//
//    // Test Case 15: Removing All Items Should Set Out of Stock
//    @Test
//    void testRemoveAllItemsOutOfStock() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem2);
//        sampleProduct.removeItem(availableWarehouseItem1);
//        sampleProduct.removeItem(availableWarehouseItem2);
//        assertEquals(E_Product_Status.Out_of_stock, sampleProduct.getStatus(), "Product should be out of stock.");
//    }
//
//    // Test Case 16: Add Expired Items to Product
//    @Test
//    void testAddExpiredItems() throws SQLException {
//        Item expiredItem = new Item("ExpiredItem", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, LocalDate.of(2022, 1, 1), E_Item_Status.Expired, E_Item_Place.Warehouse);
//        sampleProduct.addItem(expiredItem);
//        assertEquals(E_Item_Status.EXPIRED, expiredItem.getStatus(), "Item should be marked as expired.");
//    }
//
//    // Test Case 17: Adding Duplicate Items
//    @Test
//    void testAddingDuplicateItems() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem1);  // Adding same item again
//        assertEquals(1, sampleProduct.getItems().size(), "Duplicate items should not be added.");
//    }
//
//    // Test Case 18: Test Product Minimum Quantity
//    @Test
//    void testProductMinimumQuantity() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        assertTrue(sampleProduct.getItems().size() >= sampleProduct.getMin_quantity(), "Product should meet minimum quantity requirement.");
//    }
//
//    // Test Case 19: Test Adding Item with Different Attributes
//    @Test
//    void testAddingItemWithDifferentAttributes() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        assertFalse(sampleProduct.Matched_item_product(differentProductItem), "Item with different attributes should not match the product.");
//    }
//
//    // Test Case 20: Test Product Not Out of Stock After Adding Items
//    @Test
//    void testProductNotOutOfStock() throws SQLException {
//        sampleProduct.addItem(availableWarehouseItem1);
//        sampleProduct.addItem(availableWarehouseItem2);
//        assertNotEquals(E_Product_Status.Out_of_stock, sampleProduct.getStatus(), "Product should not be out of stock after adding items.");
//    }
//}
