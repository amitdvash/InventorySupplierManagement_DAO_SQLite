package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Product_Status;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Facade Design Pattern - Inventory
//All the complexity of the Inventory is hidden from the user
//The user only needs to interact with the Inventory class
public class Inventory
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


//    public void removeProduct (I_Product product)
//    {
//        //Remove the product from the inventory
//        //If the product does not exist throw an exception
//        if (products.containsKey(product.getName()))
//        {
//            products.remove(product.getName());
//        }
//        else
//        {
//            throw new IllegalArgumentException("Product does not exist");
//        }
//    }

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
                    System.out.println(item.getName() + " ,id : " + item.getId() +" added successfully to " + product.getName());
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
        System.out.println("Product named " + item.getName() + " does not exist, creating a new product according to the item with default values for discount(null) and minimum quantity(5)-for set those values use the Setters");
        Product product = new Product(item.getName() , item.getCategory() , item.getSub_category() , item.getSize() ,5 , null );
        this.addProduct(product);
        System.out.println(item.getName() + " ,id : " + item.getId() +" added successfully to " + product.getName());
        this.products.get(item.getName()).addItem(item);
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
                    System.out.println(item.getName() + " ,id : " + item.getId() +" removed successfully from " + product.getName());

                    return;
                }
                catch (IllegalArgumentException e) {
                    //If the item does not exist in the product, throw an exception
                    System.out.println(e.getMessage());
                    return;

                }
            }
        }
        //If the product matches to this item does not exist, throw an exception
        System.out.println(item.getName() + " ,id : " + item.getId() +" does not exist in the inventory");
//        throw new IllegalArgumentException("Item does not exist in the inventory");
    }




    public void addProduct(Product product)
    {
        //Add a product to the inventory
        for (Product prod : products.values())
        {
            if (prod.equals(product))
            {
                //If the product already exists throw an exception
                System.out.println("Product : "+ product.getName() + " already exists in the inventory ");
                return;
            }
        }

        products.put(product.getName() , product);
        System.out.println("Product Creation: " +product.getName() + " added successfully to the inventory");
    }
    public Product getProductByName(String name)
    {
        //Return the product with the given name
        //If the product does not exist, return null
        return products.get(name);
    }
    public List<Product> getProductsByStatus(E_Product_Status status)
    {
        /*Return a list of products with the given status
        If the status is null, return all the products in the inventory
        */
        if (status == null)
        {
            return new ArrayList<Product>(products.values());
        }
       List<Product> result = new ArrayList<>();
       for(Product prod : products.values())
       {
           if(prod.getStatus() == status)
           {
               result.add(prod);
           }
       }
       return result;
    }


    public List<Product> getProductsByCategory(String category)
    {
        //Return a list of products with the given category
        //If the category is null, return all the products in the inventory
        if (category == null)
        {
            return new ArrayList<Product>(products.values());
        }
        List<Product> result = new ArrayList<>();
        for(Product prod :products.values())
        {
            if(prod.getCategory().equals(category))
            {
                result.add(prod);
            }
        }
        return result;
    }
    public List<Product> getProductsBySubCategory(String Subcategory)
    {
        //Return a list of products with the given category
        //If the category is null, return all the products in the inventory
        if (Subcategory == null)
        {
            return new ArrayList<Product>(products.values());
        }
        List<Product> result = new ArrayList<>();
        for(Product prod : products.values())
        {
            if(prod.getSub_category().equals(Subcategory))
            {
                result.add(prod);
            }
        }
        return result;
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
        System.out.println("Item does not exist in the inventory");
    }
}
