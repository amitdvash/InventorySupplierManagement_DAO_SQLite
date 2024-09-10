package dev.Inventory.Interfaces;

import dev.Inventory.Enums.E_Item_Place;

public interface I_Item {

   public int getId();

   E_Item_Place getPlace();

   void setPlace(E_Item_Place place);

   String getCategory();

   String getSub_category();

   double getSize();

   String getName();
}
