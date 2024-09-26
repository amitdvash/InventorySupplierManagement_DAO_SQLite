//package dev.Inventory.Test;
//
//import dev.Inventory.Classes.Discount;
//import dev.Inventory.Classes.Inventory;
//import dev.Inventory.Classes.Item;
//import dev.Inventory.Classes.Product;
//import dev.Inventory.Enums.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class InventoryTest {
//
//    private Inventory inventory;
//    private Product product1, product2;
//    private Item item1, item2, item3;
//    private Discount discount;
//
//    @BeforeEach
//    public void setUp() {
//        Inventory inventory = new Inventory();
//
//        // Create test products
//        product1 = new Product("Cola", "Drink", "Sparkling", 500, 10, null);
//        product2 = new Product("Pepsi", "Drink", "Sparkling", 600, 5, null);
//
//        // Create test items
//        item1 = new Item("Cola", 10.0, 10, "Manufacturer1", "Drink", "Sparkling", 500, LocalDate.now().plusDays(10), E_Item_Status.Available, E_Item_Place.Store);
//        item2 = new Item("Cola", 12.0, 20, "Manufacturer2", "Drink", "Sparkling", 500, LocalDate.now().plusDays(20), E_Item_Status.Available, E_Item_Place.Warehouse);
//        item3 = new Item("Pepsi", 8.0, 14.0, "Manufacturer1", "Drink", "Sparkling", 600, LocalDate.now().plusDays(15), E_Item_Status.Available, E_Item_Place.Store);
//
//        // Create test discount
//        discount = new Discount(50, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));
//    }
//
//    @Test
//    public void testAddProduct() {
//        // Add product to the inventory
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//
//        // Verify product is added correctly
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertNotNull(retrievedProduct, "Product should be added to the inventory");
//        assertEquals(product1.getName(), retrievedProduct.getName(), "Product name should match");
//    }
//
//    @Test
//    public void testAddItemToProduct() {
//        // Add product and item
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//
//        // Verify item is added correctly to the product
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertNotNull(inventory.findItem(item1.getName(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getPlace()), "Item should be added to the product");
//    }
//    //
//    @Test
//    public void testRemoveProduct() {
//        // Add and remove product
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.removeProduct(product1);
//
//        // Verify product is removed
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertNull(retrievedProduct, "Product should be removed from the inventory");
//    }
//
//    @Test
//    public void testRemoveItemFromProduct() {
//        // Add product and item
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//
//        // Remove item
//        inventory.removeItem(item1);
//
//        // Verify item is removed
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertNull(retrievedProduct.getItems().get(item1.getId()), "Item should be removed from the product");
//    }
//
//    @Test
//    public void testApplyDiscountToProduct() {
//        // Add product and items
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//        inventory.addItem(item2.getName(), item2.getCost_price(), item2.getSelling_price(), item2.getManufacturer(), item2.getCategory(), item2.getSub_category(), item2.getSize(), item2.getExpiry_date(), item2.getStatus(), item2.getPlace());
//
//        // Apply discount
//        inventory.applyDiscountToProduct(product1, discount.getDiscountRate(), discount.getStart_date(), discount.getEnd_date());
//
//
//        // Verify discount is applied to all items
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertEquals(5,inventory.findItem(item1.getName(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getPlace()).getPrice_after_discount(), 0.001, "Discount should be applied to item1");
//        assertEquals(10,inventory.findItem(item2.getName(), item2.getCategory(), item2.getSub_category(), item2.getSize(), item2.getPlace()).getPrice_after_discount(), 0.001, "Discount should be applied to item1");
//    }
//
//    @Test
//    public void testMoveItemToDifferentPlace() {
//        // Add product and item
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//
//        // Move item from store to warehouse
//        inventory.moveItemTo(item1, E_Item_Place.Warehouse);
//
//        // Verify item has moved to warehouse
//        Product retrievedProduct = inventory.getProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize());
//        assertEquals(E_Item_Place.Warehouse, item1.getPlace(), "Item should be moved to warehouse");
//    }
//
//    @Test
//    public void testGetProductsByCategory() {
//        // Add products
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addProduct(product2.getName(), product2.getCategory(), product2.getSub_category(), product2.getSize(), product2.getMin_quantity());
//
//        // Retrieve products by category
//        List<Product> drinks = inventory.getProductsByCategory("Drink");
//
//        // Verify products are retrieved correctly
//        assertEquals(2, drinks.size(), "There should be 2 products in the 'Drink' category");
//    }
//
//    @Test
//    public void testGetItemsByStatus() {
//        // Add product and items
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//        inventory.addItem(item2.getName(), item2.getCost_price(), item2.getSelling_price(), item2.getManufacturer(), item2.getCategory(), item2.getSub_category(), item2.getSize(), item2.getExpiry_date(), item2.getStatus(), item2.getPlace());
//
//        // Retrieve items by status
//        List<Item> availableItems = inventory.getItemsByStatus(E_Item_Status.Available);
//
//        // Verify the correct number of items are retrieved
//        assertEquals(2, availableItems.size(), "There should be 2 available items in the inventory");
//    }
//
//    @Test
//    public void testMoveNonExistingItem() {
//        // Try moving an item that doesn't exist
//        Item nonExistingItem = new Item("Non-existing item", 5.0, 7.0, "ManufacturerX", "Drink", "Non-sparkling", 300, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Store);
//        inventory.moveItemTo(nonExistingItem, E_Item_Place.Warehouse);
//
//        // No assertion here, just ensure that no exceptions are thrown, and the message is logged as "Item does not exist in the inventory"
//    }
//    @Test
//    public void testApplyExpiredDiscountToProduct() {
//        // Add product and items to inventory
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//
//        // Apply expired discount to the product
//        Discount expiredDiscount = new Discount(10.0, LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
//        inventory.applyDiscountToProduct(product1, expiredDiscount.getDiscountRate(), expiredDiscount.getStart_date(), expiredDiscount.getEnd_date());
//
//        // Verify that no discount is applied
//        assertEquals(item1.getSelling_price(), inventory.findItem(item1.getName(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getPlace()).getPrice_after_discount(), 0.001, "Expired discount should not be applied.");
//    }
//
//
//    @Test
//    public void testMoveItemFromStoreToWarehouseAndBack() {
//        // Add product and item to inventory
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addItem(item1.getName(), item1.getCost_price(), item1.getSelling_price(), item1.getManufacturer(), item1.getCategory(), item1.getSub_category(), item1.getSize(), item1.getExpiry_date(), item1.getStatus(), item1.getPlace());
//
//        // Move item from Store to Warehouse
//        inventory.moveItemTo(item1, E_Item_Place.Warehouse);
//        assertEquals(E_Item_Place.Warehouse, item1.getPlace(), "Item should be moved to the warehouse.");
//
//        // Move item back to Store
//        inventory.moveItemTo(item1, E_Item_Place.Store);
//        assertEquals(E_Item_Place.Store, inventory.findItem(item1.getName(), item1.getCategory(), item1.getSub_category(), item1.getSize(), E_Item_Place.Store).getPlace(), "Item should be moved back to the store.");
//    }
//
//
//    @Test
//    public void testGetProductsBySubCategory() {
//        // Add products to inventory
//        inventory.addProduct(product1.getName(), product1.getCategory(), product1.getSub_category(), product1.getSize(), product1.getMin_quantity());
//        inventory.addProduct(product2.getName(), product2.getCategory(), product2.getSub_category(), product2.getSize(), product2.getMin_quantity());
//
//        // Retrieve products by subcategory
//        List<Product> sparklingProducts = inventory.getProductsBySubCategory("Sparkling");
//
//        // Verify correct products are retrieved
//        assertEquals(2, sparklingProducts.size(), "There should be 2 products in the 'Sparkling' subcategory.");
//    }
//
//
//}