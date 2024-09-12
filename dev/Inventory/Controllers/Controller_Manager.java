package dev.Inventory.Controllers;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Product;
import dev.Inventory.Classes.Inventory;
import dev.Inventory.Enums.E_Product_Status;

//to be implemented
//for limit access to the manager only
public class Controller_Manager extends Controller_Worker {

    public void setDiscount(Product product, Discount discount) {
        inventory.getProductByName(product.getName()).setDiscount(discount);
    }

    public void updateProductStatus(Product product, E_Product_Status status) {
        {
            product.setStatus(status);
        }
    }


    }