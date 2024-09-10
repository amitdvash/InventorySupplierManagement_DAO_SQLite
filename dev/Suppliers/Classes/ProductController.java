package dev.Suppliers.Classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductController {
    private List<Product> products = new ArrayList<>();

    // Add a new product
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getProductID());
    }

    // Update product price
    public void updateProductPrice(String productID, double newPrice) {
        Product product = getProduct(productID);
        if (product != null) {
            product.setPrice(newPrice);
            System.out.println("Product price updated for productID: " + productID);
        } else {
            System.out.println("Product not found: " + productID);
        }
    }

    // Get a product by ID
    public Product getProduct(String productID) {
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        System.out.println("Product not found: " + productID);
        return null;
    }

    // Add or update discounts for a product
    public void updateProductDiscounts(String productID, HashMap<Integer, Double> discountDetails) {
        Product product = getProduct(productID);
        if (product != null) {
            product.setDiscountDetails(discountDetails);
            System.out.println("Discounts updated for productID: " + productID);
        } else {
            System.out.println("Product not found: " + productID);
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        return products;
    }
}
