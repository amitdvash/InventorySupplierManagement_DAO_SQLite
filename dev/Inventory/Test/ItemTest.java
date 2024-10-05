//package dev.Inventory.Test;
//
//import dev.Inventory.Classes.Item;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import dev.Inventory.Enums.*;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//
//class ItemTest {
//
//    private Item item;
//
//    @BeforeEach
//    void setUp() {
//        // Initial setup before each test
//
//        item = new Item(
//                "Sample Item",
//                10.0,
//                15.0,
//                "Sample Manufacturer",
//                "Electronics",
//                "Mobile Phones",
//                1.5,
//                LocalDate.of(2025, 12, 31),
//                E_Item_Status.Available,
//                E_Item_Place.Store
//        );
//    }
//
//    @Test
//    void testGetName() {
//        assertEquals("Sample Item", item.getName());
//    }
//
//    @Test
//    void testSetName() {
//        item.setName("New Item Name");
//        assertEquals("New Item Name", item.getName());
//    }
//
//    @Test
//    void testGetCostPrice() {
//        assertEquals(10.0, item.getCostPrice());
//    }
//
//    @Test
//    void testSetCostPrice() {
//        item.setCost_price(12.0);
//        assertEquals(12.0, item.getCost_price());
//    }
//
//    @Test
//    void testGetSellingPrice() {
//        assertEquals(15.0, item.getSelling_price());
//    }
//
//    @Test
//    void testSetSellingPrice() {
//        item.setSelling_price(18.0);
//        assertEquals(18.0, item.getSelling_price());
//    }
//
//    @Test
//    void testGetManufacturer() {
//        assertEquals("Sample Manufacturer", item.getManufacturer());
//    }
//
//    @Test
//    void testSetManufacturer() {
//        item.setManufacturer("New Manufacturer");
//        assertEquals("New Manufacturer", item.getManufacturer());
//    }
//
//    @Test
//    void testGetId() {
//        assertNotNull(item.getId());
//    }
//
//    @Test
//    void testGetCategory() {
//        assertEquals("Electronics", item.getCategory());
//    }
//
//    @Test
//    void testSetCategory() {
//        item.setCategory("New Category");
//        assertEquals("New Category", item.getCategory());
//    }
//
//    @Test
//    void testGetSubCategory() {
//        assertEquals("Mobile Phones", item.getSub_category());
//    }
//
//    @Test
//    void testSetSubCategory() {
//        item.setSub_category("New Subcategory");
//        assertEquals("New Subcategory", item.getSub_category());
//    }
//
//    @Test
//    void testGetSize() {
//        assertEquals(1.5, item.getSize());
//    }
//
//    @Test
//    void testSetSize() {
//        item.setSize(2.0);
//        assertEquals(2.0, item.getSize());
//    }
//
//    @Test
//    void testGetExpiryDate() {
//        assertEquals(LocalDate.of(2025, 12, 31), item.getExpiry_date());
//    }
//
//    @Test
//    void testSetExpiryDate() {
//        item.setExpiry_date(LocalDate.of(2026, 1, 1));
//        assertEquals(LocalDate.of(2026, 1, 1), item.getExpiry_date());
//    }
//
//    @Test
//    void testGetStatus() {
//        assertEquals(E_Item_Status.Available, item.getStatus());
//    }
//
//
//
//    @Test
//    void testGetPlace() {
//        assertEquals(E_Item_Place.Store, item.getPlace());
//    }
//
//    @Test
//    void testSetPlace() {
//        item.setPlace(E_Item_Place.Warehouse);
//        assertEquals(E_Item_Place.Warehouse, item.getPlace());
//    }
//
//
//
//}