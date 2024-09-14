package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;
import dev.Inventory.Interfaces.I_Discount;
import dev.Inventory.Interfaces.I_Inventory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Facade Design Pattern - Inventory
//All the complexity of the Inventory is hidden from the user
//The user only needs to interact with the Inventory class

public class Inventory implements I_Inventory
{
    //A Singleton Inventory
    //There is only one instance of the Inventory
    private HashMap<String , Product> products;
    private static Inventory instance = null;

    private Inventory()
    {
        //Initialize the Inventory
        products = new HashMap<String , Product>();
    }
    public static Inventory getInstance()
    {
        if(instance == null)
        {
            instance = new Inventory();
        }
        return instance;
    }


    public void addProduct(Product product)
    {
        //Add a product to the inventory
        for (Product prod : products.values())
        {
            if (prod.equals(product))
            {
                //If the product already exists throw an exception
                errorMsg("Product : "+ product.getName() + " already exists in the inventory ");
                return;
            }
        }
        products.put(product.HashCode() , product);
        SystemMsg("Product Creation: " +product.getName() + " added successfully to the inventory");
    }

    public void removeProduct (Product product)
    {
        //Remove the product from the inventory
        //If the product does not exist throw an exception
        if (products.containsKey(product.HashCode()))
        {
            products.remove(product.HashCode());
        }
        else
        {
            errorMsg("Product does not exist in the inventory");
        }
    }

    public void addItem(Item item)
    {
        //Add an item to the inventory
        for (Product product : products.values())
        {
            if (product.Matched_item_product(item))
            {
                //If the product matches to this item, add the item to the product
                try
                {
                    product.addItem(item);
                    item.updateStatus();
                    SystemMsg(item.getName() + " ,id : " + item.getId() +" added successfully to " + product.getName());
                    return;
                }
                catch (IllegalArgumentException e) {
                    //If the item already exists in the product, throw an exception
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }

        //If the product matches to this item does not exist, create a new product and add the item to it
        errorMsg("Product named: " + item.getName() + " from Category: " + item.getCategory() + " and SubCategory: " + item.getSub_category() + " does not exist , so this item cannot be added\n please add a matching product first");
//        Product product = new Product(item.getName() , item.getCategory() , item.getSub_category() , item.getSize() ,5 , null );
//        this.addProduct(product);
//        System.out.println(item.getName() + " ,id : " + item.getId() +" added successfully to " + product.getName());
//        this.products.get(item.getName()).addItem(item);
    }
    public void removeItem(Item item)
    {
        //Remove an item from the inventory
        for (Product product : products.values())
        {
            //If the product matches to this item, remove the item from the product
            //inside the method of the product there is a check if the item exists

            if (product.Matched_item_product(item))
            {
                try
                {
                    product.removeItem(item);
                    SystemMsg(item.getName() + " ,id : " + item.getId() +" removed successfully from " + product.getName());

                    return;
                }
                catch (IllegalArgumentException e) {
                    //If the item does not exist in the product, throw an exception
                    System.out.println(e.getMessage());
                    return;

                }
            }
        }
        //If the product matches to this item does not exist, send a message
        errorMsg(item.getName() + " ,id : " + item.getId() +" does not exist in the inventory");
//        throw new IllegalArgumentException("Item does not exist in the inventory");
    }





    public List<Product> getProductsByName(String name)
    {
        //Return the product with the given name
        //If the product does not exist, return null
        return products.values().stream().filter(product -> product.getName().equals(name)).toList();
    }
    public List<Product> getProductsByStatus(E_Product_Status status)
    {
        /*Return a list of products with the given status
        if the status is null , return empty list
        */
        return products.values().stream().filter(product -> product.getStatus() == status).toList();
    }


    public List<Product> getProductsByCategory(String category)
    {
        //Return a list of products with the given category
        //If the category is null, return empty list
        return products.values().stream().filter(product -> product.getCategory().equals(category)).toList();
    }
    public List<Product> getProductsBySubCategory(String Subcategory)
    {
        //Return a list of products with the given category
        //If the category is null, return empty list
        return products.values().stream().filter(product -> product.getSub_category().equals(Subcategory)).toList();
    }
    public List<Product> getProductsBySize(int size)
    {
        //Return a list of products with the given size
        //If the size is null, return empty list
        return products.values().stream().filter(product -> product.getSize() == size).toList();
    }
    public List<Item> getItemsByStatus(E_Item_Status Status)
    {
        //Return a list of items with the given place
        //If the place is null, return empty list
        List<Item> items = new ArrayList<Item>();
        for (Product product : products.values())
        {
            for (Item item : product.getItemsByStatus(Status))
            {
                items.add(item);
            }
//            items.addAll(product.getItemsByStatus(Status));
        }
        return items;
    }
    public List<Item> getItemsByPlace(E_Item_Place place)
    {
        //Return a list of items with the given place
        //If the place is null, return empty list
        List<Item> items = new ArrayList<Item>();
        for (Product product : products.values())
        {
            for (Item item : product.getItemsByPlace(place))
            {
                items.add(item);
            }
//            items.addAll(product.getItemsByStatus(Status));
        }
        return items;
    }
    @Override
    public String toString() {
        String prod_S = "";
        for (Product prod : products.values())
        {
            prod_S+= ( prod.toString() + "\n");
        }
        return "Inventory\n{" +
                "products : \n" + prod_S +
                '}';
    }

    public void moveItemTo(Item item, E_Item_Place place) {
        //Move an item to the given place
        //If the item does not exist, throw an exception
        for (Product product : products.values())
        {
            if (product.Matched_item_product(item))
            {
                product.moveItemTo(item , place);
                return;
            }
        }
        errorMsg("Item does not exist in the inventory");
    }
    public void SetMinQuantity(Product product , int min_quantity)
    {
        //Set the minimum quantity for the product
        //If the product does not exist, throw an exception
        if (products.containsKey(product.getName()))
        {
            products.get(product.getName()).setMin_quantity(min_quantity);
        }
        else
        {
            errorMsg("Product does not exist");
        }
    }
    public void errorMsg(String msg)
    {
        //Print an error message
        System.out.println(msg);
    }
    public void SystemMsg(String msg)
    {
        //Print a system message
        System.out.println(msg);
    }

    public void applyDiscountToProduct(Product p1, Discount discount) {
        //Apply a discount to the product
        //If the product does not exist, throw an exception
        int count=0;
        if (p1 == null)
        {
            errorMsg("Product does not exist");
            return;
        }
        if (products.containsKey(p1.HashCode()))
        {
            products.get(p1.HashCode()).setDiscount(discount);
            count++;
        }
        if (count == 0) {
            errorMsg("Product : " + p1.getName() +" does not exist");
        } else {
            SystemMsg(discount.toString() +" applied to all items in the Product : " + p1.HashCode());
        }
    }
    public void applyDiscountToCategory(String category, Discount discount)
    {
        //Apply a discount to the category
        //If the category does not exist, throw an exception
        int count =0 ;
        for (Product product : products.values())
        {
            if (product.getCategory().equals(category))
            {
                product.setDiscount(discount);
                count++;
            }
        }
        if (count == 0) {
            errorMsg("Category : " + category +" does not exist");
        } else {
            SystemMsg(discount.toString() +" applied to all items in the category : " + category);
        }

    }
    public void applyDiscountToSubCategory(String subcategory, Discount discount)
    {
        //Apply a discount to the sub-category
        //If the sub-category does not exist, throw an exception
        int count =0 ;
        for (Product product : products.values())
        {
            if (product.getSub_category().equals(subcategory))
            {
                product.setDiscount(discount);
                count++;
            }
        }
        if (count == 0) {
            errorMsg("SubCategory : " + subcategory +" does not exist");
        } else {
            SystemMsg(discount.toString() +" applied to all items in the Subcategory : " + subcategory);
        }
    }
    public Item findItem(String name, String category, String subCategory, double size, E_Item_Place place) {
        // Iterate through all products in the inventory
        for (Product product : products.values()) {
            // Check if the product matches the category and sub-category
            if (product.getCategory().equals(category) && product.getSub_category().equals(subCategory)) {
                // If the product matches, check its items for a match
                for (Item item : product.getItems().values()) {
                    // Check if the item matches the name, size, and place (store or warehouse)
                    if (item.getName().equals(name) && item.getSize() == size && item.getPlace() == place) {
                        return item; // Item found
                    }
                }
            }
        }
        // If no item is found, return null
        return null;
    }
    public Product findOrProduct(String name, String category, String subCategory, double size) {
    // Iterate through all products in the inventory
    for (Product product : products.values()) {
        // Check if the product matches the category, sub-category, and size
        if (product.getCategory().equals(category) && product.getSub_category().equals(subCategory) && product.getSize() == size && name.equals(product.getName())) {
            return product;
        }
    }
    // If neither an item nor a product is found, return null
    return null;
}

    public Product getProduct(String name , String Category , String SubCategory , double size)
    {
        //Return the product with the given name
        //If the product does not exist, return null
        if(products.containsKey(name+"_"+Category+"_"+SubCategory+"_"+size))
        {
            return products.get(name+"_"+Category+"_"+SubCategory+"_"+size);
        }
        return null;
    }
    public Product getProduct(Product product)
    {
        //Return the product with the given name
        //If the product does not exist, return null
        if(products.containsKey(product.HashCode()))
        {
            return products.get(product.HashCode());
        }
        return null;
    }

}
