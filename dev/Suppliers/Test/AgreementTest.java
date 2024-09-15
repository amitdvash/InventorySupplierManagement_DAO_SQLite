

import dev.Suppliers.Domain.Agreement;
import dev.Suppliers.Domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AgreementTest {
    private Agreement agreement;

    @BeforeEach
    void setUp() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Banana", new HashMap<>(), 10.0, 7, 1.5, null));
        agreement = new Agreement(productList, new HashMap<>(), Arrays.asList("Monday"), true);  // Updated constructor
    }

    @Test
    void testGetProductList() {
        assertFalse(agreement.getProductList().isEmpty());
    }

    @Test
    void testAddProduct() {
        Product newProduct = new Product("Apple", new HashMap<>(), 15.0, 14, 2.0, null);
        agreement.addProduct(newProduct);
        assertTrue(agreement.getProductList().contains(newProduct));
    }

    @Test
    void testRemoveProduct() {
        Product product = agreement.getProductList().get(0);
        agreement.getProductList().remove(product);
        assertFalse(agreement.getProductList().contains(product));
    }

    @Test
    void testGetSupplyDays() {
        assertTrue(agreement.getSupplyDays().contains("Monday"));
    }

    @Test
    void testSetSupplyDays() {
        List<String> newSupplyDays = Arrays.asList("Tuesday", "Friday");
        agreement.setSupplyDays(newSupplyDays);
        assertEquals(newSupplyDays, agreement.getSupplyDays());
    }

    @Test
    void testGetSelfSupply() {
        assertTrue(agreement.isSelfSupply());
    }

    @Test
    void testSetSelfSupply() {
        agreement.setSelfSupply(false);
        assertFalse(agreement.isSelfSupply());
    }

    @Test
    void testAddDiscountToAgreement() {
        HashMap<Integer, Double> discountDetails = new HashMap<>();
        discountDetails.put(10, 5.0);
        agreement.getDiscountDetails().put("P1", discountDetails);
        assertEquals(5.0, agreement.getDiscountDetails().get("P1").get(10));
    }

    @Test
    void testRemoveDiscountFromAgreement() {
        HashMap<Integer, Double> discountDetails = new HashMap<>();
        discountDetails.put(10, 5.0);
        agreement.getDiscountDetails().put("P1", discountDetails);
        agreement.getDiscountDetails().remove("P1");
        assertNull(agreement.getDiscountDetails().get("P1"));
    }
}
