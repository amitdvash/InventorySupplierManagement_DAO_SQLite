//package dev.Inventory.Test;
//import dev.Inventory.Classes.Discount;
//import dev.Inventory.Classes.Inventory;
//import dev.Inventory.Classes.Item;
//import dev.Inventory.Classes.Product;
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import dev.Inventory.Enums.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class InventoryTest {
//    private Inventory inventory;
//    private Item item1;
//    private Item item2,item3,item4;
//    private Product product1,P1,P2;
//
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize the singleton inventory instance and some items/products for testing
//        inventory = Inventory.getInstance();
//        item1 = new Item("cola", 10.0, 15.0, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
//        item2 = new Item("cola", 12.0, 18.0, "Manufacturer2", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Store);
//        product1 = new Product("cola", "Category1", "SubCategory1", 10, 10, null); // Updated product constructor
//        // Create two products with same name but different sizes
//        P1 = new Product("cola", "drink", "sparkling", 500, 4, null);
//         P2 = new Product("cola", "drink", "sparkling", 500, 4, null);
//
//        // Create corresponding items for the products
//         item3 = new Item("cola", 10, 100, "Manufacturer1", "drink", "sparkling", 500, null, E_Item_Status.Available, E_Item_Place.Store);
//         item4 = new Item("cola", 10, 100, "Manufacturer1", "drink", "sparkling", 500, null, E_Item_Status.Available, E_Item_Place.Store);
//    }
//
//    @Test
//    public void testRemoveProduct() {
//        // Add a product and an item
//        inventory.addProduct(P2);
//        inventory.addItem(item3);
//
//        // Ensure the product and item are added
//        assertNotNull(inventory.getProductsByName("cola"));
//        assertNotNull(inventory.getProductsByName("cola").getItems().get(item3.getId()));
//
//        // Remove the product
//        inventory.removeProduct(P1);
//
//        // Ensure the product and its items are removed
//        assertNull(inventory.getProductsByName("cola"), "Product should be removed from inventory");
//    }
//
////need to fix the bug____________________________________________________________________________________
//
//
//    //that you add a new product and there is already the same product with the same name but you want to add
//    // a product with a different category or a different subcategory so it overrides the previous product
//    //in the map that we save the all product
//    @Test
//    public void testAddMultipleItemsWithSameNameDifferentSize() {
//    // Add the items to the inventory
//        inventory.addProduct(P2);
//       inventory.addProduct(P1);// if remove the // this work else that will make bug
//    inventory.addItem(item3);
//    inventory.addItem(item4);
//    // Verify that both items were added correctly
//    assertNotNull(inventory.getProductsByName("cola"), "Product 'cola' should exist in the inventory");
//    assertEquals(4, inventory.getProductsByName("cola").getQuantity_in_store()+inventory.getProductByName("cola").getQuantity_in_warehouse(), "There should be 2 items for the product 'cola' in the inventory");
//    // Print the inventory state (for manual inspection, not typically part of automated tests)
//    System.out.println(inventory);
//}
////need to fix the bug____________________________________________________________________________________
//
//
//    @Test
//    public void testRemoveItem() {
//        inventory.addProduct(product1);
//        inventory.addItem(item1);
//        inventory.removeItem(item1);
//        assertNull((inventory.getProductsByName(product1.getName()).getItems().get(item1.getId())), "Item1 should be removed from the inventory");
//    }
//
//
//
//    @Test
//    public void testGetProductsByStatus() {
//        // Test querying products by status
//        List<Product> aboutToFinishProducts = inventory.getProductsByStatus(E_Product_Status.about_to_finish);
//        assertNotNull(aboutToFinishProducts);
//    }
//
//    @Test
//    public void testGetProductsBySubCategory() {
//        // Test querying products by subcategory
//        List<Product> productsBySubCategory = inventory.getProductsBySubCategory("SubCategory2");
//        assertNotNull(productsBySubCategory);
//    }
//
//    @Test
//    public void testMoveItemTo() {
//        // Test moving an item to a different place
//        inventory.addProduct(product1);
//        inventory.addItem(item1);
//        inventory.moveItemTo(item1, E_Item_Place.Store);
//        assertEquals(E_Item_Place.Store, item1.getPlace());
//
//        inventory.moveItemTo(item1, E_Item_Place.Warehouse);
//        assertEquals(E_Item_Place.Warehouse, item1.getPlace());
//    }
//
//    @Test
//    public void testGetProductByName() {
//        // Test retrieving a product by name
//        inventory.addProduct(product1);
//        inventory.addItem(item1); // Assuming this method adds the product as well
//        Product foundProduct = inventory.getProductByName(product1.getName());
//        assertNotNull(product1.getItems().get(item1.getId()));
//
//    }
//    @Test
//    public void testAddItem() {
//        // Test adding an item to the inventory
//        inventory.addProduct(product1);
//        inventory.addItem(item1);
//        assertNotNull(inventory.getProductByName(product1.getName()));
//    }
//
//    @Test
//    public void testSingletonInstance() {
//        Inventory inventory1 = Inventory.getInstance();
//        Inventory inventory2 = Inventory.getInstance();
//
//        assertSame(inventory1, inventory2, "Both inventory instances should be the same (Singleton)");
//    }
//
//    @Test
//    public void testAddProduct() {
//        // Create a product
//        Product product1 = new Product("cola", "drink", "sparkling", 300, 4, null);
//
//        // Add product to inventory
//        inventory.addProduct(product1);
//
//        // Ensure the product is added
//        assertNotNull(inventory.getProductByName("cola"), "Product 'cola' should be added to the inventory");
//
//        // Try adding the same product again
//
//        inventory.addProduct(product1);
//    }
//
//    @Test
//    public void testApplyDiscountToProduct() {
//        // Create and add a product
//        Product product1 = new Product("cola", "drink", "sparkling", 500, 4, null);
//        inventory.addProduct(product1);
//        inventory.addItem(item3);
//        inventory.addItem(item4);
//        // Create and apply a discount
//        Discount discount = new Discount(50, LocalDate.ofYearDay(2024, 20), LocalDate.ofYearDay(2024, 300));
//        inventory.applyDiscountToProduct(product1, discount);
//        System.out.println(item3.getPrice_after_discount());
//        // Ensure the discount is applied
//        assertEquals(inventory.getProductByName("cola").getItems().get(item3.getId()).getPrice_after_discount(), 50, "Discount should be applied to the product");
//    }
//
//
//}
