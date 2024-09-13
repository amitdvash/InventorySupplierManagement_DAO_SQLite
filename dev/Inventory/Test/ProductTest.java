package dev.Inventory.Test;

import com.sun.source.tree.AssertTree;
import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Item;
import dev.Inventory.Classes.Product;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class ProductTest
{


    Product sampleProduct;
    Item expiringWarehouseItem, availableWarehouseItem1, availableWarehouseItem2, availableWarehouseItem3, availableWarehouseItem4, differentProductItem;
    Discount productDiscount,newDiscount;

    @BeforeEach
    void setUp() {
        // Initializing items for the product
        expiringWarehouseItem = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.about_to_expire, E_Item_Place.Warehouse);
        availableWarehouseItem1 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        availableWarehouseItem2 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        availableWarehouseItem3 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);
        availableWarehouseItem4 = new Item("Item1", 10, 20, "Manufacturer1", "Category1", "SubCategory1", 10, null, E_Item_Status.Available, E_Item_Place.Warehouse);

        differentProductItem = new Item("Item2", 20, 30, "Manufacturer2", "Category2", "SubCategory2", 10, LocalDate.now(), E_Item_Status.Available, E_Item_Place.Warehouse);

        // Initializing the product and discount
        sampleProduct = new Product("Item1", "Category1", "SubCategory1", 10, 2, null);
        productDiscount = new Discount(50, LocalDate.ofYearDay(2024, 20), LocalDate.ofYearDay(2024, 300));
        newDiscount = new Discount(30, LocalDate.ofYearDay(2024, 50), LocalDate.ofYearDay(2024, 300));

    }









    @Test
    void testApplyDiscount() {
        // Add items to the product
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem2);

        // Apply discount to the product
        sampleProduct.setDiscount(productDiscount);

        // Verify that the discount was applied to each item
        double expectedPrice = availableWarehouseItem1.getSelling_price() * (1 - productDiscount.getDiscountRate() / 100);
        assertEquals(expectedPrice, availableWarehouseItem1.getPrice_after_discount(), 0.01, "Discount should be applied to the item's selling price.");
        assertEquals(expectedPrice, availableWarehouseItem2.getPrice_after_discount(), 0.01, "Discount should be applied to the item's selling price.");
    }


    @Test
    void testAddItems() {
        sampleProduct.addItem(expiringWarehouseItem);
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem2);
        sampleProduct.addItem(availableWarehouseItem3);
        sampleProduct.addItem(availableWarehouseItem4);

        assertTrue(sampleProduct.getItems().containsKey(expiringWarehouseItem.getId()), "expiringWarehouseItem should be added to the product");
        assertTrue(sampleProduct.getItems().containsKey(availableWarehouseItem1.getId()), "availableWarehouseItem1 should be added to the product");
    }

    @Test
    void testMoveItem() {
        sampleProduct.addItem(expiringWarehouseItem);
        sampleProduct.moveItemTo(expiringWarehouseItem, E_Item_Place.Store);
        assertEquals(E_Item_Place.Store, expiringWarehouseItem.getPlace(), "expiringWarehouseItem should be moved to the store");
    }
    @Test
    void testRemoveItem() {
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.removeItem(availableWarehouseItem1);
        assertFalse(sampleProduct.getItems().containsKey(availableWarehouseItem1.getId()), "availableWarehouseItem1 should be removed from the product");
    }



    @Test
    void testQuantityUpdateOnAddRemove() {
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem2);
        sampleProduct.addItem(expiringWarehouseItem);
        assertEquals(3, sampleProduct.getItems().size(), "There should be 3 items added to the product");

        // Move one item to store and check quantities
        sampleProduct.moveItemTo(availableWarehouseItem1, E_Item_Place.Store);
        assertEquals(1, sampleProduct.getQuantity_in_store(), "There should be 1 item in the store");
        assertEquals(2, sampleProduct.getQuantity_in_warehouse(), "There should be 2 items in the warehouse");

        // Remove an item and check quantities
        sampleProduct.removeItem(availableWarehouseItem1);
        assertEquals(0, sampleProduct.getQuantity_in_store(), "Store quantity should be 0 after removal");
        assertEquals(2, sampleProduct.getQuantity_in_warehouse(), "Warehouse quantity should remain the same");
    }



    @Test
    void testMatchedItemProduct() {
        // Test with matching item
        assertTrue(sampleProduct.Matched_item_product(availableWarehouseItem1), "Item1 should match the product attributes");

        // Test with non-matching item
        assertFalse(sampleProduct.Matched_item_product(differentProductItem), "Item2 should not match the product attributes");
    }

    @Test
    void testProductOutOfStockStatus() {
        // Add items to the product
        sampleProduct.addItem(availableWarehouseItem1);
        sampleProduct.addItem(availableWarehouseItem2);

        // Initially, product should not be out of stock
        assertEquals(E_Product_Status.about_to_finish, sampleProduct.getStatus(), "Product should not be out of stock initially");

        sampleProduct.addItem(availableWarehouseItem3);

        assertEquals(sampleProduct.getStatus(), E_Product_Status.Available);


        // Remove all items
        sampleProduct.removeItem(availableWarehouseItem1);
        sampleProduct.removeItem(availableWarehouseItem2);
        sampleProduct.removeItem(availableWarehouseItem3);

        // Now the product should be out of stock
        assertEquals(E_Product_Status.Out_of_stock, sampleProduct.getStatus(), "Product should be out of stock after removing all items");
    }
    @Test
    void testExpiryDateHandling() {
        // Add expiring item
        sampleProduct.addItem(expiringWarehouseItem);

        // Check the status of the expiring item
        assertEquals(E_Item_Status.about_to_expire, expiringWarehouseItem.getStatus(), "Item should be marked as about to expire.");

        // Check if the product still matches available status
        sampleProduct.updateStatus();
        assertEquals(E_Product_Status.about_to_finish, sampleProduct.getStatus(), "Product status should update to 'about to finish' when items are about to expire.");
    }


}
