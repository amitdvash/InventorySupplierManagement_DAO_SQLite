package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Interfaces.I_Item;

import java.time.LocalDate;
import java.util.Date;

public class Item implements I_Item
{
    private String Name;
    private double cost_price;
    private double selling_price;
    private double price_after_discount;
    private String manufacturer;
    private int id;
    private String category;
    private String sub_category;
    private double size;
    private LocalDate expiry_date;
    private E_Item_Status status;
    private E_Item_Place place;
    public Item(String Name, double cost_price, double selling_price, String manufacturer, String category, String sub_category, double size, LocalDate expiry_date, E_Item_Status status, E_Item_Place place)
    {
        this.Name = Name;
        this.cost_price = cost_price;
        this.selling_price = selling_price;
        this.price_after_discount = selling_price;
        this.manufacturer = manufacturer;
        this.id = this.hashCode();
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
        this.expiry_date = expiry_date;
        this.status = status;
        this.place = place;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public double getPrice_after_discount() {
        return price_after_discount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(LocalDate expiry_date) {
        this.expiry_date = expiry_date;
    }

    public E_Item_Status getStatus() {
        return status;
    }

    public void setStatus(E_Item_Status status) {
        this.status = status;
    }


    public E_Item_Place getPlace() {
        return place;
    }


    public void setPlace(E_Item_Place place) {
        this.place = place;
    }

    public void applyDiscount(double discount)
    {
        price_after_discount = selling_price - (selling_price * discount / 100);
    }
    public void cancelDiscount()
    {
        price_after_discount = selling_price;
    }
    public void updateStatus()
    {
        if (expiry_date ==null)
            return;
        if (expiry_date.isBefore(LocalDate.now()))
        {
            status = E_Item_Status.EXPIRED;
            sendNotification_Expired();
        }
        else if(expiry_date.isBefore(LocalDate.now().plusDays(7)))
        {
            status = E_Item_Status.about_to_expire;
            sendNotification_AboutToExpire();
        }
    }

    private void sendNotification_AboutToExpire() {
        System.out.println("Item "+ Name + " is about to expire - last time to use is in : "+ expiry_date);
    }




    private void sendNotification_Expired() {
        System.out.println("Item "+ Name + " is expired");
    }

    public String toString() {
        return Name +" {" +
                " id=" + id +
                ", cost_price=" + cost_price +
                ", selling_price=" + selling_price +
                ", price after discount = " + price_after_discount + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", category='" + category + '\'' +
                ", sub_category='" + sub_category + '\'' +
                ", size=" + size +
                ", expiry_date=" + expiry_date +
                ", status=" + status +
                ", place=" + place +
                '}';
    }
}
