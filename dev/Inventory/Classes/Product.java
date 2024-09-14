package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Enums.E_Product_Status;
import dev.Inventory.Interfaces.I_Product;


import java.time.LocalDate;
import java.util.*;

public class Product implements I_Product
{
    private HashMap<Integer , Item> items;
    private int quantity_in_store;
    private int quantity_in_warehouse;
    private int min_quantity;
    private String name;
    private String category;
    private String sub_category;
    private double size;
    private Discount discount;
    private E_Product_Status status;
    private boolean Discount_Status;
    public Product(String name, String category, String sub_category, double size,int min_quantity , Discount discount)
    {
        this.name = name;
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
        this.discount = discount;
        this.status = E_Product_Status.Out_of_stock;
        items = new HashMap<Integer , Item>();
        quantity_in_store = 0;
        quantity_in_warehouse = 0;
        this.min_quantity = min_quantity;

    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public void setItems(HashMap<Integer, Item> items) {
        this.items = items;
    }

    public int getQuantity_in_store() {
        return quantity_in_store;
    }

    public void setQuantity_in_store(int quantity_in_store) {
        this.quantity_in_store = quantity_in_store;
    }

    public int getQuantity_in_warehouse() {
        return quantity_in_warehouse;
    }

    public void setQuantity_in_warehouse(int quantity_in_warehouse) {
        this.quantity_in_warehouse = quantity_in_warehouse;
    }

    public int getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(int min_quantity) {
        this.min_quantity = min_quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
        for (Item item : items.values())
        {
            updateDiscountToItem(item );
        }

    }

    public E_Product_Status getStatus() {
        return status;
    }

    public void setStatus(E_Product_Status status) {
        this.status = status;
    }

    public boolean equals(Product product)//check
    {
        return this.name.equals(product.getName()) && this.category.equals(product.getCategory()) && this.sub_category.equals(product.getSub_category()) && this.size == product.getSize();
    }

    public boolean Matched_item_product(Item item)//check
    {
        return item.getCategory().equals(this.getCategory()) && item.getSub_category().equals(this.getSub_category()) && item.getSize() == this.getSize() && item.getName().equals(this.getName());
    }
    public void addItem(Item item)
    {
        if(!Matched_item_product(item))
        {
            throw new IllegalArgumentException( item.getName() + " ,id : " + item.getId()  + "does not match to" + this.name);
        }
        if(items.containsKey(item.getId()))
        {
            throw new IllegalArgumentException(item.getName() + " ,id : " + item.getId() +" already exists in " + this.name );
        }
        items.put(item.getId() , item);
        updateDiscountToItem(item);
        switch (item.getPlace())
        {
            case Store:
                quantity_in_store++;
                break;
            case Warehouse:
                quantity_in_warehouse++;
                break;
        }
        //Update the status of the product
       updateStatus();
    }

    private void updateDiscountToItem(Item item)
    {
        if ((isDiscountAvailable()))
        {
            item.applyDiscount(getDiscount().getDiscountRate());
        }
        else
        {
            item.cancelDiscount();
        }
    }

    public void removeItem(Item item)
    {
        if (items.containsKey(item.getId()))
        {
            items.remove(item.getId());
            switch (item.getPlace())
            {
                case Store:
                    quantity_in_store--;
                    break;
                case Warehouse:
                    quantity_in_warehouse--;
                    break;
            }
            //Update the status of the product
            updateStatus();
        }
        else
        {
            throw new IllegalArgumentException(item.getName() + " ,id : " + item.getId() + " does not exist in " + this.name);
        }
    }

    public void updateStatus()
    {
        /* update the status of the product
            and send a match notification
         */

        if (quantity_in_store+quantity_in_warehouse > 0)
        {
            if (quantity_in_store+quantity_in_warehouse <= min_quantity)
            {
                status = E_Product_Status.about_to_finish;
                SendNotification_AboutToFinish();
            }
            else
            {
                status = E_Product_Status.Available;
                SendNotification_Available();

            }
        }
        else
        {
            status = E_Product_Status.Out_of_stock;
            SendNotification_OutOfStock();
        }

    }
    private void SendNotification_AboutToFinish()
    {
        /*
        send a notification that the product is about to finish
         */
        System.out.println("Product Status: " + this.getName() + " is about to finish\n    items left : "+ (quantity_in_store +" in store and " +quantity_in_warehouse + " in warehouse"));
    }
    private void SendNotification_OutOfStock()
    {
        /*
        send a notification that the product is out of stock
         */
        System.out.println("Product Status: " + this.getName() + " is out of stock");
    }
    private void SendNotification_Available()
    {
        /*
        send a notification that the product is available
         */
        System.out.println("Product Status: " + this.getName() + " is available");
    }


    public void updateQuantityAfterMove(E_Item_Place before , E_Item_Place after)
    {
        if (before == E_Item_Place.Store)
        {
            quantity_in_store--;
        }
        else if (before == E_Item_Place.Warehouse)
        {
            quantity_in_warehouse--;
        }
        if (after == E_Item_Place.Store)
        {
            quantity_in_store++;
        }
        else if (after == E_Item_Place.Warehouse)
        {
            quantity_in_warehouse++;
        }
    }


    public void moveItemTo(Item item , E_Item_Place place)
       {
        if(item.getPlace() == place)
        {
            System.out.println("Item is already in the " + place.toString() );
            return;
        }
        updateQuantityAfterMove(item.getPlace() , place);
        if(item.getPlace() == E_Item_Place.Store)
        {
            item.setPlace(place);
        }
       else if(item.getPlace() == E_Item_Place.Warehouse)
        {
            item.setPlace(place);
        }

    }

    @Override
    public String toString() {
        String items_S = "";
        for (Item item : items.values())
        {
            items_S+= ("        " + item.toString() + "\n");
        }
        return name + "\n{" +
                " quantity_in_store=" + quantity_in_store +
                ", quantity_in_warehouse=" + quantity_in_warehouse +
                ", min_quantity=" + min_quantity +
                ", category='" + category + '\'' +
                ", sub_category='" + sub_category + '\'' +
                ", size=" + size +
                ", discount=" + (discount==null ? "none": discount.toString() )+
                ", status=" + status +
                "\n items:\n     { \n " + items_S +"     }\n" +
                '}';
    }

    public List<Item> getItemsByStatus(E_Item_Status Status)
    {
        List<Item> items = new ArrayList<Item>();
        for (Item item : this.items.values())
        {
            if (item.getStatus().equals(Status))
            {
                items.add(item);
            }
        }
        return items;
    }
    public List <Item> getItemsByPlace(E_Item_Place place)
    {
        List<Item> items = new ArrayList<Item>();
        for (Item item : this.items.values())
        {
            if (item.getPlace() == place)
            {
                items.add(item);
            }
        }
        return items;
    }

    public String HashCode()
    {
        return this.name +"_"+ this.category +"_"+ this.sub_category +"_"+ this.size;
    }
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        return (anObject instanceof Product product)
                && this.name.equals(product.getName()) &&
                this.category.equals(product.getCategory()) &&
                this.sub_category.equals(product.getSub_category()) &&
                this.size == product.getSize();
    }
public boolean isDiscountAvailable()
{
    if(this.discount == null)
    {
        return false;
    }
    return this.discount.isAvailable();
}


//    public void activateDiscount() {
//        // Check if discount is available and valid for the current date
//        if (this.discount != null && discount.isAvailable(LocalDate.now())) {
//            for (Item item : items.values()) {
//                double originalPrice = item.getSelling_price();
//                double discountRate = discount.getDiscountRate() / 100;
//                double newPrice = originalPrice - (originalPrice * discountRate);
//                item.setSelling_price(newPrice);
//            }
//            System.out.println("Discount activated for product: " + this.name);
//        } else {
//            System.out.println("No valid discount available for product: " + this.name);
//        }
//    }
//
//    public void activateDiscount() {
//        // Check if discount is available and valid for the current date
//        if (this.discount != null && discount.isAvailable(LocalDate.now())) {
//            for (Item item : items.values()) {
//                double originalPrice = item.getSelling_price();
//                double discountRate = discount.getDiscountRate() / 100;
//                double newPrice = originalPrice - (originalPrice * discountRate);
//                item.setSelling_price(newPrice);
//            }
//            System.out.println("Discount activated for product: " + this.name);
//        } else {
//            System.out.println("No valid discount available for product: " + this.name);
//        }
//


}
