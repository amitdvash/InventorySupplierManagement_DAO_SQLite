package dev.Inventory.Interfaces;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.time.LocalDate;

public interface I_Item {
    // Getter for item name
    String getName();

    // Setter for item name
    void setName(String name);

    // Getter for cost price
    double getCost_price();

    // Setter for cost price
    void setCost_price(double cost_price);

    // Getter for selling price
    double getSelling_price();

    // Setter for selling price
    void setSelling_price(double selling_price);

    // Getter for manufacturer
    String getManufacturer();

    // Setter for manufacturer
    void setManufacturer(String manufacturer);

    // Getter for item ID
    int getId();

    // Setter for item ID
    void setId(int id);

    // Getter for category
    String getCategory();

    // Setter for category
    void setCategory(String category);

    // Getter for sub-category
    String getSub_category();

    // Setter for sub-category
    void setSub_category(String sub_category);

    // Getter for size
    double getSize();

    // Setter for size
    void setSize(double size);

    // Getter for expiry date
    LocalDate getExpiry_date();

    // Setter for expiry date
    void setExpiry_date(LocalDate expiry_date);

    // Getter for status
    E_Item_Status getStatus();

    // Setter for status
    void setStatus(E_Item_Status status);

    // Getter for place
    E_Item_Place getPlace();

    // Setter for place
    void setPlace(E_Item_Place place);

    // Overridden toString method
    @Override
    String toString();
}
