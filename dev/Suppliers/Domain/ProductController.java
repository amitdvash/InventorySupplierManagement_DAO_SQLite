package dev.Suppliers.Domain;

import dev.Suppliers.DataBase.ProductDTO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class ProductController {
    private ProductDTO productDTO;

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

}
