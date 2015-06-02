package com.lekohd.shopsystem.handler;

import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Leon on 01.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class PermissionHandler {

    public static int maxShops;
    public static String perm = "shopsystem.shop.create.";

    public PermissionHandler(int max)
    {
        maxShops = max;
    }

    public static boolean hasRequiredPermissions(Player user)
    {
        int currentShops = ShopSystem.dataManager.shopAmount.get(user.getUniqueId());
        if(user.isOp()) return true;
        if(currentShops >= maxShops)
            return false;
        for ( int i = maxShops; i > currentShops; i--)
        {
            if(user.hasPermission(perm + i))
            {
                return true;
            }
        }
        return false;
    }

}
