package com.lekohd.shopsystem;

import com.lekohd.shopsystem.manager.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by Leon on 04.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ShopData {

    private int x,y,z;
    private String world;
    private String owner;
    private InventoryManager inventoryManager;

    public ShopData(Location loc, UUID owner, InventoryManager inventoryManager)
    {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = loc.getWorld().getName();
        this.owner = owner.toString();
        this.inventoryManager = inventoryManager;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public String getWorld()
    {
        return this.world;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public InventoryManager getInventoryManager()
    {
        return this.inventoryManager;
    }

    public Location getLocation()
    {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

}
