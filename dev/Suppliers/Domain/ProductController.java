package dev.Suppliers.Domain;

import dev.Suppliers.DataBase.ProductDTO;
import dev.Suppliers.DataBase.SupplierDTO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    private ProductDTO productDTO;
    private SupplierDTO supplierDTO;



    public ProductController(Connection connection) {
        this.productDTO = new ProductDTO(connection);
    }

    // Create a new product and add it to the database
    public Product createProduct(String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        Product product = new Product(0, name, discountDetails, price, expirationDays, weight, agreement);
        productDTO.create(product); // Save to the database through DTO
        System.out.println("Product created: " + product.getCatalogID());
        return product;
    }

    // Find the cheapest supplier for a product and quantity using SQL queries
    public Supplier findCheapestSupplier(String productName, int quantity) {
        // This method will delegate the query logic to the DTO to get the cheapest supplier for a product
        return productDTO.findCheapestSupplier(productName, quantity);
    }

    // Get all product names from the database
    public List<String> getAllProductNames() {
        return productDTO.getAllProductNames(); // Get all product names from the database through DTO
    }

    public int createProductInDatabase(Product product) {
        return productDTO.create(product); // Returns the generated catalogID
    }

    public void addProductDiscounts(int catalogID, HashMap<Integer, Double> discountDetails) {
        for (Map.Entry<Integer, Double> entry : discountDetails.entrySet()) {
            productDTO.addDiscount(catalogID, entry.getKey(), entry.getValue());
        }
    }

    // Method to delete a product from the database based on the catalog ID
    public void deleteProductFromDatabase(int catalogID) {
        // Delete product discounts first
        productDTO.deleteProductDiscounts(catalogID); // Ensure all associated discounts are deleted first
        // Then delete the product itself
        productDTO.delete(catalogID);
        System.out.println("Product with Catalog ID " + catalogID + " deleted from the database.");
    }

    public void deleteProductDiscount(int catalogID, int quantity) {
        productDTO.deleteDiscount(catalogID, quantity); // Call DTO method to delete the discount
    }

    public Product getProductByName(String productName) {
        return productDTO.readByName(productName); // Use DTO to get the product by name
    }

    public Product getProductBySupplierAndName(int supplierID, String productName) {
        return productDTO.getProductBySupplierAndName(supplierID, productName);
    }
}
