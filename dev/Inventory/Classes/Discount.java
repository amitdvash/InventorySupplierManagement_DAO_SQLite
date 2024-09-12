package dev.Inventory.Classes;

import dev.Inventory.Interfaces.I_Discount;

import java.time.LocalDate;
import java.util.Date;

public class Discount implements I_Discount
{
    private double discountRate;
    private LocalDate start_date;
    private LocalDate end_date;
    public Discount(double discountRate, LocalDate start_date, LocalDate end_date)
    {
        //Discount rate must be between 0 and 100
        setDiscountRate(discountRate);
        //Start date must be before end date
        if (start_date.isAfter(end_date))
            throw new IllegalArgumentException("Start date must be before end date");
        setStart_date(start_date);
        setEnd_date(end_date);


    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if(discountRate < 0 || discountRate > 100)
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        this.discountRate = discountRate;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
    public boolean isAvailable()
    {
        return LocalDate.now().isAfter(start_date) && LocalDate.now().isBefore(end_date);
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Discount{" +
                "discountRate=" + discountRate +
                "% , start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }
    //-------------------------------------------------------------------------------------------
}
