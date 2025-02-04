
package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;
import dev.Inventory.ClassesDTO.ProductDTO;
import dev.Inventory.ClassesDTO.ItemDTO;
import dev.Inventory.ClassesDTO.DiscountDTO;
import dev.Suppliers.Domain.ControllersManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Inventory {

    private ProductDTO productSQL;
    private ItemDTO itemSQL;
    private DiscountDTO discountSQL;

    public Inventory(Connection connection) throws SQLException {
        // Initialize database connection for products, items, and discounts
        this.productSQL = new ProductDTO(connection);
        this.itemSQL = new ItemDTO(connection);
        this.discountSQL = new DiscountDTO(connection);
    }

    public void addProduct(String name, String category, String subCategory, double size, int minQuantity) throws SQLException {
        // Create a new product
        Product product = new Product(name, category, subCategory, size, minQuantity, null);

        // Check if the product already exists
        if (productSQL.readByCompositeKey(name, category, subCategory, size) != null) {
            System.out.println("Error: Product " + name + " already exists in the inventory.");
            return;
        }

        // Insert the product into the database
        productSQL.create(product);

        // Check if a discount applies to the product by name, category, or subcategory


        System.out.println("Product " + name + " added successfully.");
    }

    // Add an item to a product in the inventory
    public void addItemToProduct(String name, double costPrice, double sellingPrice, String manufacturer, String category,
                                 String subCategory, double size, LocalDate expiry, E_Item_Status status, E_Item_Place place) throws SQLException {
        // Find the corresponding product
        Product product = productSQL.readByCompositeKey(name, category, subCategory, size);

        // Check if the product exists
        if (product == null) {
            System.out.println("Error: Product not found.");
            return;
        }

        Item newItem = new Item(name, costPrice, sellingPrice, manufacturer, category, subCategory, size, expiry, status, place);

        double finalSellingPrice = sellingPrice;
        if (product.getDiscount() != null) {
            // Apply the discount to the selling price
            finalSellingPrice = sellingPrice - (sellingPrice * ((product.getDiscount().getDiscountRate())) / 100);
            newItem.setPriceAfterDiscount(finalSellingPrice);
        }


        // Create a new item


        // Add the item to the database
        // i need to check that if have activeate a discount
        newItem.updateStatus();
        if (itemSQL.create(newItem)) {
            // Add the item to the product's list of items
            product.addItem(newItem);  // Updates quantities in the product and status
            productSQL.update(product);
            System.out.println("Item " + name + " added to product " + product.getName() + " successfully.");
        } else {
            System.out.println("Error: Could not add the item to the product.");
        }
    }

    // Remove an item from a product in the inventory
    public void removeItemFromProduct(String name, String category, String subCategory, double size, E_Item_Place place) throws SQLException {
        // Find the corresponding product
        Product product = productSQL.readByCompositeKey(name, category, subCategory, size);

        // Check if the product exists
        if (product == null) {
            System.out.println("Error: Product not found.");
            return;
        }


        // Check if the item exists in the product's inventory
        Item item = itemSQL.readByCompositeKey(name, category, subCategory, size, place);
        if (item == null) {
            System.out.println("Error: Item not found for the product.");
            return;
        }

        // Remove the item from the database
        if (itemSQL.delete(item)) {
            // Remove the item from the product's list of items
            product.removeItem(item);  // Updates quantities in the product and status
            productSQL.update(product);
            System.out.println("Item " + name + " removed from product " + product.getName() + " successfully.");
        } else {
            System.out.println("Error: Could not remove the item from the product.");
        }

    }


    // Transfer an item from the warehouse to the shop, or vice versa
    // Transfer an item from one location to another
    public void transferItem(String name, String category, String subCategory, double size, E_Item_Place fromPlace, E_Item_Place toPlace) throws SQLException {
        // Find the item at its current place
        Item item = itemSQL.readByCompositeKey(name, category, subCategory, size, fromPlace);

        if (item == null) {
            System.out.println("Error: Item not found in the " + fromPlace + ".");
            return;
        }

        // Update the item's place to the new location
        item.setPlace(toPlace);  // Set the new place

        // Update the item in the database using the old place to find it
        if (itemSQL.update_new(item, fromPlace)) {
            System.out.println("Item " + name + " transferred from " + fromPlace + " to " + toPlace + " successfully.");
        } else {
            System.out.println("Error: Could not transfer the item.");
        }
    }
    // Apply a discount to a specific product

    public void applyDiscountToProduct(String name,String Category,String SubCategory,double size, double discountPercentage, LocalDate startDay, LocalDate endDay) throws SQLException {
        // Find all products by name (returns a list)
        Product product = productSQL.readByCompositeKey(name,Category,SubCategory,size);

        // Check if the list is empty
        if (product == null) {
            System.out.println("Error: No products found with the name " + name);
            return;
        }

        // Create the discount once and apply it to all products
        Discount discount = new Discount(discountPercentage, startDay, endDay);
        if (discount.isExpired()) {
            return;
        }

        discountSQL.create(discount);  // Save the discount in the database

        // Iterate through each product and apply the discount


            // Update the priceAfterDiscount using Discount_SQL
            discountSQL.updatePricesByName(product.getName(), discountPercentage);

            // Update the product in the database
            productSQL.update(product);

            System.out.println("Discount applied to product " + product.getName() + " and prices updated.");

    }

    public void applyDiscountToCategory(String category, double discountPercentage, LocalDate startDay, LocalDate endDay) throws SQLException {
        // Find all products in the specified category
        List<Product> products = productSQL.readAllByCategory(category);

        if (products == null || products.isEmpty()) {
            System.out.println("Error: No products found in the category " + category);
            return;
        }

        // Create the discount
        Discount discount = new Discount(discountPercentage, startDay, endDay);

        discountSQL.create(discount);

        // Iterate through all products in the category
        for (Product product : products) {
            // Apply the discount to the product
            product.setDiscount(discount);

            // Update the product in the database
            productSQL.update(product);
        }

        // Delegate price updating to Discount_SQL
        discountSQL.updateCategoryPrices(category, discountPercentage);

        System.out.println("Discount applied to all products in category " + category + " and prices updated.");
    }

    public void applyDiscountToSubCategory(String subCategory, double discountPercentage, LocalDate startDay, LocalDate endDay) throws SQLException {
        // Find all products in the specified sub-category
        List<Product> products = productSQL.readAllBySubCategory(subCategory);

        if (products == null || products.isEmpty()) {
            System.out.println("Error: No products found in the sub-category " + subCategory);
            return;
        }

        // Create the discount
        Discount discount = new Discount(discountPercentage, startDay, endDay);
        discountSQL.create(discount);

        // Iterate through all products in the sub-category
        for (Product product : products) {
            // Apply the discount to the product
            product.setDiscount(discount);

            // Update the product in the database
            productSQL.update(product);
        }

        // Delegate price updating to Discount_SQL
        discountSQL.updateSubCategoryPrices(subCategory, discountPercentage);

        System.out.println("Discount applied to all products in sub-category " + subCategory + " and prices updated.");


    }

    public void generateAllProductsReport() {
        List<Product> products = productSQL.readAll();  // Fetch all products from the database

        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("=== All Products Report ===");
            for (Product product : products) {
                System.out.println(product);  // Print product details (assuming Product has a meaningful toString() method)

                // Fetch all items associated with the current product
                List<Item> items = itemSQL.readAllByProduct(
                        product.getName(),
                        product.getCategory(),
                        product.getSub_category(),
                        product.getSize()
                );

                if (items.isEmpty()) {
                    System.out.println("  No items found for this product.");
                } else {
                    System.out.println("  Items for this product:");
                    for (Item item : items) {
                        System.out.println("    " + item);  // Assuming Item has a meaningful toString() method
                    }
                }

                System.out.println("------------------------------------------");
            }
        }
    }

    public void generateProductReport(String productName, String category, String subCategory, double size) throws SQLException {
        // Fetch the specific product by its composite key (name, category, subCategory, size)
        Product product = productSQL.readByCompositeKey(productName, category, subCategory, size);

        if (product == null) {
            System.out.println("Error: Product not found.");
        } else {
            System.out.println("=== Product Report for " + productName + " ===");
            System.out.println(product);  // Print product details (assuming Product has a meaningful toString() method)

            // Fetch all items associated with the specific product
            List<Item> items = itemSQL.readAllByProduct(product.getName(), product.getCategory(), product.getSub_category(), product.getSize());

            if (items.isEmpty()) {
                System.out.println("  No items found for this product.");
            } else {
                System.out.println("  Items for this product:");
                for (Item item : items) {
                    System.out.println("    " + item);  // Print each item (assuming Item has a meaningful toString() method)
                }
            }

            System.out.println("------------------------------------------");
        }
    }

    public void generateSubCategoryReport(String subCategory) throws SQLException {
        // Fetch all products by subCategory
        List<Product> products = productSQL.readAllBySubCategory(subCategory);

        if (products.isEmpty()) {
            System.out.println("No products found for sub-category: " + subCategory);
        } else {
            System.out.println("=== SubCategory Report for " + subCategory + " ===");
            for (Product product : products) {
                // Print each product's details
                System.out.println(product);  // Assuming Product has a meaningful toString() method

                // Fetch all items associated with this product
                List<Item> items = itemSQL.readAllByProduct(product.getName(), product.getCategory(), product.getSub_category(), product.getSize());

                if (items.isEmpty()) {
                    System.out.println("  No items found for this product.");
                } else {
                    System.out.println("  Items for this product:");
                    for (Item item : items) {
                        System.out.println("    " + item);  // Assuming Item has a meaningful toString() method
                    }
                }

                System.out.println("------------------------------------------");
            }
        }
    }

    public void generateCategoryReport(String category) throws SQLException {
        // Fetch all products by category
        List<Product> products = productSQL.readAllByCategory(category);

        if (products.isEmpty()) {
            System.out.println("No products found for category: " + category);
        } else {
            System.out.println("=== Category Report for " + category + " ===");
            for (Product product : products) {
                // Print each product's details
                System.out.println(product);  // Assuming Product has a meaningful toString() method

                // Fetch all items associated with this product
                List<Item> items = itemSQL.readAllByProduct(product.getName(), product.getCategory(), product.getSub_category(), product.getSize());

                if (items.isEmpty()) {
                    System.out.println("  No items found for this product.");
                } else {
                    System.out.println("  Items for this product:");
                    for (Item item : items) {
                        System.out.println("    " + item);  // Assuming Item has a meaningful toString() method
                    }
                }

                System.out.println("------------------------------------------");
            }
        }
    }

    public void generateAboutToFinishReport() {
        // Fetch all products with status "about_to_finish"
        List<Product> products = productSQL.readAllByStatus(E_Product_Status.about_to_finish);

        if (products.isEmpty()) {
            System.out.println("No products are about to finish.");
            return;
        }

        System.out.println("=== Products About to Finish Report ===");

        for (Product product : products) {
            System.out.println(product);  // Print the product details

            // If you want to print associated items, you can retrieve and print them here
            List<Item> items = itemSQL.readAllByProduct(product.getName(), product.getCategory(), product.getSub_category(), product.getSize());
            for (Item item : items) {
                System.out.println("   - " + item);  // Print item details
            }
        }
    }

    public void generateExpiredReport() {
        // Fetch all items with status "EXPIRED"
        List<Item> expiredItems = itemSQL.readAllByStatus(E_Item_Status.EXPIRED);

        if (expiredItems.isEmpty()) {
            System.out.println("No expired items found.");
            return;
        }

        System.out.println("=== Expired Items Report ===");

        // Iterate over expired items and print details
        for (Item item : expiredItems) {
            System.out.println(item);  // Print the item details

            // Optionally, you can fetch associated products if needed
            Product product = productSQL.readByCompositeKey(item.getName(), item.getCategory(), item.getSubCategory(), item.getSize());
            if (product != null) {
                System.out.println("   Associated Product: " + product);
            }
        }
    }

    public void generateAboutToExpireReport() {
        // Fetch all items with status "about_to_expire"
        List<Item> aboutToExpireItems = itemSQL.readAllByStatus(E_Item_Status.about_to_expire);

        if (aboutToExpireItems.isEmpty()) {
            System.out.println("No items are about to expire.");
            return;
        }

        System.out.println("=== Items About to Expire Report ===");

        // Iterate over items that are about to expire and print details
        for (Item item : aboutToExpireItems) {
            System.out.println(item);  // Print the item details

            // Optionally, fetch the associated product for each item
            Product product = productSQL.readByCompositeKey(item.getName(), item.getCategory(), item.getSubCategory(), item.getSize());
            if (product != null) {
                System.out.println("   Associated Product: " + product);  // Print associated product details
            }

        }
    }




    @Override
    public String toString() {
        List<Product> products = productSQL.readAll();
        StringBuilder result = new StringBuilder("Inventory:\n");
        for (Product product : products) {
            result.append(product.toString()).append("\n");
        }
        return result.toString();
    }

    //1st step: go through the orders_on_the_way rows, and check for orders that the date has already passed (or today).
    //if you find more than 0, then tell the user to update the inventory first before making a new shortcuts order, and exit.
    //make sure you have a method that lets the user delete an order that it's date has arrived (first show the order, then delete).
    //go through all the rows in the table: orders_on_the_way, each order that it's date has already passed or it's the current date:
    //fill the inventory with the product (based on the quantity ordered)

    public void ApplyOrderSupplier() {

        boolean flag = productSQL.checkProductsArrived();


        if (flag) {
            Scanner scanner = new Scanner(System.in);

            // Print message about products arrival and ask the user if they have updated quantities
            System.out.println("Products have arrived. Have you updated the inventory to reflect the minimum quantity of each products that arival?");
            System.out.print("Enter 'yes' or 'no': ");
            String userInput = scanner.nextLine().trim().toLowerCase();

            // Handle user input
            if ("yes".equals(userInput)) {
                System.out.println("You confirmed that the products have been added to the minimum quantity all the products that arival.");
                productSQL.OrderONTheWayRemove();

            } else if ("no".equals(userInput)) {
                System.out.println("You haven't added the products to the minimum quantity yet.");
                return;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        //2nd step: check for shortcuts for each product.
        //

        // Get the list of products that need to be reordered
        List<Product> productsBellowQuantity = productSQL.ProductsBellowQuntity();
        HashMap<String, Integer> productsToOrder = new HashMap<>();
        // If no products need to be ordered, print a message
        HashMap<String, Integer> products_hash = null;
        if (productsBellowQuantity.isEmpty()) {
            System.out.println("All products have sufficient quantity.");
        } else {
            // Loop through the list and print the product details
            for (Product product : productsBellowQuantity) {
                int unitsToOrder = product.getMin_quantity() - (product.getQuantity());
                productsToOrder.put(product.getName(), unitsToOrder);
            }


            products_hash = productSQL.CheckIfthatOlradyOrderTheProducts(productsToOrder);


        }
        ControllersManager.createOrderForShortage(products_hash);

    }

}
