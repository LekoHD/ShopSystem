package com.lekohd.shopsystem.manager;

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.item.ItemClass;
import com.lekohd.shopsystem.item.ItemCreation;
import com.lekohd.shopsystem.util.ItemType;
import com.lekohd.shopsystem.util.ShopMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by Leon on 30.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class InventoryManager {

    private int size;
    private ShopMode shopMode;
    private ArrayList<ItemClass> items = new ArrayList<ItemClass>();

    public void setShopMode(ShopMode shopMode){
        this.shopMode = shopMode;
    }

    public ShopMode getShopMode(){
        return this.shopMode;
    }

    public InventoryManager (int size)
    {
        this.size = size;
        for (int i = 0; i < 45; i++)
        {
            items.add(new ItemClass(ItemType.NULL_ITEM.getItem(), -1, -1, i));
        }
        this.shopMode = ShopMode.BUY;
    }

    public void addItem(ItemClass item)
    {
        items.set(item.getSlot(), item);
    }

    public ArrayList getItems(){
        return items;
    }

    public void openInv(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 45, Locale.SHOP_CREATE);
        for (int i = 0; i < 45; i++)
        {
            if(i == 44)
            {
                inv.setItem(44, ItemType.SAVESHOP.getItem());
                break;
            }
            if(items.get(i).getPrice() == -1)
            {
                continue;
            } else {
                ItemClass item = items.get(i);
                ItemCreation it = new ItemCreation(null, item.getItem(), item.getPrice() + "", item.getAmount(), -1, true, null, this.shopMode);
                inv.setItem(item.getSlot(), it.getItem());
            }
        }
        p.openInventory(inv);
    }
    public void openUserInv(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 45, Locale.SHOP_NAME);
        for (int i = 0; i < 45; i++)
        {
            if(i == 44)
            {
                //inv.setItem(44, ItemType.SAVESHOP.getItem());
                break;
            }
            if(items.get(i).getPrice() == -1)
            {
                continue;
            } else {
                ItemClass item = items.get(i);
                ItemCreation it = new ItemCreation(null, item.getItem(), item.getPrice() + "", item.getAmount(), -1, false, null, this.shopMode);
                inv.setItem(item.getSlot(), it.getItem());
            }
        }
        p.openInventory(inv);
    }

}
