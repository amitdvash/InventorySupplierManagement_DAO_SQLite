package dev.Suppliers.Domain;
import java.util.HashMap;

public class ProductController {


    // Add a new product
    public Product createProduct(String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        Product product = new Product(name, discountDetails, price, expirationDays, weight, agreement);
        System.out.println("Product created: " + product.getCatalogID());
        return product;
    }

//    public Product getProduct(String productID) {
//        for (Product product : products) {
//            if (product.getProductID().equals(productID)) {
//                return product;
//            }
//        }
//        System.out.println("Product not found: " + productID);
//        return null;
//    }
//
//    // Get all products
//    public List<Product> getAllProducts() {
//        return products;
//    }
}
