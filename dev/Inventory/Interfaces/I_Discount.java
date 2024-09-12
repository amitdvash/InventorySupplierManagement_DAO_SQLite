package dev.Inventory.Interfaces;

import java.time.LocalDate;

public interface I_Discount {

    // Gets the discount rate
    double getDiscountRate();

    // Sets the discount rate
    void setDiscountRate(double discountRate);

    // Gets the start date of the discount
    LocalDate getStart_date();

    // Sets the start date of the discount
    void setStart_date(LocalDate start_date);

    // Gets the end date of the discount
    LocalDate getEnd_date();

    // Sets the end date of the discount
    void setEnd_date(LocalDate end_date);

    // Checks if the discount is available on a given date
    boolean isAvailable();

}
