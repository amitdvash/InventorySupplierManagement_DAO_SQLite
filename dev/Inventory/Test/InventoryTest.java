package dev.Inventory.Test;
import dev.Inventory.Classes.Inventory;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dev.Inventory.Enums.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;
    private Item item1;
    private Item item2;
    private Product product1;


    @BeforeEach
    public void setUp() {
        // Initialize the singleton inventory instance and some items/products for testing
        inventory = Inventory.getInstance();
        item1 = new Item("Item1", 10.0, 15.0, "Manufacturer1", "Category1", "SubCategory1", 1.5, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        item2 = new Item("Item2", 12.0, 18.0, "Manufacturer2", "Category2", "SubCategory2", 1.0, null, E_Item_Status.Available, E_Item_Place.Store);
        product1 = new Product("Product1", "Category1", "SubCategory1", 1.5, 10, null); // Updated product constructor
    }



    @Test
    public void testGetProductsByStatus() {
        // Test querying products by status
        List<Product> aboutToFinishProducts = inventory.getProductsByStatus(E_Product_Status.about_to_finish);
        assertNotNull(aboutToFinishProducts);
    }

    @Test
    public void testGetProductsBySubCategory() {
        // Test querying products by subcategory
        List<Product> productsBySubCategory = inventory.getProductsBySubCategory("SubCategory2");
        assertNotNull(productsBySubCategory);
    }

    @Test
    public void testMoveItemTo() {
        // Test moving an item to a different place
        inventory.addItem(item1);
        inventory.moveItemTo(item1, E_Item_Place.Store);
        assertEquals(E_Item_Place.Store, item1.getPlace());

        inventory.moveItemTo(item1, E_Item_Place.Warehouse);
        assertEquals(E_Item_Place.Warehouse, item1.getPlace());
    }

    @Test
    public void testGetProductByName() {
        // Test retrieving a product by name
        inventory.addItem(item1); // Assuming this method adds the product as well
        Product foundProduct = inventory.getProductByName(item1.getCategory());
        assertNotNull(foundProduct);
    }
    @Test
    public void testAddItem() {
        // Test adding an item to the inventory
        inventory.addItem(item1);

        assertNotNull(inventory.getProductByName("Item1"));
        assertEquals(inventory.getProductByName(item1.getName()).getItems().get(item1.getId()), item1);
    }



}
