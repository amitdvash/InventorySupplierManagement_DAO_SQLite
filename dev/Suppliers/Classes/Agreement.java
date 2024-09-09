package dev.Suppliers.Classes;

import java.util.List;
import java.util.Date;
import java.util.Map;

public class Agreement {
    private int agreementID;
    private List<Product> productList;
    private Date agreementDate;
    private Map<String, Map<Integer,Double>> discountDetails;
    private String supplyDays;

}


