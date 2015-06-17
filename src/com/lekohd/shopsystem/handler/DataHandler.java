package com.lekohd.shopsystem.handler;

import com.lekohd.shopsystem.ShopSystem;
import com.lekohd.shopsystem.manager.InventoryManager;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by thorstenkoth on 07.06.15.
 */
public class DataHandler {

    public static void loadAll()
    {
        if (ShopSystem.settingsManager.getConfig().getBoolean("config.UseMySql")) {

        }
        else
        {
            ShopSystem.dataManager.loadAll();
        }
    }

    public static void saveShopByLocation(Location loc, UUID owner, InventoryManager inventoryManager, UUID shopUUID, boolean singleSave)
    {

    }

    public static void saveAllShops()
    {

    }


}
