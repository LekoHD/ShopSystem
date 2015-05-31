package com.lekohd.shopsystem.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ItemClass {

    ItemStack item;
    int amount, price, slot;

    public ItemClass (ItemStack item, int amount, int price, int slot)
    {
        if(item.getType() == Material.THIN_GLASS && amount == -1 && price == -1 && slot == -1 )
        {

        }
        this.item = item;
        this.price = price;
        this.amount = amount;
        this.slot = slot;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getPrice()
    {
        return price;
    }

    public int getSlot()
    {
        return slot;
    }

}
