package dev.Suppliers.Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AgreementController {
    private List<Agreement> agreements = new ArrayList<>();

    // Create a new agreement
    public void addAgreement(Agreement agreement) {
        agreements.add(agreement);
        System.out.println("Agreement added: " + agreement.getAgreementID());
    }

    // Update agreement details
    public void updateAgreement(int agreementID, List<Product> products, Date agreementDate, HashMap<String, HashMap<Integer, Double>> discountDetails, String supplyDays, String paymentType) {
        Agreement agreement = getAgreement(agreementID);
        if (agreement != null) {
            agreement.setProductList(products);
            agreement.setAgreementDate(agreementDate);
            agreement.setDiscountDetails(discountDetails);
            agreement.setSupplyDays(supplyDays);
            agreement.setPaymentType(paymentType);
            System.out.println("Agreement updated: " + agreementID);
        } else {
            System.out.println("Agreement not found: " + agreementID);
        }
    }

    // Get an agreement by ID
    public Agreement getAgreement(int agreementID) {
        for (Agreement agreement : agreements) {
            if (agreement.getAgreementID() == agreementID) {
                return agreement;
            }
        }
        System.out.println("Agreement not found: " + agreementID);
        return null;
    }

    // Get all agreements
    public List<Agreement> getAllAgreements() {
        return agreements;
    }
}
